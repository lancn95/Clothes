package com.example.shopping.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "Products")
public class Product implements Serializable {

	private static final long serialVersionUID = -1000119078147252957L;

	@Id
	@Column(name = "Code", length = 20, nullable = false)
	private String code;

	@Column(name = "Name", length = 100, nullable = false)
	private String name;

	@Column(name = "Description", length = 100, nullable = false)
	private String description;

	@Column(name = "Price", nullable = false)
	private double price;
	
	// Method này trả về byte[]
	// @Lob trong trường hợp này chú thích cho cột BLOB
	@Lob
	@Column(name = "Image", length = Integer.MAX_VALUE, nullable = true)
	private byte[] image;
	
	// TemporalType.DATE chú thích cột sẽ lưu trữ ngày tháng năm (bỏ đi thời gian)
	// TemporalType.TIME chú thích cột sẽ lưu trữ thời gian (Giờ phút giây)
	// TemporalType.TIMESTAMP chú thích cột sẽ lưu trữ ngày tháng và cả thời gian
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "Create_Date", nullable = false)
	private Date createDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "categories_id", nullable = false, //
			foreignKey = @ForeignKey(name = "PRODUCT_CATE_FK"))
	private Category category;

	public Product() {

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

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

}
