package image.module.convert.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.File;
import java.util.UUID;

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

    public static ImageRequest create(File webpFile, String messageExtension, int width, int height, String cdnBaseUrl) {
        String storedFileName = webpFile.getName();
        String cdnUrl = cdnBaseUrl + "/" + UUID.randomUUID() + "." + messageExtension;

        return ImageRequest.builder()
                .storedFileName(storedFileName)
                .cdnUrl(cdnUrl)
                .fileType("webp")
                .width(width)
                .height(height)
                .build();
    }
}
