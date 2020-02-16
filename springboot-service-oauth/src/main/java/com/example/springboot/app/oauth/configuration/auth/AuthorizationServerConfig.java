package com.example.springboot.app.oauth.configuration.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.util.Arrays;

@RefreshScope
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private Environment env;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private InfoAditionalToken infoAditionalToken;

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {    // es el permiso que va a tener nuestros endpoints del servidor de authorizacion de oauth2 para generar el token y tambien para validar el token

        security.tokenKeyAccess("permitAll()")   // tokenKeyAcess es el endpoint para generar el token, para autenticarnos con la ruta /oauth/token. Cada vez que nos autenticamos mediante post enviamos las credenciales del usuario y las credenciales del cliente de la aplicacion, valida las credenciales y autentica.
        .checkTokenAccess("isAuthenticated()");  // metodo de spring security que se encarga de validar el token
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception { // registramos nuestros clientes, que van a acceder a nuestros micro servicios

        clients.inMemory() // registramos en memoria
                .withClient(env.getProperty("config.security.oauth.client.id"))                      // registramos un cliente. Credenciales de la aplicacion cliente. clientId, identificador de nuestra aplicacion.
                .secret(passwordEncoder.encode(env.getProperty("config.security.oauth.client.secret")))    // registramos la contraseña encriptada
                .scopes("read", "write")                               // alcance/permiso de la aplicacion
                .authorizedGrantTypes("password", "refresh_token")     // tipo de consecion que va a tener nuestra autenticacion, como vamos a obtener el token, cambiamos las credenciales del usuario por el token. Password cuando es con credenciales, cuando nuestros usuarios existen en nuestro sistema de backend, autenticacion y autorizacion, y para autenticar requiere un username y una contraseñá
                .accessTokenValiditySeconds(3600)                      // tiempo de validez del token antes que caduque.
                .refreshTokenValiditySeconds(3600);                    // tiempo del refresh_token, es un token que nos permite obtener un nuevo token de accesso renobado justo antes de que caduque el token actual.
                // .and().withClient() podemos tener varios clientes, cliente android, react, angular con sus propias credenciales.
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {  // esta configuracion esta relacionada al enpoints de oauth2 que se encarga de generar el token

        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();   // cadena para unir los datos del token
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(infoAditionalToken, accessTokenConverter()));

        endpoints.authenticationManager(authenticationManager)   // registramos el authentication manager
                .tokenStore(tokenStore())                        // configuramos el token store, el componente que se encarga de generar el token con los datos del accessTokenConverter
                .accessTokenConverter(accessTokenConverter())    // configuramos el access token converter para que sea del tipo jwt
                .tokenEnhancer(tokenEnhancerChain);              // agregamos la cadena con la info adicional del token.
    }

    @Bean
    public JwtTokenStore tokenStore() {

        return new JwtTokenStore(accessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter(){

        JwtAccessTokenConverter tokenConverter = new JwtAccessTokenConverter();     // se encarga de guardar los datos del usuario en el token,

        tokenConverter.setSigningKey(env.getProperty("config.security.oauth.jwt.key"));      // agregamos un codigo secreto unico

        return tokenConverter;
    }

}
