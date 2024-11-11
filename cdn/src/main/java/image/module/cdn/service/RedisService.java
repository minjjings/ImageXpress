package image.module.cdn.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Base64;
@Service
public class RedisService {

    private final RedisTemplate<String, String> redisTemplate;

    public RedisService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void saveImage(String key, byte[] imageBytes) {
        String encodedImage = Base64.getEncoder().encodeToString(imageBytes);
        redisTemplate.opsForValue().set(key, encodedImage);
    }

    public byte[] getImage(String key) {
        String encodedImage = redisTemplate.opsForValue().get(key);
        return encodedImage != null ? Base64.getDecoder().decode(encodedImage) : null;
    }
}
