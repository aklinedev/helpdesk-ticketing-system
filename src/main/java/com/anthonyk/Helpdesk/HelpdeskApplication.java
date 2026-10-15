package com.anthonyk.Helpdesk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Helpdesk API", version = "1.0", description = "REST API for creating and managing help desk tickets, including status lifecycle rules and closed-ticket immutability."))
public class HelpdeskApplication {
	public static void main(String[] args) {
		SpringApplication.run(HelpdeskApplication.class, args);
	}

}
