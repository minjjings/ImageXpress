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
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Thumbnail extends BaseEntity {

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

        public static Thumbnail create(String uploadImageName, String storedImageName, String cdnUrl) {
            return Thumbnail.builder()
                    .originalFileName(uploadImageName)
                    .storedFileName(storedImageName)
                    .cdnUrl(cdnUrl)
                    .build();
        }
}
