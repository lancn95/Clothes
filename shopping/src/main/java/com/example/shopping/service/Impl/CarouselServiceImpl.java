package com.example.shopping.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.shopping.dao.CarouselDAO;
import com.example.shopping.entities.Carousel;
import com.example.shopping.form.CarouselForm;
import com.example.shopping.service.CarouselService;

@Service
public class CarouselServiceImpl implements CarouselService {

	@Autowired
	private CarouselDAO carouselDAO;

	@Override
	public List<Carousel> findAll() {
		List<Carousel> carousels = carouselDAO.findAll();

		return carousels;
	}

	@Override
	public Carousel findById(int id) {
		Carousel carousel = carouselDAO.findById(id);
		
		return carousel;
	}

	@Override
	public void save(CarouselForm carouselForm) {
		carouselDAO.save(carouselForm);

	}

	@Override
	public void update(CarouselForm carouselForm) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(CarouselForm carouselForm) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Carousel> searchByNameLike(String name) {
		// TODO Auto-generated method stub
		return null;
	}

}
