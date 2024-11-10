package image.module.data.domain;

import jakarta.persistence.Entity; // 추가
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity // JPA 엔티티로 지정
public class Original extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    // 이미지 원본 이름
    @Column(nullable = false)
    private String originalFileName;

    // minio에 저장된 이름
    @Column(nullable = false)
    private String storedFileName;

    // cdn url
    @Column(nullable = false)
    private String cdnUrl;

    // DTO를 엔티티로 변환하는 메서드
    public static Original create(String uploadImageName, String storedImageName, String cdnUrl) {
        return Original.builder()
                .originalFileName(uploadImageName)
                .storedFileName(storedImageName)
                .cdnUrl(cdnUrl)
                .build();
    }


}
