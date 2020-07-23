package com.example.shopping.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.shopping.model.CartInfo;
import com.example.shopping.model.CartLineInfo;

public class Utils {
	
	@Autowired
	public static SessionFactory sessionFactory;
	
	// Thông tin các sản phẩm trong giỏ hàng, được lưu trữ trong Session.
	public static CartInfo getInfoCartInSession(HttpServletRequest request) {

		CartInfo cartInfo = (CartInfo) request.getSession().getAttribute("myCart");

		if (cartInfo == null) {
			cartInfo = new CartInfo();

			request.getSession().setAttribute("myCart", cartInfo);
		}

		return cartInfo;
	}

	public static void removeCartInSession(HttpServletRequest request) {
		request.getSession().removeAttribute("mycart");
	}

	public static void storeLastOrderedCartInSession(HttpServletRequest request, CartInfo cartInfo) {
		request.getSession().setAttribute("lastOrderCart", cartInfo);
	}

	public static CartInfo getLastOrderCartInSession(HttpServletRequest request) {
		
		return (CartInfo) request.getSession().getAttribute("lastOrderCart");
	}
	
	// Thông tin sản phẩm chit tiết trong giỏ hàng, được lưu trữ trong Session.
	public static List<CartLineInfo> getInfoCartLinesInSession(HttpServletRequest request) {

		List<CartLineInfo> lines = (List<CartLineInfo>) request.getSession().getAttribute("myCartLine");

		if (lines == null) {
			lines = new ArrayList<CartLineInfo>();

			request.getSession().setAttribute("myCartLine", lines);
		}

		return lines;
	}
	
	
}
