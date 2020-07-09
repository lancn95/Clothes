package com.example.shopping.dao;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class UserRoleDAO {

	@Autowired
	private EntityManager entityManager;

	public void addRoleForUser(Long userId) {
		try {
			entityManager.createNativeQuery("insert into user_role (User_Id, Role_id) values (?,?)")
					.setParameter(1, userId)
					.setParameter(2, 2)
					.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
