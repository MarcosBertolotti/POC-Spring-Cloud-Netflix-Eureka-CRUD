package com.example.springboot.app.products.controllers;

import java.util.List;
import java.util.stream.Collectors;

import com.example.springboot.app.commons.models.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springboot.app.products.services.IProductService;

@RestController
@RequestMapping("")
public class ProductController {

	@Autowired
	private Environment env;

	@Value("${server.port}")
	private Integer port;

	@Autowired
	private IProductService productService;
	
	@GetMapping("/list")
	public List<Product> getAll(){

		return productService.findAll().stream()
				.map(p -> {
					p.setPort(port);
					return p;
				})
				.collect(Collectors.toList());
	}
	
	@GetMapping("/see/{id}")
	public Product getById(@PathVariable Long id) throws Exception{

		Product product = productService.findById(id);
		product.setPort(port);
		// product.setPort(Integer.parseInt(env.getProperty("local.server.port")));

		return product;
	}

	@PostMapping("/create")
	//@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<Product> create(@RequestBody Product product){

		Product newProduct = productService.save(product);

		return ResponseEntity.status(HttpStatus.CREATED).body(newProduct);
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<Product> update(@RequestBody Product product, @PathVariable Long id) {

		Product updatedProduct = productService.update(product, id);

		return ResponseEntity.status(HttpStatus.CREATED).body(updatedProduct);
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Product> deleteById(@PathVariable Long id) {

		Product deletedProduct = productService.deleteById(id);

		return ResponseEntity.ok(deletedProduct);
	}



}
