package image.module.upload.application;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import javax.imageio.ImageIO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageRequest {

    private UUID id;
    private String originalFileName;
    private String storedFileName;
    private String cdnUrl;
    private String fileType;
    private Integer width;
    private Integer height;

    public static ImageRequest create(MultipartFile file, String cdnBaseUrl) {
        // 1. UUID 생성
        String uuid = UUID.randomUUID().toString();

        // 2. 현재 시간 포맷팅
        String formattedTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

        // 3. 저장할 파일 이름 생성 (이미지 이름 치환하기)
        String storedFileName = uuid + "_" + formattedTime;

        // 4. 파일 확장자 가져오기
        String fileExtension = getFileExtension(file.getOriginalFilename());

        // 5. cdn url 생성
        String cdnUrl = cdnBaseUrl + "/" + UUID.randomUUID() + "." + fileExtension;

        // 6. width, height 구하기
        int imageWidth = 0;
        int imageHeight = 0;
        try {
            BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
            imageWidth = bufferedImage.getWidth();
            imageHeight = bufferedImage.getHeight();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ImageRequest.builder()
                .originalFileName(file.getOriginalFilename())
                .storedFileName(storedFileName)
                .cdnUrl(cdnUrl)
                .fileType(fileExtension)
                .width(imageWidth)
                .height(imageHeight)
                .build();
    }

    public static String getFileExtension(String fileName) {
        if (fileName == null || fileName.lastIndexOf(".") == -1) {
            return ""; // 확장자가 없으면 빈 문자열 반환
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1); // 확장자만 반환
    }
}
