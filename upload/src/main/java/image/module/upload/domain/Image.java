package image.module.upload.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
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
                               String stored_file_name,
                               String cdn_url,
                               String file_type,
                               Integer width,
                               Integer height)
    {
        return Image.builder()
                .originalFileName(original_file_name)
                .storedFileName(stored_file_name)
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
