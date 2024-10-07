package image.module.upload.infrastructure;

import image.module.upload.application.ImageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "data-service", url = "${data-server.url}")
public interface DataClient extends DataService {

    @PostMapping("/image/upload")
    ImageResponse uploadImage(@RequestBody ImageRequest imageRequest);
}
