package image.module.data.presentation;

import image.module.data.application.ImageResponse;
import image.module.data.application.ImageService;
import image.module.data.domain.Image;
import image.module.data.domain.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/image/upload")
    public ImageResponse uploadImage(@RequestBody ImageRequest imageRequest){
        return imageService.createImage(imageRequest);
    }
}
