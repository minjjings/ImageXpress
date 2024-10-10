package image.module.data.presentation;

import image.module.data.application.ImageResponse;
import image.module.data.application.ImageService;
import image.module.data.domain.Image;
import image.module.data.domain.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/image/upload")
    public ImageResponse uploadImage(@RequestBody ImageRequest imageRequest){
        return imageService.createImage(imageRequest);
    }

    @PostMapping("/image/upload/resize")
    void uploadResizeImage(
            @RequestBody ImageRequest imageRequest
    ){
        imageService.uploadResizeImage(imageRequest);
    }
}
