package app.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CouponSystemPart2Application {
	public static void main(String[] args) {
		SpringApplication.run(CouponSystemPart2Application.class, args);
	}
}
