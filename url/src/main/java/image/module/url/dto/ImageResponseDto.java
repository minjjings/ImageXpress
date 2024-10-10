package image.module.url.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ImageResponseDto {
    @JsonProperty("fileName")
    private String fileName;

    @JsonProperty("imageData")
    private String imageData; // Base64로 인코딩된 이미지 데이터

    public ImageResponseDto(String fileName, String imageData) {
        this.fileName = fileName;
        this.imageData = imageData;
    }

    public String getFileName() {
        return fileName;
    }

    public String getImageData() {
        return imageData;
    }
}
