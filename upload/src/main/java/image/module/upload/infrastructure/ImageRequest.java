package image.module.upload.infrastructure;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import javax.imageio.ImageIO;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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

    public static ImageRequest create(MultipartFile file) {
        // 1. UUID 생성
        String uuid = UUID.randomUUID().toString();

        // 2. 현재 시간 포맷팅
        String formattedTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

        // 3. 저장할 파일 이름 생성 (이미지 이름 치환하기)
        String storedFileName = uuid + "_" + formattedTime;

        // 4. width, height 구하기
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
                .cdnUrl("test")//TODO: cdn_url 설정
                .fileType(file.getContentType())
                .width(imageWidth)
                .height(imageHeight)
                .build();
    }
}
