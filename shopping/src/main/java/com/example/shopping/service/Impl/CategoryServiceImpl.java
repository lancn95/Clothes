package com.example.shopping.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.shopping.dao.CategoryDAO;
import com.example.shopping.entities.Category;
import com.example.shopping.form.CategoryForm;
import com.example.shopping.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryDAO categoryDAO;

	@Override
	public List<Category> findAll() {
		List<Category> categories = categoryDAO.getAllCategory();

		return categories;
	}

	@Override
	public void saveCategory(CategoryForm categoryForm) {
		categoryDAO.saveCategory(categoryForm);

	}
	
	@Override
	public Category findCategory(String id) {

		return categoryDAO.findById(id);
	}
	
	@Override
	public void update(CategoryForm categoryForm) {
		categoryDAO.updateCategory(categoryForm);
		
	}
	
	@Override
	public void delete(CategoryForm categoryForm) {
		categoryDAO.deleteCategory(categoryForm);
		
	}

}
