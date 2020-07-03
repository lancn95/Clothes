package com.example.shopping.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.shopping.entities.AppUser;

@Transactional
@Repository
public class AppUserDAO {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private EntityManager entityManager;

	@SuppressWarnings("unused")
	public AppUser findUserAccount(String userName) {
		try {
			Session session = sessionFactory.getCurrentSession();
			String sql = "Select e from " + AppUser.class.getName() + " e " + "Where e.userName =:userName ";
			// HSQL truy vấn trên đối tượng sau đó JPA sẽ translate
			Query query = entityManager.createQuery(sql, AppUser.class);
			query.setParameter("userName", userName);

			return (AppUser) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<AppUser> findAll() {
		try {
			String sql = "select e from " + AppUser.class.getName() + " e ";
			Query query = entityManager.createQuery(sql, AppUser.class);
			return query.getResultList();
		} catch (NoResultException e) {
			return null;

		}
	}

	@Transactional(rollbackFor = Exception.class)
	public void save(AppUser appUser) {
		try {
			// nativeQuery : query theo table name,field ở trong Database
			entityManager.createNativeQuery(
					"INSERT INTO App_User (User_Id, User_Name, Encryted_Password, Email, Enabled) VALUES (?,?,?,?,?)")
					.setParameter(1, appUser.getUserId()).setParameter(2, appUser.getUserName())
					.setParameter(3, appUser.getEncrytedPassword()).setParameter(4, appUser.getEmail())
					.setParameter(5, appUser.isEnabled()).executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateUserByName(String firstName, String lastName, String street, String town, String email,
			String phone, String userName) {
		try {
			// nativeQuery :query theo table name,field ở trong Database
			entityManager.createNativeQuery(
					"UPDATE App_User SET First_Name = ?, Last_Name = ?, Street = ?, Town = ?, Email = ?, phone = ? WHERE User_Name = ?")
					.setParameter(1, firstName).setParameter(2, lastName).setParameter(3, street).setParameter(4, town)
					.setParameter(5, email).setParameter(6, phone).setParameter(7, userName).executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
