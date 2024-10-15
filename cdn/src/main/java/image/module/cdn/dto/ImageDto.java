package image.module.cdn.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImageDto {

    private byte[] imageStream;
    private String fileName;
}
