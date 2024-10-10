package image.module.url.client.data;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

public interface DataService{
    //이미지 이름 조회
    ImageResponse getImageName(UUID id);


    //이미지 cdn -> 이름 조회
    ImageResponse getCDNImageName(String cdnUrl);

}
