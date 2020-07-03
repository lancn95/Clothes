package com.example.shopping.service;

import java.util.List;

import com.example.shopping.entities.Product;

public interface ProductService {
	List<Product> findAll();
	
	void save(Product product);
	
	void update(Product product);
	
	void delete(Product product);
	
	List<Product> seachByNameLike(String name);
}
