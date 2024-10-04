package image.module.upload.domain;

import com.fasterxml.jackson.databind.ser.Serializers.Base;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "is_delete = false")
@Entity(name = "server_image_data")
public class ServerImageData extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    private String original_file_name;

    private String stored_file_name;

    private String cdn_url;

    private String file_type;

    private int width;

    private int height;

    @Column(nullable = false)
    private UUID original_file_UUID;
}
