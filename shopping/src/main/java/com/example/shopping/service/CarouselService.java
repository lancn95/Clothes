package com.example.shopping.service;

import java.util.List;

import com.example.shopping.entities.Carousel;
import com.example.shopping.form.CarouselForm;

public interface CarouselService {

	List<Carousel> findAll();

	Carousel findById(int id);

	void save(CarouselForm carouselForm);

	void update(CarouselForm carouselForm);

	void delete(CarouselForm carouselForm);

	List<Carousel> searchByNameLike(String name);

}
