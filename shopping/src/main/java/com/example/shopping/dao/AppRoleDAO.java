package com.example.shopping.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.shopping.entities.AppRole;
import com.example.shopping.entities.UserRole;

@Repository
public class AppRoleDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Transactional(rollbackFor = Exception.class)
	public List<String> getRoleNames(Long userId) {
		String sql = "SELECT ur.appRole.roleName from " + UserRole.class.getName() + " ur "
				+ "WHERE ur.appUser.userId = :userId";
		Session session = sessionFactory.getCurrentSession();
		Query<String> query = (Query<String>) session.createQuery(sql, String.class);
		query.setParameter("userId", userId);
		return query.getResultList();
	}

	@Transactional(rollbackFor = Exception.class)
	public List<AppRole> findAll() {
		String sql = "select r from " + AppRole.class.getName() + " r ";
		Session session = sessionFactory.getCurrentSession();
		Query<AppRole> query = session.createQuery(sql, AppRole.class);

		return query.getResultList();
	}
}
