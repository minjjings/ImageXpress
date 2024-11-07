//package image.module.upload.service;
//
//import image.module.upload.dto.ImageRequest;
//import io.minio.MinioClient;
//import io.minio.PutObjectArgs;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.ResponseEntity;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//import java.io.ByteArrayInputStream;
//import java.io.IOException;
//
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class UploadService {
//    private final MinioClient minioClient;
//
//    @Value("${minio.buckets.uploadBucket}")
//    private String uploadBucket; // 업로드 버킷 이름
//
//    @KafkaListener(topics = "image-upload-topic",groupId = "image-upload-group")
//    public String u
////    public ResponseEntity<ImageRequest> uploadImage(MultipartFile image, String originalFileName) throws IOException {
////
////        try {
////
////            // MinIO에 업로드
////            minioClient.putObject(
////                    PutObjectArgs.builder()
////                            .bucket(uploadBucket)
////                            .object(uploadFileName) // .jpg로 변경된 파일 이름
////                            .stream(new ByteArrayInputStream(compressedImageBytes), compressedImageBytes.length, -1)
////                            .contentType("image/jpeg")
////                            .build()
////            );
////
////            return ResponseEntity.ok(new ImageRequest("이미지 업로드 성공"));
////        } catch (IOException e) {
////            log.error("파일 업로드 중 오류 발생: {}", e.getMessage());
////            return ResponseEntity.status(500).body(new ImageRequest("서비스 파일 업로드 중 오류 발생: " + e.getMessage()));
////        } catch (Exception e) {
////            log.error("이미지 업로드 실패: {}", e.getMessage());
////            return ResponseEntity.status(500).body(new ImageRequest("이미지 업로드 실패: " + e.getMessage()));
////        }
////    }
////
////
////    }
//}
