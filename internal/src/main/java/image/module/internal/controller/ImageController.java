package image.module.internal.controller;

import image.module.internal.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/images")
public class ImageController {
  private final ImageService imageService;

  @PostMapping("/resizeImage")
  public ResponseEntity<String> resizeImage(@RequestParam("fileName") String fileName) {
    try {
      // 전체 이미지 처리 로직을 관리하는 메서드
      String webpFileName = imageService.processImage(fileName);
      return ResponseEntity.ok("이미지 Processing 성공: " + webpFileName);
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
              .body("이미지 Processing 실패.");
    }
  }
}
