package image.module.convert.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;

@Component
public class ImageUtil {

    public BufferedImage convertToRGB(BufferedImage img) {
        // RGB 색상 공간으로 변환
        BufferedImage newImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
        newImg.getGraphics().drawImage(img, 0, 0, null);
        return newImg;
    }

    public byte[] compressImage(BufferedImage image, float quality) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
            if (!writers.hasNext()) throw new IllegalStateException("No writers found");

            ImageWriter writer = writers.next();
            try (ImageOutputStream ios = ImageIO.createImageOutputStream(baos)) {
                writer.setOutput(ios);
                ImageWriteParam param = writer.getDefaultWriteParam();

                if (param.canWriteCompressed()) {
                    param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                    param.setCompressionQuality(quality); // 품질 설정 (0.0f ~ 1.0f)
                }

                // 메타데이터 없이 이미지 작성
                writer.write(null, new javax.imageio.IIOImage(image, null, null), param);
            }
            writer.dispose();
            return baos.toByteArray();
        }
    }

    // 다양한 이미지 형식의 매직넘버로 확인
    public String getImageFormat(MultipartFile file) throws IOException {
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
