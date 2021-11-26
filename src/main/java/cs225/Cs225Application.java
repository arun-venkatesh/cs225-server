package cs225;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import api.Handler;

@SpringBootApplication
@ComponentScan(basePackageClasses = Handler.class)
public class Cs225Application {

	public static void main(String[] args) {
		SpringApplication.run(Cs225Application.class, args);
	}

}
