package image.module.cdn.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "fetch")
public interface UrlServiceClient {

    @GetMapping(value = "/fetch/image/byte")
    ResponseEntity<byte[]> fetchImageByte(@RequestParam("cdnUrl") String cdnUrl);
}
