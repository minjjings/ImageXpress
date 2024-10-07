package image.module.upload.infrastructure;

import image.module.upload.application.ImageResponse;

public interface DataService {
    ImageResponse uploadImage(ImageRequest imageRequest);
}
