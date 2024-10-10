package image.module.cdn.dto;

import java.io.InputStream;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImageDto {

    private InputStream imageStream;
    private String fileName;
}
