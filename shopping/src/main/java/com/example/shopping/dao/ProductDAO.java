package com.example.shopping.dao;

import java.io.IOException;
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

import com.example.shopping.entities.Product;
import com.example.shopping.form.ProductForm;
import com.example.shopping.model.ProductInfo;

@Transactional
@Repository
public class ProductDAO {

	@Autowired
	private SessionFactory sessionFactory;

	public List<Product> findAll() {
		try {
			String sql = "select p from " + Product.class.getName() + " p ";
			Session session = this.sessionFactory.getCurrentSession();
			Query<Product> query = session.createQuery(sql, Product.class);
			return query.getResultList();
		} catch (NoResultException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Product findProduct(String code) {
		try {
			String sql = "select p from " + Product.class.getName() + " p where p.code =: code";

			Session session = sessionFactory.getCurrentSession();
			Query<Product> query = session.createQuery(sql, Product.class);
			query.setParameter("code", code);

			return (Product) query.getSingleResult();
		} catch (NoResultException e) {
			e.printStackTrace();
			return null;
		}
	}

	public ProductInfo findProductInfo(String code) {
		Product product = this.findProduct(code);
		if (product == null) {
			return null;
		}
		return new ProductInfo(product.getCode(), product.getName(), product.getPrice());
	}

	@SuppressWarnings("unused")
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public void saveProduct(ProductForm productForm) {
		Session session = sessionFactory.getCurrentSession();
		String code = productForm.getCode();

		boolean isNew = false;
		Product product = null;
		if (product != null) {
			product = this.findProduct(code);
		}
		if (product == null) {
			isNew = true;
			product = new Product();
			product.setCreateDate(new Date());
		}
		product.setCode(code);
		product.setName(productForm.getName());
		product.setDescription(productForm.getDescription());
		product.setPrice(productForm.getPrice());

		if (productForm.getFileData() != null) {
			byte[] image = null;
			try {
				image = productForm.getFileData().getBytes();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (image != null && image.length > 0) {
				product.setImage(image);
			}

		}
		if (isNew) {
			session.persist(product);
		}
		// Nếu có lỗi tại DB, ngoại lệ sẽ ném ra ngay lập tức
		session.flush();
	}
	
	@SuppressWarnings("unused")
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public void updateProduct(ProductForm productForm) {
		Session session = sessionFactory.getCurrentSession();
		String code = productForm.getCode();
		
		Product	product = this.findProduct(code);
		product.setCode(code);
		product.setName(productForm.getName());
		product.setDescription(productForm.getDescription());
		product.setPrice(productForm.getPrice());
		
		if(productForm.getFileData() != null) {
			byte[] image = null;
			try {
				image = productForm.getFileData().getBytes();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (image != null && image.length > 0) {
				product.setImage(image);
			}

		}
		session.update(product);
		
		session.flush();
		
	}
	
	@SuppressWarnings("unused")
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public void deleteProduct(ProductForm productForm) {
		Session session = sessionFactory.getCurrentSession();
		String code = productForm.getCode();
		
		Product	product = this.findProduct(code);
		// session.delete(object) chỉ thực hiện đc khi chưa có khóa chính hoặc ngoại
		session.delete(product);
		
		session.flush();
		
	}

}
