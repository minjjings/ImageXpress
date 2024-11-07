package image.module.upload.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    private final RedisTemplate<String, byte[]> redisTemplate;

    @Autowired
    public RedisService(RedisTemplate<String, byte[]> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // 이미지 저장 메서드
    public void saveImage(String imageName, byte[] imageBytes) {
        redisTemplate.opsForValue().set(imageName, imageBytes);
        // 필요시 TTL 설정
        // redisTemplate.expire(imageName, 1, TimeUnit.HOURS); // 예: 1시간 후 만료
    }

    // 이미지 조회 메서드
    public byte[] getImage(String imageName) {
        return redisTemplate.opsForValue().get(imageName);
    }

    // 추가 기능: 이미지 삭제 메서드
    public void deleteImage(String imageName) {
        redisTemplate.delete(imageName);
    }
}
