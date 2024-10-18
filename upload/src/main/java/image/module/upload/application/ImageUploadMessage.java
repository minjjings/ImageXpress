package image.module.upload.application;

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
    private int imageSize;

    public static ImageUploadMessage createMessage(String storedFileName, int imageSize){
        return ImageUploadMessage.builder()
                .storedFileName(storedFileName)
                .imageSize(imageSize)
                .build();
    }
}
