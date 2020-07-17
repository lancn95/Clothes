package com.example.shopping.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.shopping.dao.CategoryParentDAO;
import com.example.shopping.entities.CategoryParent;
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

}
