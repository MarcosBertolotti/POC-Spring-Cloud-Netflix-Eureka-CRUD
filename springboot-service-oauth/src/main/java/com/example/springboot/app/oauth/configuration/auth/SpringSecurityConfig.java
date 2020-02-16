package com.example.springboot.app.oauth.configuration.auth;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService; // para poder obtener el usuario por el username y autenticarnos

    @Autowired
    private AuthenticationEventPublisher eventPublisher;

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    @Autowired
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {  // inyectamos el parametro authenticationManagerBuilder

        auth.userDetailsService(this.userDetailsService)     // registramos el userDetailService
                .passwordEncoder(passwordEncoder())          // encripta la contraseña automaticamente cuando el usuario inicia sesion y lo compara con la password que esta en la bd
                .and().authenticationEventPublisher(eventPublisher);
    }

    @Override
    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {     // registramos como componente de spring para luego poder utilizarlo en la configuracion del servidor de autorizacion de oauth2

        return super.authenticationManager();
    }
}
