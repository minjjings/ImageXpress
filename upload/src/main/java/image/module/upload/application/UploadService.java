package image.module.upload.application;

import image.module.upload.util.FileUtil;
import image.module.upload.domain.ImageExtension;
import image.module.upload.infrastructure.DataService;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.CompletableFuture;
import javax.imageio.ImageIO;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class UploadService {
    private final MinioClient minioClient;
    private final DataService dataService;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${minio.bucket}")
    private String bucketName;

    @Value("${cdn-server.url}")
    private String cdnBaseUrl;

    //이미지 데이터 db 저장
    public CompletableFuture<Void> saveImageMetadata(MultipartFile file) {
        return CompletableFuture.runAsync(() -> {
            try {
                // 파일 데이터를 미리 메모리에 저장
                byte[] fileBytes = file.getBytes();

                // 업로드 파일명을 불러옴
                String originalName = file.getOriginalFilename();

                // 파일 이름이 존재하지 않는 경우
                if (originalName == null) throw new IllegalArgumentException("잘못된 파일입니다.");

                // 파일 정보 분리
                String[] fileInfos = FileUtil.splitFileName(originalName);

                // 확장자 체크
                ImageExtension extension = ImageExtension.findByKey(fileInfos[1])
                        .orElseThrow(() -> new Exception("지원하지 않는 확장자 입니다."));

                // 이미지 크기 확인
                BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(fileBytes));
                int imageWidth = bufferedImage.getWidth();
                int imageHeight = bufferedImage.getHeight();

                ImageRequest imageRequest = ImageRequest.create(
                        originalName,
                        extension.getKey(),
                        cdnBaseUrl,
                        imageWidth,
                        imageHeight
                );

                // 메타데이터 저장
                ImageResponse imageResponse = dataService.uploadImage(imageRequest);

                // minio 이미지 업로드
                uploadImage(new ByteArrayInputStream(fileBytes), file.getContentType(), imageResponse);

            } catch (Exception e) {
                log.error("이미지 메타데이터 저장 중 오류 발생: ", e);
                throw new RuntimeException(e);
            }
        });
    }

    //이미지 업로드
    public void uploadImage(InputStream fileInputStream, String contentType, ImageResponse image) {
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(image.getStoredFileName())
                            .stream(fileInputStream, fileInputStream.available(), -1)
                            .contentType(contentType)
                            .build()
            );
            kafkaTemplate.send("image-upload-topic", image.getStoredFileName());
        } catch (Exception e) {
            log.error("이미지 업로드 중 오류 발생: ", e);
        }

    }


//    @SneakyThrows
//    public ResponseEntity<byte[]> getImage(String cdnUrl) {
//        byte[] imageBytes = minioClient.getObject(
//                GetObjectArgs.builder()
//                        .bucket(bucketName)
//                        .object("")
//                        .build()).readAllBytes();
//
//        // 응답 헤더 설정
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.parseMediaType("image/jpeg"));
//
//        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
//    }
//
//    @SneakyThrows
//    public ResponseEntity<byte[]> downloadImage(String cdnUrl){
//        byte[] imageBytes = minioClient.getObject(
//                GetObjectArgs.builder()
//                        .bucket(bucketName)
//                        .object("")
//                        .build()).readAllBytes();
//
//        // 응답 헤더 설정
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.parseMediaType("image/jpeg"));
//        headers.setContentDispositionFormData("attachment", "test.jpeg");
//
//        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
//    }
}
