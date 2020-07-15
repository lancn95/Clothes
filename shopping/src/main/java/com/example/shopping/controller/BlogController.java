package com.example.shopping.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class BlogController {
	@RequestMapping(value = { "/blog" }, method = RequestMethod.GET)
	public String blog(HttpServletRequest request, HttpServletResponse response, Model model) {

		return "customer/blog";
	}

	@RequestMapping(value = { "/blogDetail" }, method = RequestMethod.GET)
	public String blogDetail(HttpServletRequest request, HttpServletResponse response, Model model) {

		return "customer/blog-details";
	}
}
