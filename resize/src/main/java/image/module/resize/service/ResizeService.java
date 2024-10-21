package image.module.resize.service;

import image.module.resize.dto.ReceiveKafkaMessage;
import io.minio.MinioClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ResizeService {

  @Value("${minio.buckets.uploadBucket}")
  private String uploadBucket;

  @Value("${cdn-server.url}")
  private String cdnBaseUrl;

  private final MinioClient minioClient;

  // private final DataClient dataClient;

  public ResizeService(MinioClient minioClient) {
    this.minioClient = minioClient;
  }

  @KafkaListener(topics = "image-resize-topic", groupId = "image-resize-group")
  public void ResizeImage(ReceiveKafkaMessage receiveKafkaMessage) {
    String webPFileName = receiveKafkaMessage.getWebPFileName();
    Integer size = receiveKafkaMessage.getSize();


  }
}