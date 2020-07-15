package com.example.shopping.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.shopping.entities.AppUser;
import com.example.shopping.service.Impl.UserServiceImpl;
import com.example.shopping.utils.WebUtils;

@Controller
public class AdminController {
	
	@Autowired
	private UserServiceImpl userServiceImpl;
	
	@RequestMapping(value = "/admin", method = RequestMethod.GET)
	public String adminPage(Model model, Principal principal) {

		User loginedUser = (User) ((Authentication) principal).getPrincipal();
		System.out.println("admin name: " + principal.getName());

		String userInfo = WebUtils.toString(loginedUser);
		String adminname = principal.getName();
		model.addAttribute("userInfo", userInfo);
		model.addAttribute("name", adminname);
		
		
		return "admin/index";
	}
	
	@RequestMapping(value = "/adminInfo", method = RequestMethod.GET)
	public String userInfo(Model model, Principal principal) {

		// Sau khi user login thành công sẽ có principal
		String userName = principal.getName();

		// principal là 1 đối tượng sẽ lưu toàn bộ thông tin của 1 tk authentication
		// đăng nhập thành công

		User loginedUser = (User) ((Authentication) principal).getPrincipal();

		String userInfo = WebUtils.toString(loginedUser);
		model.addAttribute("userInfo", userInfo);

		// get info user by username
		AppUser appUser = userServiceImpl.findByName(principal.getName());
		model.addAttribute("appUser", appUser);

		return "admin/userInfoPage";
	}
	
	@RequestMapping(value = "/adminInfo", method = RequestMethod.POST)
	public String updateUserInfo(@ModelAttribute AppUser appUser, Principal principal) {
		String phone = appUser.getPhone();
		String firstname = appUser.getFirstName();
		String lastname = appUser.getLastName();
		String street = appUser.getStreet();
		String town = appUser.getTown();
		String email = appUser.getEmail();
		
		userServiceImpl.updateUserByName(firstname, lastname, street, town, email, phone, principal.getName());

		return "redirect:/adminInfo";
	}
}
