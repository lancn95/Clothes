package com.example.shopping.service;

import java.util.List;

import com.example.shopping.entities.Contact;
import com.example.shopping.form.ContactForm;

public interface ContactService {
	public List<Contact> finđAll();
	
	public void save(ContactForm contactForm);
	
	public void update(ContactForm contactForm);
	
	public void delete(ContactForm contactForm);
	
	public List<Contact> searchByNameLike(String name);
	
	public Contact findById(int id);
}
