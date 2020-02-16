package com.example.springboot.app.oauth.clients;

import com.example.springboot.app.oauth.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "users-service")
public interface UserFeignClient {

    @GetMapping("/users/search/search-username")
    User findByUsername(@RequestParam String username); // enviamos el parametro username para consumir el micro servicio

    @PutMapping("/users/{id}")
    User update(@RequestBody User user, @PathVariable Long id);
}
