package image.module.upload.presentation;

import image.module.upload.application.UploadService;
import image.module.upload.application.ImageResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/image/upload")
public class UploadController {

    private final UploadService uploadService;

    @PostMapping
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            // 1. DB에 파일 메타데이터 저장 및 응답
            CompletableFuture<ImageResponse> imageResponseFuture = uploadService.saveImageMetadata(file);

            // 2. 비동기적으로 MinIO에 파일 저장
            imageResponseFuture.thenAccept(imageResponse ->
                    uploadService.uploadImage(file, imageResponse)
            );

            // 즉시 응답 반환
            return ResponseEntity.ok("이미지 업로드가 시작되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Upload failed: " + e.getMessage());
        }
    }

    @GetMapping("/test")
    public ResponseEntity<byte[]> getImage(HttpServletRequest request) {
        return uploadService.getImage(request.getRequestURL().toString());
    }

    @GetMapping("/downloadTest")
    public ResponseEntity<byte[]> downloadImage(HttpServletRequest request) {
        return uploadService.downloadImage(request.getRequestURL().toString());
    }
}
