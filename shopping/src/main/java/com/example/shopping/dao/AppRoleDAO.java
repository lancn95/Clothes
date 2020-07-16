package com.example.shopping.dao;

import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.shopping.entities.UserRole;

@Repository
public class AppRoleDAO {
	
	@Autowired
	private EntityManager entityManager;
	
	
	public List<String> getRoleNames(Long userId){
		String sql = "SELECT ur.appRole.roleName from " + UserRole.class.getName() + " ur " + "WHERE ur.appUser.userId = :userId";
		
		Query<String> query = (Query<String>) this.entityManager.createQuery(sql, String.class);
		query.setParameter("userId", userId);
		return query.getResultList();
	}
}
