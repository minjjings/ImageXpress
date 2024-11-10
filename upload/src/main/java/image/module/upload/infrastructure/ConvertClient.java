package image.module.upload.infrastructure;

import image.module.upload.dto.ImageRequest;
import image.module.upload.dto.ImageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "convert")
public interface ConvertClient extends ConvertService {
    @GetMapping("/image/getRedisImageByte")
    ResponseEntity<byte[]> getRedisImageByte(@RequestParam String imageName);



}
