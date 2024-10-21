package image.module.url.controller;

import image.module.url.client.data.DataService;
import image.module.url.client.data.ImageResponse;
import image.module.url.dto.ImageDto;
import image.module.url.service.UrlService;
import java.util.UUID;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping("/fetch")
public class UrlController {

    private final DataService dataService;
    private final UrlService urlService;


    public UrlController(DataService dataService, UrlService urlService) {
        this.dataService = dataService;
        this.urlService = urlService;
    }


    @GetMapping("/cdnUrl")
    public String getImage(@RequestParam("id") UUID id) {

        //1.UUID로 cdnURl 조회

        ImageResponse imageResponse = dataService.getImageName(id);

        // cdnUrl이 null인지 확인
        if (imageResponse == null || imageResponse.getCdnUrl() == null) {
            throw new RuntimeException("이미지를 찾을 수 없거나 CDN URL이 없습니다.");
        }

        String cdnUrl = imageResponse.getCdnUrl();
        log.info("CDN URL: {}", cdnUrl);

        return cdnUrl;
    }


    // 이미지를 바이트 배열로 담아 ItemDto로 반환하는 경우, 클라인트가 이미지를 캐싱하고 사용하는 데 있어 몇가지 문제가 있음
    // 이미지 데이터가 JSON 응답에 포함되어 있기 때문에 CDN에서 캐싱하거나 브라우저에서 이미지로 바로 사용하기 어려움
    @GetMapping("/image")
    public ImageDto fetchImage(@RequestParam("cdnUrl") String cdnUrl) {
        return urlService.fetchImage(cdnUrl);
    }

    // 이미지 바이트 배열로 반환하는 FeignClient용 api
    @GetMapping("/image/byte")
    public ResponseEntity<byte[]> fetchImageByte(@RequestParam("cdnUrl") String cdnUrl) {

//        // 적절한 MIME 타입으로 응답을 설정 (예: 이미지의 경우)
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM); // 또는 MediaType.IMAGE_PNG 등
        return urlService.fetchImageByte(cdnUrl);
    }
}


