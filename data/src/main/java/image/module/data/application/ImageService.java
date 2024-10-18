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
    public ImageResponse saveImage(ImageRequest request){
        Image image = Image.create(request);
        imageRepository.save(image);
        image.assignOriginalFileUUID();
        return ImageResponse.fromEntity(image);
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


    public void uploadResizeImage(ImageRequest imageRequest) {
        // UUID 추출 / _ 가로 x 세로 제거
        String storedFileName = extractOriginalFileName(imageRequest.getStoredFileName());

        Image image = imageRepository.findByStoredFileName(storedFileName).orElseThrow(
                () -> new IllegalArgumentException("저장된 파일 이름을 찾을 수 없습니다: " + storedFileName)
        );

       imageRepository.save(Image.createResize(image, imageRequest));
    }

    // _가로 x 세로 제거
    private String extractOriginalFileName(String storedFileName) {
        // 마지막 언더바(_)의 인덱스 찾기
        int lastIndex = storedFileName.lastIndexOf('_');
        // 마지막 언더바 이전의 부분 추출
        if (lastIndex != -1) {
            return storedFileName.substring(0, lastIndex);
        }
        return storedFileName; // 언더바가 없을 경우 원본 그대로 반환
    }
}
