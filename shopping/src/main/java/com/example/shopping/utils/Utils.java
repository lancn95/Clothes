package com.example.shopping.utils;

import javax.servlet.http.HttpServletRequest;

import com.example.shopping.model.CartInfo;

public class Utils {
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
}
