package <%= application.package %>;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class <%= nameUpperCamelCase %>Application {

	public static void main(String[] args) {
		SpringApplication.run(<%= nameUpperCamelCase %>Application.class, args);
	}

}
