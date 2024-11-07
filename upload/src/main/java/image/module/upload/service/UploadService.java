package image.module.upload.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import image.module.upload.dto.ImageKafkaMessage;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


import java.io.ByteArrayInputStream;
import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class UploadService {
    private final MinioClient minioClient;
    private final RedisService redisService;

    @Value("${minio.buckets.uploadBucket}")
    private String uploadBucket; // 업로드 버킷 이름

    @KafkaListener(topics = "image-upload-topic", groupId = "image-upload-group")
    public ResponseEntity<String> imageUpload(String message) {
        try {
            // Kafka에서 수신한 메시지를 ImageKafkaMessage로 변환
            ImageKafkaMessage imageKafkaMessage = new ObjectMapper().readValue(message, ImageKafkaMessage.class);
            String uploadImageName = imageKafkaMessage.getUploadImageName();
            log.info("Upload image name: {}", uploadImageName);
            // Redis에서 이미지 데이터 조회
            byte[] imageBytes = redisService.getImage(uploadImageName);
            log.info("Upload image bytes: {}", imageBytes);
            if (imageBytes == null) {
                return ResponseEntity.badRequest().body("Image not found in Redis: " + uploadImageName);
            }

            // MinIO에 이미지 업로드
            uploadImageToMinIO(uploadImageName, imageBytes);

            return ResponseEntity.ok("Image uploaded successfully: " + uploadImageName);
        } catch (Exception e) {
            // 오류 처리
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error processing image upload: " + e.getMessage());
        }
    }

    private void uploadImageToMinIO(String uploadImageName, byte[] imageBytes) throws Exception {
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(uploadBucket)
                            .object(uploadImageName) // 업로드할 이미지 이름
                            .stream(new ByteArrayInputStream(imageBytes), imageBytes.length, -1)
                            .contentType("image/jpeg") // MIME 타입 설정
                            .build()
            );
            log.info("Image uploaded to MinIO successfully: {}", uploadImageName);
        } catch (IOException e) {
            log.error("File upload error: {}", e.getMessage());
            throw new IOException("File upload failed: " + e.getMessage());
        } catch (Exception e) {
            log.error("Image upload failed: {}", e.getMessage());
            throw new Exception("Image upload failed: " + e.getMessage());
        }
    }
}
