package image.module.upload.presentation;

import image.module.upload.application.UploadService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public SseEmitter uploadImage(@RequestParam("file") MultipartFile file,
                                  @RequestParam(value = "size") int size,
                                  @RequestParam(value = "cachingTime") int cachingTime
                                  ) throws IOException {
        SseEmitter emitter = new SseEmitter(600000L);
        try {
            emitter.send(SseEmitter.event().name("INIT").data("이미지 업로드가 시작되었습니다."));
            uploadService.saveImageMetadata(file, size, cachingTime)
                    .handle((result, ex) -> {
                        try {
                            if (ex == null) {
                                emitter.send(SseEmitter.event().name("SUCCESS").data("이미지 업로드 완료: " + result));
                                emitter.complete();
                            } else {
                                emitter.send(SseEmitter.event().name("ERROR").data(file.getOriginalFilename() + " 업로드 실패..." + ex.getMessage()));
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

    @PostMapping("/multi")
    public SseEmitter uploadImages(@RequestParam("files") MultipartFile[] files,
                                   @RequestParam(value = "sizes") int[] sizes,
                                   @RequestParam(value = "cachingTimes") int[] cachingTimes)  {
        // 파일 개수와 size, cachingTime 배열 길이가 동일한지 확인
        if (files.length != sizes.length || files.length != cachingTimes.length) {
            throw new IllegalArgumentException("파일 개수와 sizes, cachingTimes 배열 길이가 일치하지 않습니다.");
        }

        SseEmitter emitter = new SseEmitter(600000L);
        try {
            emitter.send(SseEmitter.event().name("INIT").data("이미지 업로드가 시작되었습니다."));

            List<CompletableFuture<Void>> futures = new ArrayList<>();

            // 각 파일에 대해 개별적으로 size, cachingTime 적용
            for (int i = 0; i < files.length; i++) {
                MultipartFile file = files[i];
                int size = sizes[i];
                int cachingTime = cachingTimes[i];

                CompletableFuture<Void> future = uploadService.saveImageMetadata(file, size, cachingTime)
                        .handle((result, ex) -> {
                            try {
                                if (ex == null) {
                                    // 개별 이미지 업로드 성공 시 알림
                                    emitter.send(SseEmitter.event().name("SUCCESS").data("이미지 업로드 완료: " + result));
                                } else {
                                    // 개별 이미지 업로드 실패 시 알림
                                    emitter.send(SseEmitter.event().name("ERROR").data(file.getOriginalFilename() + " 업로드 실패: " + ex.getMessage()));
                                }
                            } catch (IOException e) {
                                log.error("IOException 발생", e);
                                emitter.completeWithError(e);
                            }
                            return null;
                        });

                futures.add(future);
            }

            // 모든 업로드 작업 완료 후 알림
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).thenRun(() -> {
                try {
                    emitter.send(SseEmitter.event().name("COMPLETE").data("모든 이미지 업로드 작업이 완료되었습니다."));
                    emitter.complete();
                } catch (IOException e) {
                    log.error("IOException 발생", e);
                    emitter.completeWithError(e);
                }
            });

        } catch (Exception e) {
            try {
                emitter.send(SseEmitter.event().name("ERROR").data("업로드 실패..."));
            } catch (IOException ioException) {
                log.error("IOException 발생", ioException);
            }
            emitter.completeWithError(e);
        }

        return emitter;
    }

}
