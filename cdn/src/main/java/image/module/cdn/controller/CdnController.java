package image.module.cdn.controller;

import image.module.cdn.service.CdnService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/cdn")
public class CdnController {

    private final CdnService cdnService;
    @GetMapping("/{imageType}/{imageName}")
    public ResponseEntity<byte[]> getImage(@PathVariable String imageName, @PathVariable String imageType) {




       byte[] imageBytes= cdnService.getImage(imageName,imageType).getBody();

        if (imageBytes == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(("Image not found: " + imageName).getBytes());
        }

        // 이미지 응답 반환 (무조건 image/jpeg로 설정)
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)  // image/jpeg로 고정
                .body(imageBytes);
    }


}