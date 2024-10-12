package image.module.upload.presentation;

import image.module.upload.application.UploadService;
import image.module.upload.application.ImageResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/image/upload")
public class UploadController {

    private final UploadService uploadService;

    @PostMapping
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            uploadService.saveImageMetadata(file);

            // 즉시 응답 반환
            return ResponseEntity.ok("이미지 업로드가 시작되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Upload failed: " + e.getMessage());
        }
    }

//    @GetMapping("/test")
//    public ResponseEntity<byte[]> getImage(HttpServletRequest request) {
//        return uploadService.getImage(request.getRequestURL().toString());
//    }
//
//    @GetMapping("/downloadTest")
//    public ResponseEntity<byte[]> downloadImage(HttpServletRequest request) {
//        return uploadService.downloadImage(request.getRequestURL().toString());
//    }
}
