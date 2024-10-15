package image.module.url.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ImageDto {

    @JsonProperty("fileName")
    private String fileName;

    @JsonProperty("imageData")
    private byte[] imageData; // 이미지 데이터

    public ImageDto(String fileName, byte[] imageBytes) {
        this.fileName = fileName;
        this.imageData = imageBytes;
    }
}
