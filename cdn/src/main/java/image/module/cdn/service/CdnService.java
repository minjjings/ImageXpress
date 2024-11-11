package image.module.cdn.service;

import image.module.cdn.dto.ImageResponseDto;

import java.io.IOException;
import java.io.InputStream;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CdnService {

    private final RedisService redisService;
    private final MinioClient minioClient;

    @Value("${minio.buckets.uploadBucket}")
    private String uploadBucket;

    @Value("${minio.buckets.downloadBucket}")
    private String downloadBucket;

    @Transactional
    public ResponseEntity<byte[]> getImage(String imageName, String imageSize) {
        // Redis에서 이미지 조회
        String key = imageSize.toLowerCase() + "/" + imageName;
        byte[] imageValue = redisService.getImage(key);

        if (imageValue == null) {
            // 이미지가 Redis에 없으면 MinIO에서 조회
            String bucket = "ORIGINAL".equals(imageSize) ? downloadBucket : uploadBucket;

            try {
                // MinIO에서 이미지 바이트 배열로 조회
                imageValue = getImageByteArray(bucket, imageName);

                // 조회된 이미지를 Redis에 저장
                redisService.saveImage(key, imageValue);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(("Error fetching image: " + e.getMessage()).getBytes());
            }
        }

        return ResponseEntity.ok(imageValue);
    }

    // MinIO에서 이미지 바이트 배열로 가져오는 메서드
    private byte[] getImageByteArray(String bucket, String storageFileName) throws Exception {
        // MinIO에서 객체를 가져오는 InputStream을 받음
        InputStream inputStream = minioClient.getObject(GetObjectArgs.builder()
                .bucket(bucket)
                .object(storageFileName)
                .build());

        if (inputStream == null) {
            throw new RuntimeException(storageFileName + " 파일에 대한 InputStream이 null입니다.");
        }

        // InputStream을 byte[]로 변환
        byte[] byteArray = inputStream.readAllBytes();
        return byteArray;
    }


}



