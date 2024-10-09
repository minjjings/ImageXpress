package image.module.data.application;

import image.module.data.domain.Image;
import image.module.data.domain.repository.ImageRepository;
import image.module.data.presentation.ImageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ImageService {
    private final ImageRepository imageRepository;

    @Transactional
    public ImageResponse createImage(ImageRequest request){
        Image image = Image.create(request);
        return ImageResponse.fromEntity(imageRepository.save(image));
    }
}
