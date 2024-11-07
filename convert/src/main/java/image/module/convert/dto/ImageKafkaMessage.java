package image.module.convert.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageKafkaMessage {
    private String uploadImageName;
    private EcommerceImageSize ecommerceImageSize;

    public static ImageKafkaMessage fromEcommerceImageSize(String file, EcommerceImageSize ecommerceImageSize) {
        return ImageKafkaMessage.builder()
                .uploadImageName(file)
                .ecommerceImageSize(ecommerceImageSize) // enum 값을 설정합니다.
                .build();
    }
}
