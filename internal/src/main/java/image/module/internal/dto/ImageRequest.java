package image.module.internal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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

    public static ImageRequest create(File webpFile, String messageExtension, String cdnBaseUrl) {
        String storedFileName = webpFile.getName();
        String cdnUrl = cdnBaseUrl + "/" + UUID.randomUUID() + "." + messageExtension;

        // 6. width, height 구하기
        int imageWidth = 0;
        int imageHeight = 0;
        try {
            BufferedImage bufferedImage = ImageIO.read(webpFile);
            imageWidth = bufferedImage.getWidth();
            imageHeight = bufferedImage.getHeight();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ImageRequest.builder()
                .storedFileName(storedFileName)
                .cdnUrl(cdnUrl)
                .fileType("webp")
                .width(imageWidth)
                .height(imageHeight)
                .build();
    }
}
