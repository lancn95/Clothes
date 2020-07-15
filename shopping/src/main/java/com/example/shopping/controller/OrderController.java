package com.example.shopping.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.shopping.model.OrderDetailInfo;
import com.example.shopping.model.OrderInfo;
import com.example.shopping.service.Impl.OrderServiceImpl;

@Controller
public class OrderController {
	
	@Autowired
	private OrderServiceImpl orderServiceImpl;
	
	// order list
		@RequestMapping(value = "/admin/orders", method = RequestMethod.GET)
		private String listOrder(Model model) {
			List<OrderInfo> orderInfos = orderServiceImpl.findAll();

			model.addAttribute("orders", orderInfos);
			return "admin/list_order";
		}
		
		
		// order detail 
		@RequestMapping(value = "/admin/order", method = RequestMethod.GET)
		private String viewOrderDetail(Model model, @RequestParam("orderId") String orderId) {
			OrderInfo orderInfo = null;
			if (orderId != null) {
				orderInfo = orderServiceImpl.findOrderInfo(orderId);
			}
			if (orderInfo == null) {
				return "redirect:/admin/orders";
			}
			List<OrderDetailInfo> details = orderServiceImpl.findAllOrderDetail(orderId);
			orderInfo.setDetails(details);
			model.addAttribute("orderInfo", orderInfo);

			return "admin/order_detail";
		}
}
