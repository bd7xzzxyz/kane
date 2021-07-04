package xyz.bd7xzz.kane;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
		scanBasePackages = "xyz.bd7xzz.kane"
)
public class KaneApplication {

	public static void main(String[] args) {
		SpringApplication.run(KaneApplication.class, args);
	}

}
