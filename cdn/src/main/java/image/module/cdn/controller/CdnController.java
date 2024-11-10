package image.module.cdn.controller;

import image.module.cdn.dto.ImageResponseDto;
import image.module.cdn.service.CdnService;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/cdn")
public class CdnController {

    private final CdnService cdnService;

    @GetMapping("/{cdnImageName}")
    public ResponseEntity<String> getImage(@RequestParam String imagName,
                                           @RequestParam String imageSize) {

//        cdnService.getImage(imagName,imageSize);

     return ResponseEntity.ok("이미지 조회 완료 ");
    }


}
