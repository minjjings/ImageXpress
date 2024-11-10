package image.module.upload.infrastructure;

import image.module.upload.dto.ImageRequest;
import image.module.upload.dto.ImageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

public interface DataService {
    ResponseEntity<String> uploadImage(@RequestParam String uploadImageName,
                                       @RequestParam  String storedImageName);

}
