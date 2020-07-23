package com.example.shopping.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.shopping.entities.Category;
import com.example.shopping.entities.CategoryParent;
import com.example.shopping.entities.Order;
import com.example.shopping.form.CategoryParentForm;
import com.example.shopping.utils.DeleteCommon;

@Repository
public class CategoryParentDAO {

	@Autowired
	private SessionFactory sessionFactory;
	

	@Transactional(rollbackFor = Exception.class)
	public List<CategoryParent> findAll() {
		try {
			String sql = "select cp from " + CategoryParent.class.getName() + " cp ";
			Session session = sessionFactory.getCurrentSession();
			Query<CategoryParent> query = session.createQuery(sql, CategoryParent.class);
			
			return query.getResultList();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Transactional(rollbackFor = NoResultException.class)
	public CategoryParent findById(int id) {
		Session session = sessionFactory.getCurrentSession();
		
		return session.find(CategoryParent.class, id);
	}
	
	@Transactional(rollbackFor = NoResultException.class)
	public List<CategoryParent> searchByNameLike(String name) {
		try {
			String sql = "select c from " + CategoryParent.class.getName() + " c " + "where c.name like concat ('%', :name ,'%')";
			Session session = sessionFactory.getCurrentSession();
			Query<CategoryParent> query = session.createQuery(sql, CategoryParent.class);
			query.setParameter("name", name);
			
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Transactional(rollbackFor = Exception.class)
	public void create(CategoryParentForm categoryParentForm) {
		
		Session session = sessionFactory.getCurrentSession();
		CategoryParent cateParent = new CategoryParent();
		cateParent.setName(categoryParentForm.getName());
		cateParent.setUrl("/search/1?name=" + categoryParentForm.getName());
		cateParent.setCreateDate(new Date());
		
		
		session.persist(cateParent);
		
		session.flush();
		
	}
	
	@Transactional(rollbackFor = Exception.class)
	public void update(CategoryParentForm categoryParentForm) {
		Session session = sessionFactory.getCurrentSession();
		
		if(categoryParentForm != null) {
			
			CategoryParent cateParent = this.findById(categoryParentForm.getId());
			cateParent.setName(categoryParentForm.getName());
			cateParent.setUrl("/search/1?name=" + categoryParentForm.getName());
			cateParent.setCreateDate(new Date());
			
			session.update(cateParent);
			
			session.flush();
		}
		
	}
	
	@Transactional(rollbackFor = Exception.class)
	public void delete(CategoryParentForm categoryParentForm) {
		DeleteCommon deleteCommon = new DeleteCommon();
		
		boolean result = deleteCommon.deleteById(CategoryParent.class, categoryParentForm.getId(), sessionFactory);
	}
	
}
