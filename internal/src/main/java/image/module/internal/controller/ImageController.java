package image.module.internal.controller;

import image.module.internal.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
public class ImageController {
  private final ImageService imageService;

  @PostMapping("/upload")
  public ResponseEntity<String> uploadImage(
          @RequestParam("file") MultipartFile file
  ) {
    try {
      imageService.uploadImage(file);
      return ResponseEntity.ok("File uploaded successfully.");
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file.");
    }
  }

  @GetMapping("/download")
  public ResponseEntity<InputStreamResource> downloadFile(@RequestParam("file") String fileName) {
    try {
      InputStream imageStream = imageService.downloadImage(fileName);
      return ResponseEntity.ok()
              .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
              .contentType(MediaType.IMAGE_PNG) // 혹은 IMAGE_JPEG
              .body(new InputStreamResource(imageStream)); // imageStream을 InputStreamResource으로 감싸서 반환
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
  }

}