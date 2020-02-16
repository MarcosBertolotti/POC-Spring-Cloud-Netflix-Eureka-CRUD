package com.example.springboot.app.oauth.services;

import brave.Tracer;
import com.example.springboot.app.oauth.clients.UserFeignClient;
import com.example.springboot.app.oauth.entity.User;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements IUserService, UserDetailsService { // interfaz propia de spring security que se encarga de autentincar, de obtener al usuario por el username

    private Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserFeignClient client;

    @Autowired
    private Tracer tracer;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException { // obtenemos el usuario por el username utilizando un client http con feign

        try{
            User user = this.findByUsername(username); // se comunica con el micro servicios usuarios mediante balanceo de carga

            List<GrantedAuthority> authorities = user.getRoles()
                    .stream()
                    .map(role -> new SimpleGrantedAuthority(role.getName()))
                    .peek(authority -> log.info("Role: " + authority.getAuthority()))   // mostramos por cada authority el nombre del rol del usuario autenticado
                    .collect(Collectors.toList());

            log.info("authenticated User: " + username);

            return new org.springframework.security.core.userdetails
                    .User(user.getUsername(), user.getPassword(), user.getEnabled(),
                    true, true, true, authorities);

        }catch(FeignException e){
            String error = "Loggin Error, User '" + username + "' doesn't exists";
            log.error(error);

            tracer.currentSpan().tag("error.message", error + ": " + e.getMessage());
            throw new UsernameNotFoundException(error);
        }
    }

    @Override
    public User findByUsername(String username) {

        return client.findByUsername(username);
    }

    @Override
    public User update(User user, Long id) {

        return client.update(user, id);
    }
}
