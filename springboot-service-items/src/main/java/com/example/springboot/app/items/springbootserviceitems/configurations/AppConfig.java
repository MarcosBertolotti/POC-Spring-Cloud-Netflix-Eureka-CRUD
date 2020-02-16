package com.example.springboot.app.items.springbootserviceitems.configurations;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Bean("clientRest")
    @LoadBalanced // de forma automatica va a utilzar ribbon para el balanceo de carga, y rest template por debajo utilizando el balanceador buscar la mejor instancia disponible
    public RestTemplate registerRestTemplate() { // cliente http para trabajar con api rest, para poder acceder a recursos que estan en otros microservicios

        return new RestTemplate();
    }
}
