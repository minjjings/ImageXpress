package image.module.upload.infrastructure;

import image.module.upload.dto.ImageRequest;
import image.module.upload.dto.ImageResponse;

public interface DataService {
    ImageResponse uploadImage(ImageRequest imageRequest);
}
