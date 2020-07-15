package com.example.shopping.model;

import java.util.Date;

import com.example.shopping.entities.Product;

public class ProductInfo {
	private String code;
	private String name;
	private String description;
	private double price;
	private byte[] image;
	private String category_id;
	private Date createDate;
	
	public ProductInfo(Product product) {
		this.code = product.getCode();
		this.name = product.getName();
		this.price = product.getPrice();
		this.description = product.getDescription();
		this.image = product.getImage();
		this.category_id = product.getCategory().getId();
	}
	
	// Sử dụng trong JPA/Hibernate query
	public ProductInfo(String code, String name, double price, String description, byte[] image, String category_id) {
		this.code = code;
		this.name = name;
		this.price = price;
		this.description = description;
		this.image = image;
		this.category_id = category_id;
	}

	public ProductInfo() {

	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public String getCategory_id() {
		return category_id;
	}

	public void setCategory_id(String category_id) {
		this.category_id = category_id;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	
}
