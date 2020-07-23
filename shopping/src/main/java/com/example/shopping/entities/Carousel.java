package com.example.shopping.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "carousel")
public class Carousel {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private int id;

	@Column(name = "title")
	private String title;

	// Method này trả về String
	// @Lob trong trường hợp này sẽ chú thích cho CLOB.
	@Lob
	@Column(name = "description")
	private String description;

	@Column(name = "tag")
	private String tag;

	// Method này trả về byte[]
	// @Lob trong trường hợp này chú thích cho cột BLOB
	@Lob
	@Column(name = "image")
	private byte[] image;

	@Column(name = "createdate")
	private Date createDate;

	@Column(name = "url")
	private String url;

	public Carousel() {

	}

	public Carousel(int id, String title, String description, String tag, byte[] image, Date createDate, String url) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.tag = tag;
		this.image = image;
		this.createDate = createDate;
		this.url = url;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
