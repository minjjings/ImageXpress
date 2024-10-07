package image.module.internal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ConsumerService {

    // 이 메서드는 Kafka에서 메시지를 소비하는 리스너 메서드입니다.
    // @KafkaListener 어노테이션은 이 메서드를 Kafka 리스너로 설정합니다.
    @KafkaListener(topics = "image-upload-topic", groupId = "image-upload-group")
    // Kafka 토픽 "test-topic"에서 메시지를 수신하면 이 메서드가 호출됩니다.
    // groupId는 컨슈머 그룹을 지정하여 동일한 그룹에 속한 다른 컨슈머와 메시지를 분배받습니다.
    public void listen(String message) {
        System.out.println("Received Message in group 'image-upload-group': " + message);
        // 메시지 처리 로직 추가
    }
}
