package image.module.upload.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "is_delete = false")
@Entity(name = "image")
public class Image extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    private String originalFileName;

    private String storedFileName;

    private String cdnUrl;

    private String fileType;

    private Integer width;

    private Integer height;

    @Column(name = "original_file_uuid")
    private UUID originalFileUUID;

    public static Image create(String original_file_name,
                               String cdn_url,
                               String file_type,
                               Integer width,
                               Integer height)
    {
        // 1. UUID 생성
        String uuid = UUID.randomUUID().toString();

        // 2. 현재 시간 포맷팅
        String formattedTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

        // 3. 저장할 파일 이름 생성 (이미지 이름 치환하기)
        String storedFileName = uuid + "_" + formattedTime;

        return Image.builder()
                .originalFileName(original_file_name)
                .storedFileName(storedFileName)
                .cdnUrl(cdn_url)
                .fileType(file_type)
                .width(width)
                .height(height)
                .build();
    }

    @PrePersist
    public void setOriginalFileUUID() {
         this.originalFileUUID = this.id;
    }
}
