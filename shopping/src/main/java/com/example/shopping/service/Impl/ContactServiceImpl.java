package com.example.shopping.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.shopping.dao.ContactDAO;
import com.example.shopping.entities.Contact;
import com.example.shopping.form.ContactForm;
import com.example.shopping.service.ContactService;

@Service
public class ContactServiceImpl implements ContactService {

	@Autowired
	private ContactDAO contactDAO;

	@Override
	public List<Contact> finđAll() {
		List<Contact> contacts = contactDAO.finđAll();
		
		return contacts;
	}

	@Override
	public void save(ContactForm contactForm) {
		contactDAO.save(contactForm);

	}

	@Override
	public void update(ContactForm contactForm) {
		contactDAO.update(contactForm);
	}

	@Override
	public void delete(ContactForm contactForm) {
		contactDAO.delete(contactForm);

	}

	@Override
	public List<Contact> searchByNameLike(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Contact findById(int id) {
		Contact contact = contactDAO.findContactById(id);
		
		return contact;
	}

}
