package com.example.springboot.app.products.services;

import java.util.List;

import com.example.springboot.app.products.repository.IProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import com.example.springboot.app.commons.models.entity.Product;

@Service
public class ProductService implements IProductService{

	private static final String PRODUCT_NOT_FOUND = "Product with id: '%s' not found";
	
	@Autowired
	private IProductRepository productRepository;
	
	@Override
	@Transactional(readOnly = true)
	public List<Product> findAll() {
		
		return productRepository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Product findById(Long id) {
		
		return productRepository.findById(id)
				.orElseThrow(() -> new HttpClientErrorException(HttpStatus.BAD_REQUEST, String.format(PRODUCT_NOT_FOUND, id)));
	}

	@Override
	@Transactional
	public Product save(Product product) {

		return productRepository.save(product);
	}

	@Override
	public Product update(Product product, Long id) {

		Product p = this.findById(id);

		p.setName(product.getName());
		p.setPrice(product.getPrice());

		return productRepository.save(p);
	}


	@Override
	@Transactional
	public Product deleteById(Long id) {

		Product product = this.findById(id);

		productRepository.deleteById(id);

		return product;
	}


}
