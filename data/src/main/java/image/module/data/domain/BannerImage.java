package image.module.data.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

public class BannerImage {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
//@Where(clause = "is_deleted = false")
    @Entity(name = "image")
    public class Image extends BaseEntity {
        @Id
        @GeneratedValue
        @Column(name = "id")
        private UUID id;

        //이미지 원본 이름

        @Column(nullable = false)
        private String originalFileName;

        // minio에 저장된 이름

        @Column(nullable = false)
        private String storedFileName;

        // cdn url
        @Column(nullable = false)
        private String cdnUrl;

        //썸네일 이미지 150 x 150,
        //카테고리 리스트 이미지 300 x 300
        //상세보기 이미지 600 x 600 ,
        //확대 이미지 1200x 1200
        //배너 이미지 1920 x 500

        @Column(nullable = false)
        private String fileType;

        //이미지 사이즈 (fileType에 따라 이미지 결정)

        @Column(nullable = false)
        private Integer width;

        @Column(nullable = false)
        private Integer height;

        @Column(nullable = false)
        private UUID postId;


//        public static Image create(ImageRequest request) {
//            return Image.builder()
//                    .originalFileName(request.getOriginalFileName())
//                    .storedFileName(request.getStoredFileName())
//                    .fileType(request.getFileType())
//                    .width(request.getW)
//                    .build();
//        }


    }
}
