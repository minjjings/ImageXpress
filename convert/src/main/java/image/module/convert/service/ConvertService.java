package image.module.convert.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.io.*;

@Slf4j
@Service

public class ConvertService {


  private final ImageUtil imageUtil;

  private final KafkaTemplate<String, ImageKafkaMessage> kafkaTemplate;
  private final RedisService redisService;
  public ConvertService(ImageUtil imageUtil, KafkaTemplate<String, String> kafkaTemplate,
                        RedisService redisService, ObjectMapper objectMapper, KafkaTemplate<String, ImageKafkaMessage> kafkaTemplate1) {

      this.imageUtil = imageUtil;

      this.redisService = redisService;

      this.kafkaTemplate = kafkaTemplate1;
  }


  public ResponseEntity<String> convert(MultipartFile image, String originalFilename , EcommerceImageSize imageSize) throws IOException {

    String imageFormat = imageUtil.getImageFormat(image);
    log.info("Image format: " + imageFormat);

    if (imageFormat.equals("Unknown")){
      return ResponseEntity.badRequest().body(originalFilename);
    }

    // MultipartFile을 BufferedImage로 변환
    BufferedImage bufferedImage = ImageIO.read(image.getInputStream());

    // 색상 공간 변환
    bufferedImage = imageUtil.convertToRGB(bufferedImage);

    // JPEG로 압축
    String uploadFileName = originalFilename.replaceFirst("[.][^.]+$", "") + ".jpg"; // .jpg로 변경

    // JPEG 이미지로 압축 및 메타데이터 제거
    byte[] compressedImageBytes = imageUtil.compressImage(bufferedImage, 0.50f); // 품질 75%

    // redis 로 캐싱
    redisService.saveImage(uploadFileName, compressedImageBytes);

    // kafka로 upload와 resizing 서버에 redis에 저장한 이미지 이름 전달

    log.info("Saved image to: " + uploadFileName);
    // ImageKafkaMessage를 JSON 문자열로 변환하여 전송

      kafkaTemplate.send("image-upload-topic", ImageKafkaMessage.fromEcommerceImageSize(uploadFileName,imageSize));


      return ResponseEntity.ok(uploadFileName);
  }
}