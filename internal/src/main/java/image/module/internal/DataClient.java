package image.module.internal;

import image.module.internal.dto.ImageRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "data-service", url = "${data-server.url}")
public interface DataClient {

  @GetMapping("/image//upload/resize")
  void uploadResizeImage(
          @RequestBody ImageRequest imageRequest
  );
}