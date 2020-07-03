package com.example.shopping.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.shopping.dao.ProductDAO;
import com.example.shopping.entities.Product;
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

	@Override
	public void save(Product product) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(Product product) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(Product product) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Product> seachByNameLike(String name) {
		// TODO Auto-generated method stub
		return null;
	}

}
