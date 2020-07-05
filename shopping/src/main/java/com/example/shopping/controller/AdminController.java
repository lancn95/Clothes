package com.example.shopping.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.shopping.dao.ProductDAO;
import com.example.shopping.entities.Product;
import com.example.shopping.form.ProductForm;
import com.example.shopping.service.Impl.ProductServiceImpl;

@Controller
@Transactional
public class AdminController {

	@Autowired
	private ProductServiceImpl productServiceImpl;

	@RequestMapping(value = "/admin/products", method = RequestMethod.GET)
	private String listProduct(Model model, Principal principal) {
		String adminname = principal.getName();
		model.addAttribute("name", adminname);

		List<Product> products = productServiceImpl.findAll();
		model.addAttribute("products", products);

		return "admin/list_product";
	}

	@RequestMapping(value = "/admin/product-save", method = RequestMethod.GET)
	private String getInfoProduct(Model model) {
		ProductForm productForm = new ProductForm();
		model.addAttribute("productForm", productForm);
		return "admin/save_product";
	}

	@RequestMapping(value = "/admin/product-save", method = RequestMethod.POST)
	private String createNewProduct(@ModelAttribute ProductForm productForm) {
		productServiceImpl.save(productForm);

		return "redirect:/admin/products";
	}

	@RequestMapping(value = "/admin/product-update", method = RequestMethod.GET)
	private String editProduct(@RequestParam("code") String code, Model model) {
		System.out.println("code: " + code);
		ProductForm productForm = null;

		if (code != null && code.length() > 0) {
			Product product = productServiceImpl.findProduct(code);
			if (product != null) {
				productForm = new ProductForm(product);
			}
		}

		model.addAttribute("productForm", productForm);
		return "admin/update_product";
	}

	@RequestMapping(value = "/admin/product-update", method = RequestMethod.POST)
	private String updateProduct(@ModelAttribute ProductForm productForm) {
		productServiceImpl.update(productForm);

		return "redirect:/admin/products";
	}

	@RequestMapping(value = "/admin/product-delete", method = RequestMethod.GET)
	private String deleteProduct(@RequestParam("code") String code) {
		ProductForm productForm = null;
		if (code != null && code.length() > 0) {
			Product product = productServiceImpl.findProduct(code);
			if (product != null) {
				productForm = new ProductForm(product);
			}
		}
		productServiceImpl.delete(productForm);
		return "redirect:/admin/products";
	}
}
