package com.example.springboot.app.oauth.configuration.auth;

import com.example.springboot.app.oauth.entity.User;
import com.example.springboot.app.oauth.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class InfoAditionalToken implements TokenEnhancer { // para potenciar nuestro token y agregar mas informacion (claims)

    @Autowired
    private IUserService userService;

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken oAuth2AccessToken, OAuth2Authentication oAuth2Authentication) { // authentication contiene al usuario

        Map<String, Object> info = new HashMap<>();

        User user = userService.findByUsername(oAuth2Authentication.getName());

        info.put("firstName", user.getFirstName());
        info.put("lastName", user.getLastName());
        info.put("email", user.getEmail());

        ((DefaultOAuth2AccessToken) oAuth2AccessToken).setAdditionalInformation(info);

        return oAuth2AccessToken;
    }


}
