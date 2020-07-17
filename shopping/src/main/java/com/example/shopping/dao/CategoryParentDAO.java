package com.example.shopping.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.shopping.entities.CategoryParent;

@Repository
public class CategoryParentDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Transactional(rollbackFor = Exception.class)
	public List<CategoryParent> findAll() {
		try {
			String sql = "select c from " + CategoryParent.class.getName() + " c ";
			Session session = sessionFactory.getCurrentSession();
			Query<CategoryParent> query = session.createQuery(sql, CategoryParent.class);
			
			return query.getResultList();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
