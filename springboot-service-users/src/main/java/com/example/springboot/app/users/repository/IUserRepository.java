package com.example.springboot.app.users.repository;

import com.example.springboot.app.users.entity.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource(path = "users")     // indicamos nuestro endpoint donde se va a exportar todos los metodos del crud repository a nuestra api rest
public interface IUserRepository extends PagingAndSortingRepository<User, Long> {

    @RestResource(path="search-username")   // ruta para el endpoint
    User findByUsername(@Param("username") String username);    // Param indicamos el nombre del parametro para la ruta endpoint
}
