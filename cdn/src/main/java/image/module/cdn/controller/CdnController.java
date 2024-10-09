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
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/cdn")
public class CdnController {

    private final CdnService cdnService;

    @GetMapping("/{cdnImageName}")
    public ResponseEntity<?> getImage(HttpServletRequest request) {
        try {
            ImageResponseDto imageResponseDto = cdnService.getImage(request.getRequestURL().toString());
            return new ResponseEntity<>(imageResponseDto.getImageBytes(), imageResponseDto.getHeaders(), HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("IOException 발생", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/download/{cdnImageName}")
    public ResponseEntity<?> downloadImage(HttpServletRequest request) {
        try {
            ImageResponseDto imageResponseDto = cdnService.downloadImage(request.getRequestURL().toString());
            return new ResponseEntity<>(imageResponseDto.getImageBytes(), imageResponseDto.getHeaders(), HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("IOException 발생", HttpStatus.NOT_FOUND);
        }
    }
}
