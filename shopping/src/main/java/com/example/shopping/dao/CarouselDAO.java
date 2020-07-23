package com.example.shopping.dao;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.shopping.entities.Carousel;
import com.example.shopping.form.CarouselForm;
import com.example.shopping.form.ProductForm;

@Repository
public class CarouselDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Transactional(rollbackOn = Exception.class)
	public List<Carousel> findAll() {
		Session session = sessionFactory.getCurrentSession();

		String sql = "select c from " + Carousel.class.getName() + " c " + " order by createdate desc";
		Query<Carousel> query = session.createQuery(sql, Carousel.class);

		return query.getResultList();
	}

	@Transactional(rollbackOn = Exception.class)
	public Carousel findById(int id) {
		Session session = sessionFactory.getCurrentSession();

		return session.find(Carousel.class, id);
	}

	@Transactional(rollbackOn = Exception.class)
	public void save(CarouselForm carouselForm) {
		Session session = sessionFactory.getCurrentSession();
		Carousel carousel = new Carousel();

		if (carouselForm != null) {
			carousel.setTitle(carouselForm.getTitle());
			carousel.setDescription(carouselForm.getDescription());
			carousel.setTag(carouselForm.getTag());
			carousel.setCreateDate(new Date());
			carousel.setUrl("img/" + carouselForm.getFileData().getOriginalFilename());

			//System.out.println(carouselForm.getFileData().getOriginalFilename());
			if (carouselForm.getFileData() != null) {
				byte[] image = null;
				try {
					image = carouselForm.getFileData().getBytes();
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (image != null && image.length > 0) {
					carousel.setImage(image);
				}
			}
			
			session.persist(carousel);
			
			session.flush();
		}
	}
}
