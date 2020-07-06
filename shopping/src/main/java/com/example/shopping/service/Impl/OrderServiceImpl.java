package com.example.shopping.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.shopping.dao.OrderDAO;
import com.example.shopping.model.CartInfo;
import com.example.shopping.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService{
	
	@Autowired
	private OrderDAO orderDAO;
	
	@Override
	public void saveOrder(CartInfo cartInfo) {
		orderDAO.saveOder(cartInfo);
		
	}

}
