package com.example.springboot.app.items.springbootserviceitems.services;

import com.example.springboot.app.commons.models.entity.Product;
import com.example.springboot.app.items.springbootserviceitems.models.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("itemServiceRestTemplate")
public class ItemServiceImpl implements IItemService {

    @Autowired
    private RestTemplate clientRest;

    @Override
    public List<Item> findAll() {

        List<Product> products = Arrays.asList(clientRest.getForObject("http://products-service/list", Product[].class));

        return products.stream()
                .map(p -> new Item(p, 1))
                .collect(Collectors.toList());
    }

    @Override
    public Item findById(Long id, Integer quantity) {

        Map<String, String> pathVariables = new HashMap();
        pathVariables.put("id", id.toString());

        Product product = clientRest.getForObject("http://products-service/see/{id}", Product.class, pathVariables);

        return new Item(product, quantity);
    }

    @Override
    public Product save(Product product) {

        HttpEntity<Product> body = new HttpEntity<>(product);

        ResponseEntity<Product> response = clientRest.exchange("http://products-service/create", HttpMethod.POST, body, Product.class); // exchange=intercambiar, 1ro la ruta(endpoint), 2do tipo de la peticion (httpMethod), 3er enviar request/body que contiene el objeto product, 4to el tipo de dato que queremos recibir el json.
        Product productResponse = response.getBody();

        return productResponse;
    }

    @Override
    public Product update(Product product, Long id) {

        HttpEntity<Product> body = new HttpEntity<>(product);

        Map<String, String> pathVariables = new HashMap();
        pathVariables.put("id", id.toString());

        ResponseEntity<Product> response = clientRest.exchange("http://products-service/update/{id}",
                HttpMethod.PUT, body, Product.class, pathVariables);

        return response.getBody();
    }

    @Override
    public Product deleteById(Long id) {

        Map<String, String> pathVariables = new HashMap();
        pathVariables.put("id", id.toString());

        //clientRest.delete("http://products-service/delete/{id}", pathVariables);
        ResponseEntity<Product> response = clientRest.exchange("http://products-service/delete/{id}",
                HttpMethod.DELETE, null, Product.class, pathVariables);

        return response.getBody();
    }
}
