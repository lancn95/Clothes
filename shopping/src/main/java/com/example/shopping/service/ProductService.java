package com.example.shopping.service;

import java.util.List;

import com.example.shopping.entities.Product;
import com.example.shopping.form.ProductForm;

public interface ProductService {
	List<Product> findAll();
	
	Product findProduct(String code);
	
	void save(ProductForm productForm);
	
	void update(ProductForm productForm);
	
	void delete(ProductForm productForm);
	
	List<Product> seachByNameLike(String name);
}
