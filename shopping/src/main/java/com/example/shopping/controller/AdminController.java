package com.example.shopping.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.shopping.dao.ProductDAO;
import com.example.shopping.entities.Product;
import com.example.shopping.service.Impl.ProductServiceImpl;

@Controller
@Transactional
public class AdminController {
	
	@Autowired
	private ProductServiceImpl productServiceImpl;
	
	@RequestMapping(value = "/products", method = RequestMethod.GET)
	private String listProduct(Model model) {
		List<Product> products = productServiceImpl.findAll();
		model.addAttribute("products", products);
		
		return "admin/list_product";
	}
	

	
}
