package com.example.shopping.form;

import com.example.shopping.entities.Contact;

public class ContactForm {
	private int id;
	private String address;
	private String phone;
	private String email;

	public ContactForm() {

	}
	
	public ContactForm(Contact contact) {
		this.id = contact.getId();
		this.address = contact.getAddress();
		this.phone = contact.getPhone();
		this.email = contact.getEmail();
	}
	
	public ContactForm(int id, String address, String phone, String email) {
		this.id = id;
		this.address = address;
		this.phone = phone;
		this.email = email;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
