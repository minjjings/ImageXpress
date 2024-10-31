package image.module.upload.dto;

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
    private String fileType;
    private Integer size;
    private UUID originalFileUUID;

    public ImageResponse(String 메타데이터_저장_성공) {
    }
}
