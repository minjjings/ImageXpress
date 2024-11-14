package image.module.convert;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


@EnableFeignClients
@SpringBootApplication
public class ConvertApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConvertApplication.class, args);
	}

}
