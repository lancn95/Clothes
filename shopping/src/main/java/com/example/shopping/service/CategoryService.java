package com.example.shopping.service;

import java.util.List;

import com.example.shopping.entities.Category;
import com.example.shopping.form.CategoryForm;

public interface CategoryService {
	public List<Category> findAll();
	
	public void saveCategory(CategoryForm categoryForm);
	
	public Category findCategory(String id);
	
	public void update(CategoryForm categoryForm);
	
	public void delete(CategoryForm categoryForm);
		

}
