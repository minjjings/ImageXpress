package image.module.upload.presentation;

import image.module.upload.application.UploadService;
import image.module.upload.application.ImageResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/image/upload")
public class UploadController {

    private final UploadService uploadService;

    @PostMapping
    public SseEmitter uploadImage(@RequestParam("file") MultipartFile file, @RequestParam(value = "size", required = false) int size) throws IOException {
        SseEmitter emitter = new SseEmitter(600000L);
        try {
            emitter.send(SseEmitter.event().name("INIT").data("이미지 업로드가 시작되었습니다."));
            uploadService.saveImageMetadata(file, size)
                    .handle((result, ex) -> {
                        try {
                            if (ex == null) {
                                emitter.send(SseEmitter.event().name("SUCCESS").data("이미지 업로드 완료: " + result));
                                emitter.complete();
                            } else {
                                emitter.send(SseEmitter.event().name("ERROR").data("업로드 실패..."));
                                emitter.completeWithError(ex);
                            }
                        } catch (IOException e) {
                            log.error("IOException 발생", e);
                            emitter.completeWithError(e);
                        }
                        return null;
                    });

        } catch (Exception e) {
            emitter.send(SseEmitter.event().name("ERROR").data("업로드 실패..."));
            emitter.completeWithError(e);
        }
        return emitter;
    }

//    @GetMapping("/test")
//    public ResponseEntity<byte[]> getImage(HttpServletRequest request) {
//        return uploadService.getImage(request.getRequestURL().toString());
//    }
//
//    @GetMapping("/downloadTest")
//    public ResponseEntity<byte[]> downloadImage(HttpServletRequest request) {
//        return uploadService.downloadImage(request.getRequestURL().toString());
//    }
}
