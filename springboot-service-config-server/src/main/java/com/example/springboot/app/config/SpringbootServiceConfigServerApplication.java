package com.example.springboot.app.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@EnableConfigServer    // habilitamos como servidor de configuracion
@SpringBootApplication
public class SpringbootServiceConfigServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootServiceConfigServerApplication.class, args);
	}

}
