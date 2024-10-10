package image.module.url;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class UrlApplication {

	public static void main(String[] args) {
		SpringApplication.run(UrlApplication.class, args);
	}

}
