package image.module.upload;

import image.module.upload.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class controller {

    private final RedisService redisService;

    @GetMapping("check")
    public ResponseEntity<byte[]> checkImage() {
        String imageName = "test.jpg";

        // Redis에서 이미지 조회
        byte[] base64Image = redisService.getImage(imageName);


        System.out.println("Retrieved image (Base64): " + base64Image);
        return ResponseEntity.ok(base64Image);
    }
}
