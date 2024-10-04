package image.module.upload;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/image/upload")
public class UploadController {

    private final UploadService minioService;

    // 파일 업로드 API
    @PostMapping
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            return minioService.uploadFile(file);
        } catch (Exception e) {
            e.printStackTrace();
            return "File uplaod failed!!";
        }
    }
}
