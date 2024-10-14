package image.module.url.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;



@Service
@RequiredArgsConstructor
public class UrlService {

//    private final ImageConverter imageConverter;
//
//    // WebP와 JPEG 파일 경로를 상수로 선언합니다.
//    private static final String WEBP_FILE_PATH = "C:/spring/project/Image-Module/url/src/main/resources/images/test.webp";
//    private static final String JPEG_FILE_PATH = "C:/spring/project/Image-Module/url/src/main/resources/images/test.jpeg";
//
//    // 변환 메서드
//    public void convertWebPToJpeg() {
//        File webpFile = new File(WEBP_FILE_PATH);
//        File jpegFile = new File(JPEG_FILE_PATH);
//
//        try (FileOutputStream outputStream = new FileOutputStream(jpegFile)) {
//            imageConverter.convertWebPToJpeg(webpFile, outputStream);
//            System.out.println("변환 완료: " + jpegFile.getAbsolutePath());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
