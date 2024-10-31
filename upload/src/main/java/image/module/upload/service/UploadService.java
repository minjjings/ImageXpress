package image.module.upload.service;

import image.module.upload.dto.ImageRequest;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;

@Slf4j
@Service
@RequiredArgsConstructor
public class UploadService {
    private final MinioClient minioClient;

    @Value("${minio.buckets.uploadBucket}")
    private String uploadBucket; // 업로드 버킷 이름

    public ResponseEntity<ImageRequest> uploadImage(MultipartFile image, String originalFileName) throws IOException {
        String imageFormat = getImageFormat(image);
        log.info("Image format: " + imageFormat);
        if (imageFormat.equals("Unknown")) {
            return ResponseEntity.badRequest().body(new ImageRequest(originalFileName));
        }

        // MultipartFile을 BufferedImage로 변환
        BufferedImage bufferedImage = ImageIO.read(image.getInputStream());

        // 색상 공간 변환
        bufferedImage = convertToRGB(bufferedImage);

        try {
            // JPEG로 압축
            String uploadFileName = originalFileName.replaceFirst("[.][^.]+$", "") + ".jpg"; // .jpg로 변경

            // JPEG 이미지로 압축 및 메타데이터 제거
            byte[] compressedImageBytes = compressImage(bufferedImage, 0.50f); // 품질 75%

            // MinIO에 업로드
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(uploadBucket)
                            .object(uploadFileName) // .jpg로 변경된 파일 이름
                            .stream(new ByteArrayInputStream(compressedImageBytes), compressedImageBytes.length, -1)
                            .contentType("image/jpeg")
                            .build()
            );

            return ResponseEntity.ok(new ImageRequest("이미지 업로드 성공"));
        } catch (IOException e) {
            log.error("파일 업로드 중 오류 발생: {}", e.getMessage());
            return ResponseEntity.status(500).body(new ImageRequest("서비스 파일 업로드 중 오류 발생: " + e.getMessage()));
        } catch (Exception e) {
            log.error("이미지 업로드 실패: {}", e.getMessage());
            return ResponseEntity.status(500).body(new ImageRequest("이미지 업로드 실패: " + e.getMessage()));
        }
    }

    private BufferedImage convertToRGB(BufferedImage img) {
        // RGB 색상 공간으로 변환
        BufferedImage newImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
        newImg.getGraphics().drawImage(img, 0, 0, null);
        return newImg;
    }


    private byte[] compressImage(BufferedImage image, float quality) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
        ImageWriter writer = writers.next();
        ImageOutputStream ios = ImageIO.createImageOutputStream(baos);
        writer.setOutput(ios);

        ImageWriteParam param = writer.getDefaultWriteParam();
        if (param.canWriteCompressed()) {
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(quality); // 품질 설정 (0.0f ~ 1.0f)
        }

        // 메타데이터 없이 이미지 작성
        writer.write(null, new javax.imageio.IIOImage(image, null, null), param);
        writer.dispose();
        return baos.toByteArray();
    }

    // 다양한 이미지 형식의 매직넘버로 확인
    private String getImageFormat(MultipartFile file) throws IOException {
        byte[] fileBytes = new byte[8]; // 여러 바이트 읽기
        file.getInputStream().read(fileBytes); // 파일의 첫 8 바이트 읽기

        // JPEG
        if (fileBytes[0] == (byte) 0xFF && fileBytes[1] == (byte) 0xD8) {
            return "JPEG";
        }

        // PNG
        if (fileBytes[0] == (byte) 0x89 && fileBytes[1] == (byte) 0x50 &&
                fileBytes[2] == (byte) 0x4E && fileBytes[3] == (byte) 0x47) {
            return "PNG";
        }

        // GIF
        if (fileBytes[0] == (byte) 0x47 && fileBytes[1] == (byte) 0x49 &&
                fileBytes[2] == (byte) 0x46 && (fileBytes[3] == (byte) 0x38)) {
            return "GIF";
        }

        // BMP
        if (fileBytes[0] == (byte) 0x42 && fileBytes[1] == (byte) 0x4D) {
            return "BMP";
        }

        // TIFF
        if ((fileBytes[0] == (byte) 0x49 && fileBytes[1] == (byte) 0x49 &&
                fileBytes[2] == (byte) 0x2A && fileBytes[3] == (byte) 0x00) ||
                (fileBytes[0] == (byte) 0x4D && fileBytes[1] == (byte) 0x4D &&
                        fileBytes[2] == (byte) 0x00 && fileBytes[3] == (byte) 0x2A)) {
            return "TIFF";
        }

        return "Unknown";
    }
}
