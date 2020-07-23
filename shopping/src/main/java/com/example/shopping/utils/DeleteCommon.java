package com.example.shopping.utils;

import java.io.Serializable;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;

@Component
public class DeleteCommon {
	
	
	public boolean deleteById(Class<?> type, Serializable id, SessionFactory sessionFactory) {
		Session session = sessionFactory.getCurrentSession();
		Object persistentInstance = session.load(type, id);
		if (persistentInstance != null) {
			session.delete(persistentInstance);
			return true;
		}
		return false;
	}
}
