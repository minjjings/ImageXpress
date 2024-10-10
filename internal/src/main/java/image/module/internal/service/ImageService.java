package image.module.internal.service;

import com.sksamuel.scrimage.ImmutableImage;
import com.sksamuel.scrimage.webp.WebpWriter;
import image.module.internal.DataClient;
import image.module.internal.dto.ImageRequest;
import io.minio.*;
import lombok.SneakyThrows;
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

  @Value("${cdn-server.url}")
  private String cdnBaseUrl;

  private final MinioClient minioClient;
  private final DataClient dataClient;

  public ImageService(MinioClient minioClient, DataClient dataClient) {
    this.minioClient = minioClient;
    this.dataClient = dataClient;
  }

  // 전체 이미지 처리 로직을 관리하는 메서드
  @KafkaListener(topics = "image-upload-topic", groupId = "image-upload-group")
  public void listen(String message) {
 // jpg
    // 0. 확장자 추출
    String extension = extractExtensionFromMinio(message);
    // 1. 이미지 다운로드
    InputStream originalFile = downloadImage(message);
    // 2. 원본 이미지 복사
    File copyOriginalFile = copyOriginalImage(originalFile,extension);
    // 3. 복사 이미지를 300x300 리사이징
    // TODO 나중에 width, height 값을 받아서 처리
    File resizedFile = resizeImage(copyOriginalFile, 300, 300);
    // 4. 리사이즈된 이미지 WebP로 변환
    File webpFile = convertToWebp(message, resizedFile);
    // 5. WebP 이미지 업로드
    uploadWebPImage(webpFile);
    // 6. 업로드 된 WebP 이미지 DB 저장
    ImageRequest imageRequest = ImageRequest.create(webpFile, extension, 300, 300, cdnBaseUrl);
    dataClient.uploadResizeImage(imageRequest);
    // 7. 임시 파일 삭제
    cleanupTemporaryFiles(copyOriginalFile, resizedFile, webpFile);
  }

  // 확장자 추출 메서드
  private String extractExtensionFromMinio(String fileName) {
    try {
      // MinIO에서 객체의 메타데이터 가져오기
      StatObjectResponse statObject = minioClient.statObject(
              StatObjectArgs.builder()
                      .bucket(downloadBucket)    // 버킷 이름
                      .object(fileName)          // 치환된 이미지 객체 이름
                      .build()
      );

      // Content-Type 출력
      String contentType = statObject.contentType();

      // 확장자 결정
      String extension;
      if ("image/jpeg".equals(contentType) || "image/jpg".equals(contentType)) {
        extension = "jpg";
      } else if ("image/png".equals(contentType)) {
        extension = "png";
      } else {
        throw new IllegalArgumentException("지원되지 않는 파일 형식입니다: " + contentType);
      }
      return extension;

    } catch (Exception e) {
      throw new RuntimeException("확장자 추출 실패: " + e.getMessage(), e);
    }
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
  private File copyOriginalImage(InputStream originalFile, String extension) {
    File copyOriginalFile = null;
    try {
      copyOriginalFile = File.createTempFile("copy-", "." + extension); // 임시 파일 생성
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

  // 복사 이미지 WebP 파일로 변환
  // 손실 압축
  // TODO 이미지 가로 세로 받을 경우 최종 파일 이름 수정
  public File convertToWebp(String fileName, File copyOriginalFile) {
    try {
      String uploadFileName = FilenameUtils.getBaseName(fileName) + "_300x300"; // MINIO에 업로드할 최종 파일 이름
      File outputFile = new File(copyOriginalFile.getParent(), uploadFileName);

      return ImmutableImage.loader()
              .fromFile(copyOriginalFile)
              .output(WebpWriter.DEFAULT, outputFile);
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  // WebP 파일 업로드
  private void uploadWebPImage(File webpFile) {
    try (InputStream webpInputStream = new FileInputStream(webpFile)) {
      minioClient.putObject(PutObjectArgs.builder()
              .bucket(uploadBucket)
              .object(webpFile.getName())
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