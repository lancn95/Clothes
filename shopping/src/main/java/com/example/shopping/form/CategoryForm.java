package com.example.shopping.form;

import com.example.shopping.entities.Category;

public class CategoryForm {
	private String id;
	private String name;
	
	public CategoryForm() {}
	
	public CategoryForm(Category category) {
		this.id = category.getId();
		this.name = category.getName();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
