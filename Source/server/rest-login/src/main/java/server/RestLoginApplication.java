package server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
// @ImportResource("ApplicationContext.xml")
public class RestLoginApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestLoginApplication.class, args);
	}

}
