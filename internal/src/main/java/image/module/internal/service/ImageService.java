package image.module.internal.service;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
public class ImageService {
  @Value("${minio.bucketName}")
  private String bucketName;


  private final MinioClient minioClient;

  public ImageService(MinioClient minioClient) {
    this.minioClient = minioClient;
  }

  // 이미지 업로드
  @SneakyThrows
  public void uploadImage(MultipartFile file) {
    InputStream inputStream = file.getInputStream();
    minioClient.putObject(PutObjectArgs.builder()
            .bucket(bucketName)
            .object(file.getOriginalFilename())
            .stream(inputStream, inputStream.available(), -1)
            .build());
  }

  // 이미지 다운로드
  @SneakyThrows
  public InputStream downloadImage(String fileName) {
    return minioClient.getObject(
            GetObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .build() // GetObjectArgs 객체를 생성하여 넘깁니다.
    );
  }


}


