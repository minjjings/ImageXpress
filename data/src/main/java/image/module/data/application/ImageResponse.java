package image.module.data.application;

import image.module.data.domain.Image;
import image.module.data.domain.repository.ImageRepository;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class ImageResponse {

    private UUID id;
    private String originalFileName;
    private String storedFileName;
    private String cdnUrl;
    private String fileType;
    private Integer size;
    private Integer cashingTime;
    private UUID originalFileUUID;

    public static ImageResponse fromEntity(Image image){
        return ImageResponse.builder()
                .id(image.getId())
                .originalFileName(image.getOriginalFileName())
                .storedFileName(image.getStoredFileName())
                .cdnUrl(image.getCdnUrl())
                .fileType(image.getFileType())
                .size(image.getSize())
                .cashingTime(image.getCashingTime())
                .originalFileUUID(image.getOriginalFileUUID())
                .build();
    }
}
