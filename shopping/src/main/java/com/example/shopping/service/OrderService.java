package com.example.shopping.service;

import java.util.List;

import com.example.shopping.model.CartInfo;
import com.example.shopping.model.OrderDetailInfo;
import com.example.shopping.model.OrderInfo;

public interface OrderService {
	public void saveOrder(CartInfo cartInfo, Long CustomerId);
	
	public List<OrderInfo> findAll();
	
	public OrderInfo findOrderInfo(String orderId);
	
	public List<OrderDetailInfo> findAllOrderDetail(String orderId);
	
	public List<OrderInfo> findOrdersByCusID(Long customerId);
	
	public void updateStatus(String orderId, String status);
}
