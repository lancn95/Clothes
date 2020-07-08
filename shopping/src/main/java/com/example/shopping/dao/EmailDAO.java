package com.example.shopping.dao;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.shopping.entities.AppUser;

@Repository
public class EmailDAO {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Transactional(rollbackFor = Exception.class)
	public AppUser findByEmail(String email) {
		try {
			Session session = this.sessionFactory.getCurrentSession();
			String sql = "select u from " + AppUser.class.getName() + " u " + "where u.email = :email";
			Query query = session.createQuery(sql, AppUser.class);
			query.setParameter("email", email);

			return (AppUser) query.getSingleResult();

		} catch (Exception e) {
			e.printStackTrace();

		}
		return null;
	}
	
	@Transactional(rollbackFor = Exception.class)
	public AppUser findByResetToken(String resetToken) {
		try {
			String sql = "select u from " + AppUser.class.getName() + " u " + "where u.resetToken = :resetToken";
			Session session = this.sessionFactory.getCurrentSession();
			Query query = session.createQuery(sql, AppUser.class);
			query.setParameter("resetToken", resetToken);
			
			return (AppUser) query.getSingleResult();

		} catch (Exception e) {
			e.printStackTrace();

		}
		return null;
	}
}
