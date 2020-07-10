package com.example.shopping.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.ConditionalConverter;
import org.springframework.stereotype.Service;

import com.example.shopping.convert.ProductToProductInfo;
import com.example.shopping.dao.ProductDAO;
import com.example.shopping.entities.Product;
import com.example.shopping.form.ProductForm;
import com.example.shopping.model.ProductInfo;
import com.example.shopping.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductDAO productDAO;

	@Override
	public List<Product> findAll() {
		List<Product> products = this.productDAO.findAll();
		
		
		
		return products;
	}
	
	public List<ProductInfo> findAllProInfo() {
		List<Product> products = this.productDAO.findAll();
		// ép list product sang list product info
		List<ProductInfo> listProductInfo = ProductToProductInfo.convertProductInfo(products);
		
		return listProductInfo;
	}

	@Override
	public void save(ProductForm productForm) {
		productDAO.saveProduct(productForm);

	}

	@Override
	public void update(ProductForm productForm) {
		productDAO.updateProduct(productForm);
	}

	@Override
	public List<Product> seachByNameLike(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Product findProduct(String code) {
		Product product = productDAO.findProduct(code);
		return product;
	}

	@Override
	public void delete(ProductForm productForm) {
		productDAO.deleteProduct(productForm);

	}


}
