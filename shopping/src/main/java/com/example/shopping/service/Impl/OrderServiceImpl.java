package com.example.shopping.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.shopping.dao.OrderDAO;
import com.example.shopping.model.CartInfo;
import com.example.shopping.model.OrderDetailInfo;
import com.example.shopping.model.OrderInfo;
import com.example.shopping.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderDAO orderDAO;

	@Override
	public void saveOrder(CartInfo cartInfo, Long CustomerId) {
		orderDAO.saveOder(cartInfo, CustomerId);

	}

	@Override
	public List<OrderInfo> findAll() {

		return orderDAO.findAll();
	}

	@Override
	public OrderInfo findOrderInfo(String orderId) {

		return orderDAO.getOrderInfo(orderId);
	}

	@Override
	public List<OrderDetailInfo> findAllOrderDetail(String orderId) {
		List<OrderDetailInfo> details = orderDAO.listOrderDetailInfos(orderId);
		return details;
	}

	@Override
	public List<OrderInfo> findOrdersByCusID(Long customerId) {
		List<OrderInfo> orderInfos = orderDAO.findOrdersByCusID(customerId);

		return orderInfos;
	}

}
