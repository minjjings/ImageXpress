package image.module.cdn.client;

import image.module.cdn.dto.ImageDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "fetch")
public interface UrlServiceClient {

    @GetMapping(value = "/fetch/image")
    ImageDto fetchImage(@RequestParam("cdnUrl") String cdnUrl);
}
