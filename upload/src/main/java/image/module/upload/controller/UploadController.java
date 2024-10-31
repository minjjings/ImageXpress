package image.module.upload.controller;

import image.module.upload.service.UploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;


@Slf4j
@RestController
@RequiredArgsConstructor

@RequestMapping("/image")
public class UploadController {

    private final UploadService uploadService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImages(@RequestParam("file") MultipartFile file)  {
        try {
            // 로깅: 업로드되는 파일의 이름
            log.info("Uploading file: {}", file.getOriginalFilename());

            // 파일 업로드
            uploadService.uploadImage(file, file.getOriginalFilename());

            // 성공적으로 업로드한 경우 응답 반환
            return ResponseEntity.ok("File uploaded successfully.");
        } catch (IOException e) {
            log.error("파일 업로드 중 오류 발생: {}", e.getMessage());
            return ResponseEntity.status(500).body("파일 업로드 중 오류 발생: " + e.getMessage());
        } catch (Exception e) {
            log.error("이미지 업로드 실패: {}", e.getMessage());
            return ResponseEntity.status(500).body("이미지 업로드 실패: " + e.getMessage());
        }
    }
    }








