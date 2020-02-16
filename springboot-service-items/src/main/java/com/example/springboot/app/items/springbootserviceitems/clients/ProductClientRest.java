package com.example.springboot.app.items.springbootserviceitems.clients;

import com.example.springboot.app.commons.models.entity.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "products-service") // indicamos el nombre del microservicio al cual queremos conectarnos
public interface ProductClientRest {

    @GetMapping("/list") // indicamos la ruta para consumir el servicio apirest y obtener los datos del json convertidos a nuestros objetos.
    List<Product> getAll();

    @GetMapping("/see/{id}")
    Product getById(@PathVariable Long id);

    @PostMapping("/create")
    Product create(@RequestBody Product product);

    @PutMapping("/update/{id}")
    Product update(@RequestBody Product product, @PathVariable Long id);

    @DeleteMapping("/delete/{id}")
    Product deleteById(@PathVariable Long id);

}
