package image.module.upload.presentation;

import image.module.upload.application.UploadService;
import image.module.upload.application.ImageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
            ImageResponse imageResponse = uploadService.saveImageMetadata(file);
            // 2. 비동기적으로 MinIO에 파일 저장
            uploadService.uploadImage(file, imageResponse);

            return ResponseEntity.ok(imageResponse.getOriginalFileUUID());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Upload failed: " + e.getMessage());
        }
    }
}
