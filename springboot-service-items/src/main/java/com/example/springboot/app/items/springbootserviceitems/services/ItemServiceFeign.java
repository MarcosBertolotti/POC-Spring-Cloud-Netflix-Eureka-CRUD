package com.example.springboot.app.items.springbootserviceitems.services;

import com.example.springboot.app.commons.models.entity.Product;
import com.example.springboot.app.items.springbootserviceitems.clients.ProductClientRest;
import com.example.springboot.app.items.springbootserviceitems.models.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("itemServiceFeign")
@Primary
public class ItemServiceFeign implements IItemService{

    @Autowired
    private ProductClientRest clientFeign;

    @Override
    public List<Item> findAll() {

        return clientFeign.getAll().stream()
                .map(p -> new Item(p, 1))
                .collect(Collectors.toList());
    }

    @Override
    public Item findById(Long id, Integer quantity) {

        return new Item(clientFeign.getById(id), quantity);
    }

    @Override
    public Product save(Product product){

        return clientFeign.create(product);
    }

    @Override
    public Product update(Product product, Long id) {

        return clientFeign.update(product, id);
    }

    @Override
    public Product deleteById(Long id) {

        return clientFeign.deleteById(id);
    }
}
