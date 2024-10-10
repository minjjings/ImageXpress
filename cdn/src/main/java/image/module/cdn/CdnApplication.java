package image.module.cdn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class CdnApplication {

    public static void main(String[] args) {
        SpringApplication.run(CdnApplication.class, args);
    }

}
