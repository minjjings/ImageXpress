package image.module.upload.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageUploadMessage {
    private String storedFileName;
    private Integer requestSize;

    public static ImageUploadMessage createMessage(String storedFileName, int requestSize){
        return ImageUploadMessage.builder()
                .storedFileName(storedFileName)
                .requestSize(requestSize)
                .build();
    }
}
