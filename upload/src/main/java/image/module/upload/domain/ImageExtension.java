package image.module.upload.domain;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.Getter;

@Getter
public enum ImageExtension {

    JPG(List.of("jpg", "JPG"), "image/jpeg"),
    JPEG(List.of("jpeg", "JPEG"), "image/jpeg"),
    PNG(List.of("png", "PNG"), "image/png");

    final List<String> key;
    final String contentType;

    ImageExtension(List<String> key, String contentType) {
        this.key = key;
        this.contentType = contentType;
    }

    public static Optional<ImageExtension> findByKey(String key) {
        return Arrays.stream(ImageExtension.values())
                .filter(extension -> extension.key.contains(key))
                .findAny();
    }
    
    public String getKey() {
        return this.key.isEmpty() ? "" : this.key.get(0);
    }
}
