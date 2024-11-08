package image.module.upload.infrastructure;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

public interface ConvertService {
    ResponseEntity<byte[]> getRedisImageByte(@RequestParam String imageName);
}
