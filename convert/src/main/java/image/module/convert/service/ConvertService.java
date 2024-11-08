package image.module.convert.service;

import image.module.convert.dto.EcommerceImageSize;
import image.module.convert.dto.ImageKafkaMessage;
import image.module.convert.util.ImageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Slf4j
@Service
public class ConvertService {

  private final ImageUtil imageUtil;
  private final KafkaTemplate<String, ImageKafkaMessage> kafkaTemplate;
  private final RedisService redisService;

  public ConvertService(ImageUtil imageUtil, KafkaTemplate<String, ImageKafkaMessage> kafkaTemplate, RedisService redisService) {
    this.imageUtil = imageUtil;
    this.kafkaTemplate = kafkaTemplate;
    this.redisService = redisService;
  }

  @Transactional
  public ResponseEntity<String> convert(MultipartFile image, String originalFilename, EcommerceImageSize imageSize) throws IOException {
    // 이미지 포맷 확인
    String imageFormat = imageUtil.getImageFormat(image);
    log.info("Image format: {}", imageFormat);

    if (imageFormat.equals("Unknown")) {
      return ResponseEntity.badRequest().body(originalFilename);
    }

    // MultipartFile을 BufferedImage로 변환
    BufferedImage bufferedImage = ImageIO.read(image.getInputStream());
    // 색상 공간 변환
    bufferedImage = imageUtil.convertToRGB(bufferedImage);
    // JPEG로 압축
    String uploadFileName = originalFilename.replaceFirst("[.][^.]+$", "") + ".jpg"; // .jpg로 변경
    // JPEG 이미지로 압축 및 메타데이터 제거
    byte[] compressedImageBytes = imageUtil.compressImage(bufferedImage, 0.50f); // 품질 50%

    // Redis에 이미지 저장
    redisService.saveImage(uploadFileName, compressedImageBytes);
    log.info("Saved image to Redis: {}", uploadFileName);

    // 이미지가 성공적으로 저장되었는지 확인

      // Kafka로 이미지 이름과 크기 전송
      kafkaTemplate.send("image-upload-topic", ImageKafkaMessage.fromEcommerceImageSize(uploadFileName, imageSize));
    kafkaTemplate.send("imate-resize-upload-topic", ImageKafkaMessage.fromEcommerceImageSize(uploadFileName, imageSize));
      log.info("Sent message to Kafka: {} with size: {}", uploadFileName, imageSize);


    return ResponseEntity.ok(uploadFileName);
  }

  //레디스에서 이미지 byte 조회
    public byte[] getRedisImageByte(String imageName) {

      // Redis에서 이미지 조회
     byte[] base64Image = redisService.getImage(imageName);
      log.info("Redis image is: {}", base64Image);

    return base64Image;
    }
}
