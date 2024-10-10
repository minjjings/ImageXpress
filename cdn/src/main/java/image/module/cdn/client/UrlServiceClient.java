package image.module.cdn.client;

import image.module.cdn.dto.ImageDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "url")
public interface UrlServiceClient {

    @GetMapping("/fetch/image/{cdnUrl}")
    ImageDto fetchImage(@PathVariable("cdnUrl") String cdnUrl);
}
