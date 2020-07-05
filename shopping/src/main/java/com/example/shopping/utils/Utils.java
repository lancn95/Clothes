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
}
