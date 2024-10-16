package image.module.url.service;

import image.module.url.client.data.DataService;
import image.module.url.client.data.ImageResponse;
import image.module.url.dto.ImageDto;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class UrlService {

    private final DataService dataService;
    private final MinioClient minioClient;

    @Value("${minio.buckets.uploadBucket}")
    private String uploadBucket;

    @Value("${minio.buckets.downloadBucket}")
    private String downloadBucket;

    public ImageDto fetchImage(String cdnUrl) {
        // CDN URL을 통해 데이터베이스에서 파일 이름 조회
        log.info("받은 CDN URL: {}", cdnUrl);
        ImageResponse imageResponse = dataService.getCDNImageName(cdnUrl);

        // imageResponse가 null인지 확인
        if (imageResponse == null) {
            throw new RuntimeException("제공된 CDN URL에 대한 이미지를 찾을 수 없습니다.");
        }

        String fileName = imageResponse.getStoredFileName();
        String fileType = imageResponse.getFileType();
        log.info("저장된 파일명: {}", fileName);
        log.info("파일 타입: {}", fileType);

        // ImageDto 생성 및 반환
        return new ImageDto(fileName);
    }

    public byte[] fetchImageByte(String cdnUrl) {

        try {
            // CDN URL을 통해 데이터베이스에서 파일 이름 조회
            log.info("받은 CDN URL: {}", cdnUrl);
            ImageResponse imageResponse = dataService.getCDNImageName(cdnUrl);

            // imageResponse가 null인지 확인
            if (imageResponse == null) {
                throw new RuntimeException("제공된 CDN URL에 대한 이미지를 찾을 수 없습니다.");
            }

            String fileName = imageResponse.getStoredFileName();
            String fileType = imageResponse.getFileType();
            log.info("저장된 파일명: {}", fileName);
            log.info("파일 타입: {}", fileType);

            String bucketToUse = fileType.equalsIgnoreCase("webp") ? uploadBucket : downloadBucket;

            // 2. MinIO에서 데이터 조회
            InputStream inputStream = minioClient.getObject(GetObjectArgs.builder()
                    .bucket(bucketToUse)
                    .object(fileName)
                    .build());

            // InputStream이 null인지 확인
            if (inputStream == null) {
                throw new RuntimeException(fileName + " 파일에 대한 InputStream이 null입니다.");
            }

            log.info("얻은 InputStream: {}", inputStream);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            // InputStream을 바이트 배열로 변환 및 이미지 형식에 따라 변환 처리
            if ("webp".equalsIgnoreCase(fileType)) {
                // webp 이미지를 jpg로 변환
                BufferedImage image = ImageIO.read(inputStream);
                if (image == null) {
                    throw new IOException("지원하지 않는 이미지 형식이거나 손상된 이미지입니다.");
                }
                ImageIO.write(image, "jpg", outputStream); // jpg로 변환
            } else {
                // 변환하지 않고 그대로 반환
                inputStream.transferTo(outputStream);
            }

            byte[] imageBytes = outputStream.toByteArray();

            // 바이트 배열이 비어 있는지 확인
            if (imageBytes == null || imageBytes.length == 0) {
                throw new RuntimeException("이미지 바이트가 null이거나 비어 있습니다.");
            }

            log.info("이미지 바이트 길이: {}", imageBytes.length);

            // ImageDto 생성 및 반환
            return imageBytes;
        } catch (IOException e) {
            log.error("입출력 오류 발생: {}", e.getMessage());
            throw new RuntimeException("이미지 처리 중 오류 발생: " + e.getMessage());
        } catch (Exception e) {
            log.error("예상치 못한 오류 발생: {}", e.getMessage());
            throw new RuntimeException("이미지 가져오기 중 오류 발생: " + e.getMessage());
        }
    }
}
