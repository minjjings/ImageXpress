package image.module.cdn.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {

    private final RedisTemplate<String, String> redisTemplate;

    @Autowired
    public RedisService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // 값을 Redis에 저장하는 메서드
    public void setValue(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    // 값을 Redis에서 가져오는 메서드
    public String getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }
}