package image.module.url.controller;

import image.module.url.client.data.DataService;
import image.module.url.client.data.ImageResponse;
import image.module.url.dto.ImageDto;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;


import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

import java.util.UUID;

@Log4j2
@RestController
@RequestMapping("/fetch")
public class UrlController {

    private final DataService dataService;
    private final MinioClient minioClient;


    @Value("${minio.buckets.uploadBucket}")
    private String uploadBucket;

    @Value("${minio.buckets.downloadBucket}")
    private String downloadBucket;


    public UrlController(DataService dataService, MinioClient minioClient) {
        this.dataService = dataService;
        this.minioClient = minioClient;
    }


    @GetMapping("/cdnUrl")
    public String getImage(@RequestParam("id") UUID id) {

            //1.UUID로 cdnURl 조회

            ImageResponse imageResponse =dataService.getImageName(id);
            String cdnUrl = imageResponse.getCdnUrl();
            log.info(imageResponse.getCdnUrl());



            return cdnUrl;
    }


    // 이미지를 바이트 배열로 담아 ItemDto로 반환하는 경우, 클라인트가 이미지를 캐싱하고 사용하는 데 있어 몇가지 문제가 있음
    // 이미지 데이터가 JSON 응답에 포함되어 있기 때문에 CDN에서 캐싱하거나 브라우저에서 이미지로 바로 사용하기 어려움
    @GetMapping("/image")
    public ImageDto fetchImage(@RequestParam("cdnUrl") String cdnUrl) {
        try {
            // CDN URL을 통해 데이터베이스에서 파일 이름 조회
            log.info("Received CDN URL: {}", cdnUrl);
            ImageResponse imageResponse = dataService.getCDNImageName(cdnUrl);

            // imageResponse가 null인지 확인
            if (imageResponse == null) {
                throw new RuntimeException("No image found for the provided CDN URL.");
            }

            String fileName = imageResponse.getStoredFileName();
            String fileType = imageResponse.getFileType();
            log.info("Stored file name: {}", fileName);
            log.info("File type: {}", imageResponse.getFileType());

            String bucketToUser = fileType.equalsIgnoreCase("webp")?uploadBucket:downloadBucket;

            // 2. MinIO에서 데이터 조회
            InputStream inputStream = minioClient.getObject(GetObjectArgs.builder()
                    .bucket(bucketToUser)
                    .object(fileName)
                    .build());

            // InputStream이 null인지 확인
            if (inputStream == null) {
                throw new RuntimeException("InputStream is null for file: " + fileName);
            }

            log.info("InputStream obtained: {}", inputStream);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            //InputStream을 바이트 배열로 변환 및 이미지 형식에 따라 변환 처리
            if("webp".equalsIgnoreCase(fileType)) {
                //webp 이미지를 jpg로 변환
                BufferedImage image = ImageIO.read(inputStream);
                if(image == null) {
                    throw new IOException("Unsupported image format or corrupted image");
                }
                ImageIO.write(image, "jpg", outputStream); //jpg로 변환
            } else {
                //변환하지 않고 그대로 반환
                inputStream.transferTo(outputStream);
            }

            byte[] imageBytes = outputStream.toByteArray();

            // 바이트 배열이 비어 있는지 확인
            if (imageBytes == null || imageBytes.length == 0) {
                throw new RuntimeException("Image bytes are null or empty.");
            }

            log.info("Image bytes length: {}", imageBytes.length);



            // ImageDto 생성 및 반환
            return new ImageDto(fileName, imageBytes);
        } catch (IOException e) {
            log.error("I/O error occurred: {}", e.getMessage());
            throw new RuntimeException("Error processing the image: " + e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage());
            throw new RuntimeException("Error fetching image: " + e.getMessage());
        }
    }







}


