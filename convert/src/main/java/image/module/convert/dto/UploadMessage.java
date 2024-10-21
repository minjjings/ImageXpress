package image.module.convert.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class UploadMessage {
    private String storedFileName;
    private Integer requestSize;
}