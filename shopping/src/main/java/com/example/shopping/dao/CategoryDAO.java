package com.example.shopping.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.shopping.entities.Category;
import com.example.shopping.form.CategoryForm;
import com.example.shopping.utils.Utils;

@Repository
public class CategoryDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Transactional(rollbackFor = NoResultException.class)
	public List<Category> getAllCategory() {
		try {
			String sql = "select c from " + Category.class.getName() + " c " + " order by c.createDate desc ";
			Session session = sessionFactory.getCurrentSession();
			Query<Category> query = session.createQuery(sql, Category.class);

			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Transactional(rollbackFor = NoResultException.class)
	public Category findById(String id) {

		try {
			String sql = "select c from " + Category.class.getName() + " c where c.id = :id";

			Session session = sessionFactory.getCurrentSession();
			Query<Category> query = session.createQuery(sql, Category.class);
			query.setParameter("id", id);

			return query.getSingleResult();
		} catch (NoResultException e) {
			e.printStackTrace();
			return null;
		}

	}
	
	@Transactional(rollbackFor = NoResultException.class)
	public List<Category> searchByNameLike(String name) {
		try {
			String sql = "select c from " + Category.class.getName() + " c " + "where c.name like concat ('%', :name ,'%')";
			Session session = sessionFactory.getCurrentSession();
			Query<Category> query = session.createQuery(sql, Category.class);
			query.setParameter("name", name);
			
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public void saveCategory(CategoryForm categoryForm) {

		Session session = sessionFactory.getCurrentSession();
		Category category = new Category();
		category.setId(categoryForm.getId());
		category.setName(categoryForm.getName());
		category.setCreateDate(new Date());
		session.persist(category);

		session.flush();

	}

	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public void updateCategory(CategoryForm categoryForm) {

		Session session = sessionFactory.getCurrentSession();
		Category category = this.findById(categoryForm.getId());

		category.setId(categoryForm.getId());
		category.setName(categoryForm.getName());
		category.setCreateDate(new Date());

		session.update(category);

		session.flush();

	}
	
	@Transactional(rollbackFor = Exception.class)
	private boolean deleteById(Class<?> type, Serializable id) {
		Session session = sessionFactory.getCurrentSession();
		Object persistentInstance = session.load(type, id);
		if (persistentInstance != null) {
			session.delete(persistentInstance);
			return true;
		}
		return false;
	}

	@Transactional(rollbackFor = Exception.class)
	public void deleteCategory(CategoryForm categoryForm) {
		
		boolean result = this.deleteById(Category.class, categoryForm.getId());
		

	}
}
