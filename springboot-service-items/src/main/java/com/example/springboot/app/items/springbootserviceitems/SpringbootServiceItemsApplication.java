package com.example.springboot.app.items.springbootserviceitems;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableCircuitBreaker // habilitamos Hystrix
@EnableEurekaClient // no es necesario la dependencia ya lo habilita
//@RibbonClient(name = "products-service") // (no es necesario si usamos eureka, ya se usa internamente)indicamos nombre del servicio con el que nos queremos conectar, singular un solo cliente. de forma automatica Feign se integra con Ribbon. por debajo va a consumir los servicios utilizando balanceo de carga mediante Ribbon
@EnableFeignClients // habilita nuestros clientes feigns que tengamos implementados en nuestro proyecto, nos permite inyectar estos clientes en nuestros componentes de spring.
@SpringBootApplication
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class}) // sobreescribimos la anotacion, excluimos la configuracion del datasource para no conectarnos a bd
public class SpringbootServiceItemsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootServiceItemsApplication.class, args);
	}

}
