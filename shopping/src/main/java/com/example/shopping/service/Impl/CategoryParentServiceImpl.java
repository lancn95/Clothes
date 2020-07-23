package com.example.shopping.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.shopping.dao.CategoryParentDAO;
import com.example.shopping.entities.CategoryParent;
import com.example.shopping.form.CategoryParentForm;
import com.example.shopping.service.CategoryParentService;

@Service
public class CategoryParentServiceImpl implements CategoryParentService {

	@Autowired
	private CategoryParentDAO categoryParentDAO;

	@Override
	public List<CategoryParent> findAll() {
		List<CategoryParent> list = categoryParentDAO.findAll();
		
		return list;
	}

	

	@Override
	public List<CategoryParent> searchByNameLike(String name) {
		List<CategoryParent> list = categoryParentDAO.searchByNameLike(name);
		
 		return list;
	}



	@Override
	public void create(CategoryParentForm categoryParentForm) {
		categoryParentDAO.create(categoryParentForm);
		
	}



	@Override
	public void update(CategoryParentForm categoryParentForm) {
		categoryParentDAO.update(categoryParentForm);
		
	}



	@Override
	public void delete(CategoryParentForm categoryParentForm) {
		categoryParentDAO.delete(categoryParentForm);
		
	}



	@Override
	public CategoryParent findById(int id) {
		
		return categoryParentDAO.findById(id);
	}

}
