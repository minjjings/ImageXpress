package image.module.upload;

import image.module.upload.domain.Image;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UploadService {
    private final MinioClient minioClient;
    private final ImageRepository imageRepository;

    @Value("${minio.bucket}")
    private String bucketName;

    //이미지 데이터 db 저장
    public Image saveImageMetadata(MultipartFile file){
        Image image = Image.create(file.getOriginalFilename(),
                UUID.randomUUID() + "_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")),
                null,
                file.getContentType(),
                null,
                null
                );
        return imageRepository.save(image);
    }

    //이미지 업로드
    @Async
    public void uploadImage(MultipartFile file, Image image)
            throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(image.getStoredFileName())
                        .stream(file.getInputStream(), file.getSize(), -1)
                        .contentType(file.getContentType())
                        .build()
        );
    }
}
