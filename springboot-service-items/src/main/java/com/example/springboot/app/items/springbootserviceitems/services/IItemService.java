package com.example.springboot.app.items.springbootserviceitems.services;

import com.example.springboot.app.commons.models.entity.Product;
import com.example.springboot.app.items.springbootserviceitems.models.Item;

import java.util.List;

public interface IItemService {

    List<Item> findAll();

    Item findById(Long id, Integer quantity);

    Product save(Product product);

    Product update(Product product, Long id);

    Product deleteById(Long id);
}
