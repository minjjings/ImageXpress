package image.module.convert.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration
public class RedisConfig {

    private final RedisConnectionFactory redisConnectionFactory;

    public RedisConfig(RedisConnectionFactory redisConnectionFactory) {
        this.redisConnectionFactory = redisConnectionFactory;
    }

    @Bean
    public RedisTemplate<String, byte[]> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, byte[]> template = new RedisTemplate<>();

        template.setConnectionFactory(redisConnectionFactory);

        // 키와 값의 시리얼라이저 설정
        template.setKeySerializer(RedisSerializer.string()); // 키는 문자열로
        template.setValueSerializer(RedisSerializer.byteArray()); // 값은 바이트 배열로

        // 트랜잭션 지원 비활성화
        template.setEnableTransactionSupport(false);

        return template;
    }
}
