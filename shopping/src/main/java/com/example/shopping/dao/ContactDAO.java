package com.example.shopping.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.shopping.entities.Contact;
import com.example.shopping.form.ContactForm;
import com.example.shopping.utils.DeleteCommon;

@Repository
public class ContactDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private DeleteCommon deleteCommon;

	@Transactional(rollbackOn = Exception.class)
	public List<Contact> finđAll() {
		Session session = sessionFactory.getCurrentSession();

		String sql = "select c from " + Contact.class.getName() + " c ";
		Query<Contact> query = session.createQuery(sql, Contact.class);

		return query.getResultList();
	}

	@Transactional(rollbackOn = Exception.class)
	public void save(ContactForm contactForm) {
		Session session = sessionFactory.getCurrentSession();
		Contact contact = new Contact();

		if (contactForm != null) {
			contact.setAddress(contactForm.getAddress());
			contact.setEmail(contactForm.getEmail());
			contact.setPhone(contactForm.getPhone());

			session.persist(contact);

			session.flush();

		}

	}

	@Transactional(rollbackOn = Exception.class)
	public Contact findContactById(int id) {
		Session session = sessionFactory.getCurrentSession();

		return session.find(Contact.class, id);
	}

	@Transactional(rollbackOn = Exception.class)
	public void update(ContactForm contactForm) {
		Session session = sessionFactory.getCurrentSession();
		if (contactForm != null) {
			Contact contact = this.findContactById(contactForm.getId());

			contact.setAddress(contactForm.getAddress());
			contact.setEmail(contactForm.getEmail());
			contact.setPhone(contactForm.getPhone());

			session.update(contact);

			session.flush();
		}
	}

	@Transactional(rollbackOn = Exception.class)
	public void delete(ContactForm contactForm) {
		
		boolean result = deleteCommon.deleteById(Contact.class, contactForm.getId(), sessionFactory);
	}
}
