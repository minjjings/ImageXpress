package image.module.url.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Base64;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageDto {

    @JsonProperty("fileName")
    private String fileName;

    @JsonProperty("imageData")
    private String imageData; // Base64로 인코딩된 이미지 데이터

    // 생성자 수정: byte[]를 입력받아 imageData를 Base64로 인코딩
    public ImageDto(String fileName, byte[] imageBytes) {
        this.fileName = fileName;
        this.imageData = Base64.getEncoder().encodeToString(imageBytes); // Base64로 인코딩
    }
}
