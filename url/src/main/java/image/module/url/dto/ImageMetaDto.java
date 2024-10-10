package image.module.url.dto;

import lombok.Data;

import java.io.InputStream;

@Data
public class ImageMetaDto {
    private String fileName;
    private InputStream inputStream;

    public ImageMetaDto(String fileName, InputStream inputStream) {
        this.fileName = fileName;
        this.inputStream = inputStream;
    }
}
