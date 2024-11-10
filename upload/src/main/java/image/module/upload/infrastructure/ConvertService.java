package image.module.upload.infrastructure;

import image.module.upload.dto.ImageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
@Service
public interface ConvertService {
    ResponseEntity<byte[]> getRedisImageByte(@RequestParam String imageName);

}
