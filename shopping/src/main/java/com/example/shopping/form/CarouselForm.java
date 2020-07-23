package com.example.shopping.form;

import org.springframework.web.multipart.MultipartFile;

public class CarouselForm {
	private int id;
	private String title;
	private MultipartFile fileData;
	private String description;
	private String tag;
	private String url;

	public CarouselForm() {
	}

	public CarouselForm(int id, String title, MultipartFile fileData, String description, String tag, String url) {
		this.id = id;
		this.title = title;
		this.fileData = fileData;
		this.description = description;
		this.tag = tag;
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

	public MultipartFile getFileData() {
		return fileData;
	}

	public void setFileData(MultipartFile fileData) {
		this.fileData = fileData;
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
