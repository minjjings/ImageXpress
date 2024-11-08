package image.module.upload;

import image.module.upload.service.RedisService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class RedisServiceTest {

    @Autowired
    private RedisService redisService;

    @Test
    public void testGetImage() {
        // 이미지 이름
        String imageName = "test.jpg";

        // Redis에서 이미지 조회
        String base64Image = Arrays.toString(redisService.getImage(imageName));

        // Base64 인코딩된 이미지가 조회되었는지 확인
        assertThat(base64Image).isNotNull();
        System.out.println("Retrieved image (Base64): " + base64Image);
    }


}
