package image.module.internal.service;

import com.sksamuel.scrimage.ImmutableImage;
import com.sksamuel.scrimage.webp.WebpWriter;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

@Slf4j
@Service
public class ImageService {
  @Value("${minio.buckets.downloadBucket}")
  private String downloadBucket;

  @Value("${minio.buckets.uploadBucket}")
  private String uploadBucket;

  public ImageService(MinioClient minioClient) {
    this.minioClient = minioClient;
  }

  private final MinioClient minioClient;

  // 전체 이미지 처리 로직을 관리하는 메서드
  @Transactional
  @KafkaListener(topics = "image-upload-topic", groupId = "image-upload-group")
  public void listen(String message) {
    log.info("@@@@@@" + message);

    // 1. 이미지 다운로드
    InputStream originalFile = downloadImage(message);
    // 2. 원본 이미지 복사
    File copyOriginalFile = copyOriginalImage(originalFile);
    // 3. 복사 이미지 WebP로 변환
    File webpFile = convertToWebp(message, copyOriginalFile);
    // 4. WebP 이미지 업로드
    uploadWebPImage(message, webpFile);
    // 5. 임시 파일 삭제
    cleanupTemporaryFiles(copyOriginalFile, webpFile);
  }


  // 이미지 다운로드
  @SneakyThrows
  public InputStream downloadImage(String fileName) {
    return minioClient.getObject(
            GetObjectArgs.builder()
                    .bucket(downloadBucket)
                    .object(fileName)
                    .build()
    );
  }

  // 원본 이미지 복사
  @SneakyThrows
  private File copyOriginalImage(InputStream originalFile) {
    File copyOriginalFile = File.createTempFile("original-", ".temp"); // 임시 파일 생성
    FileUtils.copyInputStreamToFile(originalFile, copyOriginalFile); // 생성된 임시 파일에 원본 파일(originalFile) 복사
    return copyOriginalFile;
  }

  // 복사 이미지 WebP 파일로 변환
  public File convertToWebp(String fileName, File copyOriginalFile) {
    try {
      return
              // ImmutableImage 클래스를 사용하여 이미지 변환 작업을 위한 로더 객체 생성.
              ImmutableImage.loader()
                      .fromFile(copyOriginalFile)
                      // TODO 파일 이름은 리사이즈 구현 후 다시 설정해주기
                      .output(WebpWriter.DEFAULT, new File(copyOriginalFile.getParent(), fileName + ".webp"));
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  // WebP 파일 업로드
  @SneakyThrows
  private void uploadWebPImage(String originalFileName, File webpFile) {
    try (InputStream webpInputStream = new FileInputStream(webpFile)) {
      minioClient.putObject(PutObjectArgs.builder()
              .bucket(uploadBucket)
              .object(originalFileName.replace(".png", ".webp").replace(".jpg", ".webp"))
              .stream(webpInputStream, webpFile.length(), -1)
              .contentType("image/webp")
              .build());
    }
  }

  // 임시 파일 삭제
  private void cleanupTemporaryFiles(File... files) {
    for (File file : files) {
      if (file.exists()) {
        file.delete();
      }
    }
  }

}