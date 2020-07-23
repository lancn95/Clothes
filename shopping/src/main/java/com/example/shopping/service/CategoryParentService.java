package com.example.shopping.service;

import java.util.List;

import com.example.shopping.entities.CategoryParent;
import com.example.shopping.form.CategoryParentForm;

public interface CategoryParentService {
	List<CategoryParent> findAll();
	
	void create(CategoryParentForm categoryParentForm);
	
	void update(CategoryParentForm categoryParentForm);
	
	void delete(CategoryParentForm categoryParentForm);
	
	List<CategoryParent> searchByNameLike(String name);

	CategoryParent findById(int id);
}
