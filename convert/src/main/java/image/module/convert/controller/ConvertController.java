package image.module.convert.controller;

import image.module.convert.dto.EcommerceImageSize;
import image.module.convert.service.ConvertService;
import image.module.convert.service.RedisService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor


@RequestMapping("/image")
public class ConvertController {

    private final ConvertService convertService;
    private final RedisService redisService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImages(@RequestParam("file") MultipartFile file,
                                               @RequestParam("imageSize")EcommerceImageSize imageSize)  {
        try {
            // 로깅: 업로드되는 파일의 이름
            log.info("Uploading file: {}", file.getOriginalFilename());

            // 파일 업로드
            convertService.convert(file, file.getOriginalFilename(), imageSize);

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

    //레디스에서 이미지 byte조회
    @GetMapping("/getRedisImageByte")
    public ResponseEntity<byte[]> getRedisImageByte(@RequestParam String imageName) {

       byte[] base64Image = convertService.getRedisImageByte(imageName);

        return ResponseEntity.ok(base64Image);
    }
    }

