package com.example.springboot.app.commons;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class}) // sobreescribimos la anotacion, excluimos la configuracion del datasource para no conectarnos a bd
public class SpringbootServiceCommonsApplication {

	// quitamos metodo main, es un proyecto de libreria, es una dependencia que no vamos a ejecutar
}
