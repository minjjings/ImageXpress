package image.module.cdn.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpHeaders;

@Getter
@Setter
public class ImageResponseDto {
    private byte[] imageBytes;
    private String imageType;
    private HttpHeaders headers;
}
