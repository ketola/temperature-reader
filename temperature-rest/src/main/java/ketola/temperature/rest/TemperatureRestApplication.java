package ketola.temperature.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@Configuration
public class TemperatureRestApplication {

	public static void main(String[] args) {
		SpringApplication.run(TemperatureRestApplication.class, args);
	}
}
