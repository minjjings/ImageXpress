package image.module.data.presentation;

import image.module.data.application.ImageResponse;
import image.module.data.application.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;


@RestController
@RequiredArgsConstructor
@RequestMapping
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/image/upload")
    public ImageResponse uploadImage(@RequestBody ImageRequest imageRequest){
        return imageService.createImage(imageRequest);
    }

    //fetch -> 객체 조회
    @GetMapping("/image/getImageName")
    public ImageResponse getImageName(@RequestParam("id") UUID id){

        ImageResponse getImageName = imageService.getImageName(id);

        return getImageName;
    }

    //fetch -> cdn 주소로 객체 조회
    @GetMapping("/image/getCDNImageName")
    public ImageResponse getCDNImageName(@RequestParam("cdnUrl") String cdnUrl){

        ImageResponse getCDNImageName = imageService.getCDNImageName(cdnUrl);

        return getCDNImageName;
    }

    // 리사이즈 이미지 DB 저장
    @PostMapping("/image/upload/resize")
    void uploadResizeImage(
            @RequestBody ImageRequest imageRequest
    ){
        imageService.uploadResizeImage(imageRequest);
    }
}
