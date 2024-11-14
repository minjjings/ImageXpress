package image.module.data.service;


import image.module.data.domain.*;

import image.module.data.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class ImageService {

    private final OriginalRepository originalRepository;
    private final BannerRepository bannerRepository;
    private final CategoryRepository categoryRepository;
    private final DetailRepository detailRepository;
    private final ZoomRepository zoomRepository;
    private final ThumbnailRepository thumbnailRepository;



    @Transactional
    public ResponseEntity<String> saveImage(String uploadImageName, String storedImageName) {

        String cdnUrl = "localhost:19096/cdn/"+storedImageName;

        Original original = Original.create(uploadImageName,storedImageName,cdnUrl);

        originalRepository.save(original);


        return ResponseEntity.ok("저장 완료되었습니다.");
    }

    @Transactional
    public void saveResizingImage(String uploadImageName, String storedImageName, String imageType) {

        if (imageType.equals("BANNER")){

            String cdnUrl = "localhost:19096/cdn/"+storedImageName;

            Banner banner = Banner.create(uploadImageName,storedImageName,cdnUrl);
            bannerRepository.save(banner);

        }

        if (imageType.equals("THUMBNAIL")){
            String cdnUrl = "localhost:19096/cdn/"+storedImageName;
            Thumbnail thumbnail = Thumbnail.create(uploadImageName,storedImageName,cdnUrl);
            thumbnailRepository.save(thumbnail);

        }

        if (imageType.equals("CATEGORY")){
            String cdnUrl = "localhost:19096/cdn/"+storedImageName;
            Category category = Category.create(uploadImageName,storedImageName,cdnUrl);
            categoryRepository.save(category);

        }

        if (imageType.equals("DETAIL")){
            String cdnUrl = "localhost:19096/cdn/"+storedImageName;
            Detail detail = Detail.create(uploadImageName,storedImageName,cdnUrl);
            detailRepository.save(detail);

        }

        if (imageType.equals("ZOOM")){
            String cdnUrl = "localhost:19096/cdn/"+storedImageName;
            Zoom zoom = Zoom.create(uploadImageName,storedImageName,cdnUrl);
            zoomRepository.save(zoom);
        }


    }

    public String getCdnUrl(String originalFileName, String imageType) {

        String cdnUrl= null;


        switch (imageType) {
            case "ORIGINAL":
                Original original = originalRepository.findByOriginalFileName(originalFileName);
                cdnUrl = original.getCdnUrl();
                break;
            case "BANNER":
                Banner banner =bannerRepository.findByOriginalFileName(originalFileName);
                cdnUrl = banner.getCdnUrl();
                break;
            case "ZOOM":
                Zoom zoom = zoomRepository.findByOriginalFileName(originalFileName);
                cdnUrl = zoom.getCdnUrl();
                break;
            case "DETAIL":
                Detail detail  = detailRepository.findByOriginalFileName(originalFileName);
                cdnUrl = detail.getCdnUrl();
                break;
            case "THUMBNAIL":
                Thumbnail thumbnail= thumbnailRepository.findByOriginalFileName(originalFileName);
                cdnUrl = thumbnail.getCdnUrl();
                break;
            case "CATEGORY":
                Category category = categoryRepository.findByOriginalFileName(originalFileName);
                cdnUrl = category.getCdnUrl();
                break;
            default:
                throw new IllegalArgumentException("지원하지 않는 이미지 타입입니다: " + imageType);
        }

        return cdnUrl;
    }


}