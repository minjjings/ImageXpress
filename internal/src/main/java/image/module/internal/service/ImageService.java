package image.module.internal.service;

import com.sksamuel.scrimage.ImmutableImage;
import com.sksamuel.scrimage.webp.WebpWriter;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
  @KafkaListener(topics = "image-upload-topic", groupId = "image-upload-group")
  public void listen(String message) {

    // 1. 이미지 다운로드
    InputStream originalFile = downloadImage(message);
    // 2. 원본 이미지 복사
    File copyOriginalFile = copyOriginalImage(originalFile);
    // 3. 복사 이미지를 300x300 리사이징
    File resizedFile = resizeImage(copyOriginalFile, 300, 300);
    // 4. 리사이즈된 이미지 WebP로 변환
    File webpFile = convertToWebp(message, resizedFile);
    // 5. WebP 이미지 업로드
    uploadWebPImage(message, webpFile);
    // 6. 임시 파일 삭제
    cleanupTemporaryFiles(copyOriginalFile, resizedFile, webpFile);
  }

  // 이미지 다운로드
  public InputStream downloadImage(String fileName) {
    try {
      return minioClient.getObject(
              GetObjectArgs.builder()
                      .bucket(downloadBucket)
                      .object(fileName)
                      .build()
      );
    } catch (Exception e) {
      throw new IllegalArgumentException("이미지 다운로드 실패: " + e.getMessage());
    }
  }

  // 원본 이미지 복사
  private File copyOriginalImage(InputStream originalFile) {
    File copyOriginalFile = null;
    try {
      copyOriginalFile = File.createTempFile("copy-", ".png"); // 임시 파일 생성
      FileUtils.copyInputStreamToFile(originalFile, copyOriginalFile); // 생성된 임시 파일에 원본 파일 복사
    } catch (IOException e) {
      throw new IllegalArgumentException("원본 이미지 복사 실패: " + e.getMessage());
    }
    return copyOriginalFile;
  }

  // 복사 이미지 리사이징
  // 비율 고정 비율: 300x300
  private File resizeImage(File originalFile, int width, int height) {
    File resizedFile = new File(originalFile.getParent(), "stretched-" + originalFile.getName());
    try {
      // Thumbnails 라이브러리를 사용하여 지정한 크기로 왜곡
      Thumbnails.of(originalFile)
              .size(width, height) // 강제로 가로, 세로 크기 지정
              .keepAspectRatio(false) // 이미지 비율 유지하지 않기
              .toFile(resizedFile);
    } catch (IOException e) {
      throw new IllegalArgumentException("이미지 리사이징 실패: " + e.getMessage());
    }
    return resizedFile;
  }
  // 이미지 비율 유지 ex) 1000x500 -> 300 x 150
//  private File resizeImage(File originalFile, int width, int height) {
//    File resizedFile = new File(originalFile.getParent(), "resized-" + originalFile.getName());
//    try {
//      // Thumbnails 라이브러리를 사용하여 이미지 리사이징
//      Thumbnails.of(originalFile)
//              .size(width, height)
//              .toFile(resizedFile); // 리사이즈된 이미지를 저장
//    } catch (IOException e) {
//      throw new IllegalArgumentException("이미지 리사이징 실패: " + e.getMessage());
//    }
//    return resizedFile;
//  }

  // 복사 이미지 WebP 파일로 변환
  // 손실 압축
  public File convertToWebp(String fileName, File copyOriginalFile) {
    try {
      return ImmutableImage.loader()
              .fromFile(copyOriginalFile)
              .output(WebpWriter.DEFAULT, new File(copyOriginalFile.getParent(), FilenameUtils.getBaseName(fileName) + ".webp"));
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  // WebP 파일 업로드
  private void uploadWebPImage(String originalFileName, File webpFile) {
    try (InputStream webpInputStream = new FileInputStream(webpFile)) {
      minioClient.putObject(PutObjectArgs.builder()
              .bucket(uploadBucket)
              .object(FilenameUtils.getBaseName(originalFileName) + ".webp")
              .stream(webpInputStream, webpFile.length(), -1)
              .contentType("image/webp")
              .build());
    } catch (Exception e) {
      throw new IllegalArgumentException("WebP 파일 업로드 실패: " + e.getMessage());
    }
  }

  // 임시 파일 삭제
  private void cleanupTemporaryFiles(File... files) {
    for (File file : files) {
      if (file.exists()) {
        boolean deleted = file.delete();
        if (!deleted) {
          throw new IllegalArgumentException("임시 파일 삭제 실패: " + file.getAbsolutePath());
        }
      }
    }
  }
}