package ru.hogwarts.school.third;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <code>@OpenAPIDefinition</code> - Swagger UI
 */
@SpringBootApplication
@OpenAPIDefinition
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
