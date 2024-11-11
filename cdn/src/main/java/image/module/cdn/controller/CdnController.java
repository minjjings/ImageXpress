package image.module.cdn.controller;

import image.module.cdn.service.CdnService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/cdn")
public class CdnController {

    private final CdnService cdnService;

    @GetMapping("/{imageType}/{imageName}")
    public ResponseEntity<InputStreamResource> getImage(@PathVariable String imageName, @PathVariable String imageType) {

        log.info("Get image {}", imageName);
        // 이미지를 서비스에서 가져옵니다. (InputStream 반환)
        InputStream imageInputStream = cdnService.getImage(imageName, imageType);

        if (imageInputStream == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }

        // 이미지가 있으면 200 OK 상태 코드와 함께 image/jpeg 형식으로 반환
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)  // 이미지 타입을 JPEG로 설정
                .body(new InputStreamResource(imageInputStream)); // InputStream을 InputStreamResource로 래핑하여 반환
    }


}