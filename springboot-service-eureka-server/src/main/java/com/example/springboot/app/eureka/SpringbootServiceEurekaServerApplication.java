package com.example.springboot.app.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer // habilitamos eureka server
@SpringBootApplication
public class SpringbootServiceEurekaServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootServiceEurekaServerApplication.class, args);
	}

}
