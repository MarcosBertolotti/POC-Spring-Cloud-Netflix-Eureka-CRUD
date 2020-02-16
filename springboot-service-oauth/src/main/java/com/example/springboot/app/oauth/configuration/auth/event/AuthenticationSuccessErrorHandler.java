package com.example.springboot.app.oauth.configuration.auth.event;

import brave.Tracer;
import com.example.springboot.app.oauth.entity.User;
import com.example.springboot.app.oauth.services.IUserService;
import com.netflix.discovery.converters.Auto;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationSuccessErrorHandler implements AuthenticationEventPublisher {

    @Autowired
    private IUserService userService;

    @Autowired
    private Tracer tracer;

    private Logger log = LoggerFactory.getLogger(AuthenticationSuccessErrorHandler.class);

    @Override
    public void publishAuthenticationSuccess(Authentication authentication) {   // usuario ya autenticado

        UserDetails user = (UserDetails) authentication.getPrincipal();

        String message = "Success Login: " + user.getUsername();

        System.out.println(message);
        log.info(message);

        User userLogged = userService.findByUsername(authentication.getName());

        if(userLogged.getAttempts() != null && userLogged.getAttempts() > 0) {
            userLogged.setAttempts(0);
            userService.update(userLogged, userLogged.getId());
        }
    }

    @Override
    public void publishAuthenticationFailure(AuthenticationException e, Authentication authentication) {    // usuario que esta tratando de autenticarse

        String message = "Login Error: " + e.getMessage();

        System.out.println(message);
        log.error(message);

        try {
            StringBuilder errors = new StringBuilder();
            errors.append(message);

            User user = userService.findByUsername(authentication.getName());

            if(user.getAttempts() == null){
                System.out.println("ASDASDASDASDASDASD");
                user.setAttempts(0);
            }

            log.info("Current attempt: " + user.getAttempts());

            user.setAttempts(user.getAttempts() + 1);

            log.info("Later attempt: " + user.getAttempts());

            errors.append(" - Login attempts: " + user.getAttempts());

            if(user.getAttempts() >= 3){
                String errorMaxAttempts = String.format("User %s disabled for maximum attempts", user.getUsername());
                log.error(errorMaxAttempts);
                errors.append(" - " + errorMaxAttempts);
                user.setEnabled(false);
            }

            userService.update(user, user.getId());

            tracer.currentSpan().tag("error.message", errors.toString());

        } catch(FeignException exception) {
            log.error(String.format("User %s doesn't exists", authentication.getName()));
        }

    }
}
