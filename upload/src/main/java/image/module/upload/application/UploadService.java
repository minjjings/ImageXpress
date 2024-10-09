package image.module.upload.application;

import image.module.upload.util.FileUtil;
import image.module.upload.domain.ImageExtension;
import image.module.upload.infrastructure.DataService;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class UploadService {
    private final MinioClient minioClient;
    private final DataService dataService;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${minio.bucket}")
    private String bucketName;

    @Value("${cdn-server.url}")
    private String cdnBaseUrl;

    //이미지 데이터 db 저장
    @Transactional
    public ImageResponse saveImageMetadata(MultipartFile file) throws Exception {
        // 업로드 파일명을 불러옴
        String originalName = file.getOriginalFilename();

        // 파일 이름이 존재하지 않는 경우
        if(originalName == null) throw new IllegalArgumentException("잘못된 파일입니다.");

        // test_image.jpg > [0] -> test_image, [1] -> jpg
        String[] fileInfos = FileUtil.splitFileName(file.getOriginalFilename());

        // ImageExtension Enum 에 설정한 확장자가 아닌 경우는 업로드 하지 않음
        ImageExtension extension = ImageExtension.findByKey(fileInfos[1])
                .orElseThrow(() -> new Exception("지원하지 않는 확장자 입니다."));

        int imageWidth = 0;
        int imageHeight = 0;
        try {
            BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
            imageWidth = bufferedImage.getWidth();
            imageHeight = bufferedImage.getHeight();
        } catch (IOException e) {
            log.error("IOException 발생: 이미지 입력 스트림을 읽을 수 없습니다.", e);
        }

        ImageRequest imageRequest = ImageRequest.create(
                originalName,
                extension.getKey(),
                cdnBaseUrl,
                imageWidth,
                imageHeight
        );
        return dataService.uploadImage(imageRequest);
    }

    //이미지 업로드
    @Async
    public void uploadImage(MultipartFile file, ImageResponse image) {
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(image.getStoredFileName())
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
        } catch (IOException e){
            log.error("IOException 발생: 이미지 입력 스트림을 읽을 수 없습니다.", e);
        }
        catch (Exception e) {
            log.error("Exception 발생: ", e);
        }

        //kafkaTemplate.send("image-upload-topic", image.getStoredFileName());
    }
}
