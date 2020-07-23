package com.example.shopping.form;

import java.util.Date;

import com.example.shopping.entities.CategoryParent;

public class CategoryParentForm {

	private int id;
	private String name;
	private String url;
	private Date createdate;

	public CategoryParentForm() {

	}

	public CategoryParentForm(CategoryParent categoryParent) {

		this.id = categoryParent.getId();
		this.name = categoryParent.getName();
		this.url = categoryParent.getName();
		this.createdate = categoryParent.getCreateDate();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Date getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

}
