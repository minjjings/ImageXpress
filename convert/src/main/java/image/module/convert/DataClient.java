package image.module.convert;

import image.module.convert.dto.ImageRequest;
import image.module.convert.dto.UpdateImageData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "data")
public interface DataClient {

  @PostMapping("/image/update")
  void updateImageData(@RequestBody UpdateImageData updateImageData);
}
