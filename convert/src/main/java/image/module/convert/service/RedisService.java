package image.module.convert.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {

    private final RedisTemplate<String, byte[]> redisTemplate;

    @Autowired
    public RedisService(RedisTemplate<String, byte[]> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void saveImage(String originalFilename, byte[] imageBytes) {
        // 원본 파일 이름을 키로 사용하고, 이미지 바이트 배열을 값으로 저장
        redisTemplate.opsForValue().set(originalFilename, imageBytes);
        // 필요에 따라 TTL(Time to Live)을 설정할 수 있습니다.
        // redisTemplate.opsForValue().set(originalFilename, imageBytes, 1, TimeUnit.HOURS);
    }

    public boolean imageExists(String uploadFileName) {
        // Redis에서 해당 키의 값을 가져옴
        byte[] imageBytes = redisTemplate.opsForValue().get(uploadFileName);

        // 이미지가 존재하면 true, 존재하지 않으면 false 반환
        return imageBytes != null;
    }

    public byte[] getImage(String uploadFileName) {
        // Redis에서 해당 키의 값을 가져옴
        return redisTemplate.opsForValue().get(uploadFileName);
    }

}
