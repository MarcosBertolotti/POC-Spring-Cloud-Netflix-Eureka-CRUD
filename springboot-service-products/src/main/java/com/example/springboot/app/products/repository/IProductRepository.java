package com.example.springboot.app.products.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.springboot.app.commons.models.entity.Product;

@Repository
public interface IProductRepository extends CrudRepository<Product, Long>{
	
	List<Product> findAll();
}
