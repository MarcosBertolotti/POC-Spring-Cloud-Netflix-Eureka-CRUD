package com.example.springboot.zuul.configuration.oauth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@RefreshScope // para actualizar la configuracion, ej. si cambiamos algun parametro en el servidor de recurso, en el config y la aplicacion esta levantada, podemos refrescar sin reiniciar el servicio
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Value("${config.security.oauth.jwt.key}")
    private String jwtKey;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {  // para configurar el token con la misma estructura que el AuthorizationServer

        resources.tokenStore(tokenStore()); // configuramos el token store
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {  // para proteger nuestras rutas, los endpoints

        http.authorizeRequests()
                .antMatchers("/api/security/oauth/**").permitAll()  // para que cualquier usuario pueda inicar sesion
                .antMatchers(HttpMethod.GET, "/api/products/list", "/api/items/list", "/api/users/users").permitAll()
                .antMatchers(HttpMethod.GET, "/api/products/see/{id}", "api/items/see/{id}/quantity/{id}",
                         "/api/users/users/{id}").hasAnyRole("ADMIN", "USER")
                .antMatchers("/api/products/**", "/api/items/**", "/api/users/**").hasRole("ADMIN")
                .anyRequest().authenticated()  // para cualquier ruta que no fue especificada
                .and().cors().configurationSource(corsConfigurationSource());
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration corsConfig = new CorsConfiguration();

        corsConfig.setAllowedOrigins(Arrays.asList("*")); // configuramos nuestras aplicaciones clientes, los origenes, la ubicacion, el nombre del dominio, el puerto
        corsConfig.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "DELETE", "OPTIONS"));
        corsConfig.setAllowCredentials(true);
        corsConfig.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();     // pasar la configuracion de nuestro corConfig a nuestras rutas Url (endpoints)
        source.registerCorsConfiguration("/**", corsConfig); // aplicamos a todas las rutas

        return source;
    }

    // para registrar un filtro de Cors, para que no solo quede configurado en spring security, si no, que quede configurado a nivel global en un filtro de spring a toda nuestra aplicacion
    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter() {    // filtro para que se aplique a nivel global

        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<CorsFilter>(new CorsFilter(corsConfigurationSource()));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE); // prioridad alta

        return bean;    // bean encargado de registrar el CorsFilter
    }

    // debemos tener los mismos bean que en AuthorizationServerConfig con la misma clave secreta
    @Bean
    public JwtTokenStore tokenStore() {

        return new JwtTokenStore(accessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter(){

        JwtAccessTokenConverter tokenConverter = new JwtAccessTokenConverter();     // se encarga de guardar los datos del usuario en el token,

        tokenConverter.setSigningKey(jwtKey);      // agregamos un codigo secreto unico

        return tokenConverter;
    }
}
