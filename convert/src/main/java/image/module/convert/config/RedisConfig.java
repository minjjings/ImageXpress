package image.module.convert.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration
//@Profile("convert")
public class RedisConfig {

    @Bean
    public RedisConnectionFactory redisConnectionFactoryOne() {
        return new LettuceConnectionFactory(new RedisStandaloneConfiguration("localhost", 6379));
    }

    @Bean
    public RedisTemplate<String, byte[]> redisTemplate(RedisConnectionFactory redisConnectionFactoryOne) {
        RedisTemplate<String, byte[]> template = new RedisTemplate<>();

        // 설정한 RedisConnectionFactory를 사용하여 RedisTemplate 설정
        template.setConnectionFactory(redisConnectionFactoryOne);

        // 키와 값의 시리얼라이저 설정
        template.setKeySerializer(RedisSerializer.string()); // 키는 문자열로
        template.setValueSerializer(RedisSerializer.byteArray()); // 값은 바이트 배열로

        // 트랜잭션 지원 비활성화
        template.setEnableTransactionSupport(false);

        return template;
    }
}
