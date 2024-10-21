package image.module.convert.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SendKafkaMessage {
    private String WebPFileName;
    private Integer Size;

    public static SendKafkaMessage createMessage(String fileName, int size){
        return SendKafkaMessage.builder()
                .WebPFileName(fileName)
                .Size(size)
                .build();
    }
}
