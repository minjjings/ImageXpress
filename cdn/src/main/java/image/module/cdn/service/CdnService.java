package image.module.cdn.service;

import image.module.cdn.client.UrlServiceClient;
import image.module.cdn.dto.ImageDto;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CdnService {

    private final RedisService redisService;
    private final UrlServiceClient urlServiceClient;

    public static final String FILE_PATH = "cdn/src/main/resources/static/images/";


    public ResponseEntity<byte[]> getImage(String cdnUrl) throws IOException {
        String fileLocation = checkFileExist(cdnUrl);

        byte[] imageBytes = getByteImage(fileLocation);

        // 파일의 MIME 타입을 동적으로 추출
        String imageType = getImageType(fileLocation);

        // 응답 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(imageType));

        // 이미지 데이터를 ResponseEntity로 반환
        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);

    }

    private byte[] getByteImage(String fileLocation) throws IOException {
        Path imagePath = Paths.get(fileLocation);
        return Files.readAllBytes(imagePath);
    }

    private String getImageType(String fileLocation) throws IOException {
        Path imagePath = Paths.get(fileLocation);
        return Files.probeContentType(imagePath);
    }

    // @Cacheable(cacheNames = "fileLocationCache", key = "args[0]")
    // 같은 클래스에서 한 메서드가 다른 메서드 호출할 때 캐시 적용 안된다고 함
    public String checkFileExist(String cdnUrl) throws IOException {
        String fileLocation = redisService.getValue(cdnUrl);
        if (fileLocation == null) {
            ImageDto imageDto = urlServiceClient.fetchImage(URLEncoder.encode(cdnUrl, StandardCharsets.UTF_8));
            fileLocation = saveImageInCdn(imageDto.getImageStream(), imageDto.getFileName());
            redisService.setValue(cdnUrl, fileLocation);
        }
        return fileLocation;
    }

    // 이미지 저장
    private String saveImageInCdn(InputStream imageStream, String fileName) throws IOException {
        // 저장 경로 생성
        Path uploadPath = Paths.get(FILE_PATH);

        // 파일 경로
        Path filePath = uploadPath.resolve(fileName);

        // InputStream을 파일로 복사하여 저장
        Files.copy(imageStream, filePath);

        return filePath.toString();
    }

}
