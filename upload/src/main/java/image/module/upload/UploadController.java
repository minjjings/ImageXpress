package image.module.upload;

import image.module.upload.domain.Image;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
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

    // 치환된 이미지 이름으로 minio에 저장하기
    // Original_file_UUID 반환
    // 파일 업로드 API
    //    치환된 이미지 이름은 UUID_업로드 요청시간
    //    ex) d5f59b88-2d5f-4d89-9e21-35fc2bcd0012_20231004123456
    @PostMapping
    public ResponseEntity<UUID> uploadImage(@RequestParam("file") MultipartFile file) {
        // 1. DB에 파일 메타데이터 저장 및 응답
        Image image = uploadService.saveImageMetadata(file);
        // 2. 비동기적으로 MinIO에 파일 저장
        uploadService.uploadImage(file, image);

        return ResponseEntity.ok(image.getOriginalFileUUID());
    }
}
