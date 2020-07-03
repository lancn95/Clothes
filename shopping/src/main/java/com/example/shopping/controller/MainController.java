package com.example.shopping.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.shopping.entities.AppUser;
import com.example.shopping.entities.Product;
import com.example.shopping.service.Impl.ProductServiceImpl;
import com.example.shopping.service.Impl.UserServiceImpl;
import com.example.shopping.untils.WebUtils;

@Controller
public class MainController {

	@Autowired
	private UserServiceImpl userServiceImpl;
	
	@Autowired
	private ProductServiceImpl productServiceImpl;

	@RequestMapping(value = { "/", "/welcome" }, method = RequestMethod.GET)
	public String welcomePage(Model model) {
		model.addAttribute("title", "Welcome");
		model.addAttribute("message", "This is welcome page!");
		return "customer/index";
	}
	
	@RequestMapping(value = "/productList", method = RequestMethod.GET)
	public String listProductHandler(Model model) {
		List<Product> products = productServiceImpl.findAll();
		model.addAttribute("products", products);
		return "customer/list_test";
	}
	
	@RequestMapping(value = "/product-update/{id}", method = RequestMethod.GET)
	public String getInfoProduct() {
		return "customer/product_update";
	}
	
	@RequestMapping(value = "/product-update/{id}", method = RequestMethod.POST)
	public String updateProduct() {
		return "redirect:/";
	}
	
	@RequestMapping(value = "/product-delete/{id}", method = RequestMethod.GET)
	public String deleteProduct() {
		return "redirect:/";
	}
	
	
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String register(Model model) {
		AppUser appUser = new AppUser();
		model.addAttribute("appUser", appUser);

		System.out.println("GET username: " + appUser.getUserName());
		return "customer/register";
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String createUser(@ModelAttribute AppUser appUser, RedirectAttributes redirect, Model model) {

		if (appUser == null) {
			return "customer/register";
		}

		System.out.println("POST username: " + appUser.getUserName());

		if (appUser != null) {

			List<AppUser> appUsers = userServiceImpl.findAll();

			for (AppUser u : appUsers) {
				if (u == null) {
					return "customer/register";
				} else {
					if (userServiceImpl.isDuplicateUser(model, appUser.getUserName(), u.getUserName()) == true) {
						model.addAttribute("duplicate_name");
						return "customer/register";
					}

					if (userServiceImpl.isDuplicateUser(model, appUser.getEmail(), u.getEmail()) == true) {
						model.addAttribute("duplicate_email", "Email này đã được sử dụng!");
						return "customer/register";
					}

				}
			}
			userServiceImpl.createUser(appUser);

		}

		redirect.addFlashAttribute("success", "Bạn đã đăng ký thành công, hãy đăng nhập nhé");
		return "redirect:/login";
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginpage(Model model) {

		return "customer/login";
	}

	@RequestMapping(value = "/userAccountInfo", method = RequestMethod.GET)
	public String userAccountInfo(Model model) {
		return "redirect:/userInfo";
	}

	@RequestMapping(value = "/userInfo", method = RequestMethod.GET)
	public String userInfo(Model model, Principal principal) {

		// Sau khi user login thành công sẽ có principal
		String userName = principal.getName();

		// principal là 1 đối tượng sẽ lưu toàn bộ thông tin của 1 tk authentication
		// đăng nhập thành công

		System.out.println("User Name: " + userName);

		User loginedUser = (User) ((Authentication) principal).getPrincipal();

		String userInfo = WebUtils.toString(loginedUser);
		model.addAttribute("userInfo", userInfo);

		// get info user by username
		AppUser appUser = userServiceImpl.findByName(principal.getName());
		model.addAttribute("appUser", appUser);

		return "customer/userInfoPage";
	}

	@RequestMapping(value = "/userInfo", method = RequestMethod.POST)
	public String updateUserInfo(@ModelAttribute AppUser appUser, Principal principal) {
		String phone = appUser.getPhone();
		String firstname = appUser.getFirstName();
		String lastname = appUser.getLastName();
		String street = appUser.getStreet();
		String town = appUser.getTown();
		String email = appUser.getEmail();

		System.out.println("phone: " + phone);
		System.out.println("UserName: " + principal.getName());
		userServiceImpl.updateUserByName(firstname, lastname, street, town, email, phone, principal.getName());

		return "redirect:/userInfo";
	}

	@RequestMapping(value = "/admin", method = RequestMethod.GET)
	public String adminPage(Model model, Principal principal) {

		User loginedUser = (User) ((Authentication) principal).getPrincipal();
		System.out.println("useradminname: " + principal.getName());
		
		String userInfo = WebUtils.toString(loginedUser);
		String adminname = principal.getName();
		model.addAttribute("userInfo", userInfo);
		model.addAttribute("name", adminname);
		return "admin/index";
	}

	@RequestMapping(value = "/403", method = RequestMethod.GET)
	public String accessDenied(Model model, Principal principal) {

		if (principal != null) {
			User loginedUser = (User) ((Authentication) principal).getPrincipal();

			String userInfo = WebUtils.toString(loginedUser);
			model.addAttribute("userInfo", userInfo);

			String message = "Hi" + principal.getName() + "<br/> You do not have permission to access this page!";

			model.addAttribute("message", message);
		}

		return "403Page";
	}

	@RequestMapping(value = "/logoutSuccessful", method = RequestMethod.GET)
	public String logoutSuccessfulPage(Model model) {
		model.addAttribute("title", "Logout");
		return "redirect:/";
	}
}
