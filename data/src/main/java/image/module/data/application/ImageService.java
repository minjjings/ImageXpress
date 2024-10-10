package image.module.data.application;

import image.module.data.domain.Image;
import image.module.data.domain.repository.ImageRepository;
import image.module.data.presentation.ImageRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ImageService {
    private final ImageRepository imageRepository;

    @Transactional
    public ImageResponse createImage(ImageRequest request){
        Image image = Image.create(request);
        return ImageResponse.fromEntity(imageRepository.save(image));
    }

    public ImageResponse getImageName(UUID id) {
        return ImageResponse.fromEntity(imageRepository.findById(id).orElse(null));
    }

    public ImageResponse getCDNImageName(String cdnUrl) {

        Image image = imageRepository.findByCdnUrl(cdnUrl);

        if(image != null){
            return ImageResponse.fromEntity(image);
        }else {
            //이미지가 없을 경우 처리
            throw new EntityNotFoundException("Image not found"+cdnUrl);
        }

    }
}
