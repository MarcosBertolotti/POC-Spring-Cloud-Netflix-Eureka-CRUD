package com.example.springboot.app.products.services;

import com.example.springboot.app.commons.models.entity.Product;

import java.util.List;

public interface IProductService {

	List<Product> findAll();
	
	Product findById(Long id);

	Product save(Product product);

	Product update(Product product, Long id);

	Product deleteById(Long id);
}
