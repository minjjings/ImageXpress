package image.module.data.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageRequest {

    private UUID id;
    private String originalFileName;
    private String storedFileName;
    private String cdnUrl;
    private String fileType;
    private Integer width;
    private Integer height;

}
