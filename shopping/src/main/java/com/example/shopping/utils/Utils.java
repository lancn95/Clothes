package com.example.shopping.utils;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.shopping.model.CartInfo;

public class Utils {
	
	@Autowired
	private static SessionFactory sessionFactory;
	
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
	
	@SuppressWarnings("unused")
	public static boolean deleteById(Class<?> type, Serializable id) {
		Session session = sessionFactory.getCurrentSession();
	    Object persistentInstance = session.load(type, id);
	    if (persistentInstance != null) {
	        session.delete(persistentInstance);
	        return true;
	    }
	    return false;
	}
}
