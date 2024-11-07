package image.module.convert.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    private final StringRedisTemplate stringRedisTemplate;

    @Autowired
    public RedisService(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public void saveImage(String originalFilename, byte[] imageBytes) {
        // 원본 파일 이름을 키로 사용하고, 이미지 바이트 배열을 값으로 저장
        stringRedisTemplate.opsForValue().set(originalFilename, Base64.getEncoder().encodeToString(imageBytes));
        // 필요에 따라 TTL(Time to Live)을 설정할 수 있습니다.
        // stringRedisTemplate.opsForValue().set(originalFilename, imageBytes, 1, TimeUnit.HOURS);
    }

    public byte[] getImage(String originalFilename) {
        String imageBase64 = stringRedisTemplate.opsForValue().get(originalFilename);
        return imageBase64 != null ? Base64.getDecoder().decode(imageBase64) : null;
    }
}
