package image.module.convert.config;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@EnableKafka
@Configuration
public class KafkaConfig {

    @Bean
    public AdminClient adminClient() {
        // Kafka AdminClient 생성
        return AdminClient.create(Collections.singletonMap("bootstrap.servers", "localhost:29092"));
    }

    @Bean
    public NewTopic imageUploadTopic() {
        // 새로운 토픽 생성
        return new NewTopic("image-upload-topic", 1, (short) 1);
    }

    @Bean
    public NewTopic imageResizeUploadTopic() {
        // 새로운 토픽 생성
        return new NewTopic("imate-resize-upload-topic", 1, (short) 1);
    }
}
