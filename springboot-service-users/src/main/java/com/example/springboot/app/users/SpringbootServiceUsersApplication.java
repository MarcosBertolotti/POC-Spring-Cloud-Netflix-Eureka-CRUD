package com.example.springboot.app.users;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@EntityScan({"com.example.springboot.app.users.commons.entity"})// importamos el package donde se encuentra nuestras user entity commons
@SpringBootApplication
public class SpringbootServiceUsersApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootServiceUsersApplication.class, args);
	}

}
