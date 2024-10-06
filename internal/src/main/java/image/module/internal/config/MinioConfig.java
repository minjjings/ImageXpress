package image.module.internal.config;

import io.minio.MinioClient;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {

  @Value("${minio.url}")
  private String url;

  @Value("${minio.access.key}")
  private String key;

  @Value("${minio.access.secret}")
  private String secret;

  @Bean
  public MinioClient minioClient() {
    return MinioClient.builder()
            .endpoint(url)
            .credentials(key, secret)
            .build();
  }
}
