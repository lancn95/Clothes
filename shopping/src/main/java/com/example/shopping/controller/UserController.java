package com.example.shopping.controller;

import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.shopping.entities.AppRole;
import com.example.shopping.entities.AppUser;
import com.example.shopping.form.UserForm;
import com.example.shopping.mapping.UserFormMapping;
import com.example.shopping.model.UserInfo;
import com.example.shopping.service.RoleService;
import com.example.shopping.service.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;

	@RequestMapping(value = "/account-search/{pageNumber}")
	public String searchAccount(@RequestParam("name") String name, Model model, HttpServletRequest request,
			@PathVariable int pageNumber, Principal principal) {
		// fill admin name sign in
		String adminname = principal.getName();
		model.addAttribute("name", adminname);

		if (name.isEmpty()) {
			return "redirect:/admin/account";
		}
		List<UserInfo> list = userService.searchByNameLikeUserInfo(name);

		if (list == null || list.size() < 0) {
			return "redirect:/admin/account";
		}
		PagedListHolder<?> pages = (PagedListHolder<?>) request.getSession().getAttribute("accountlist");
		int pageSize = 6;
		pages = new PagedListHolder<>(list);

		pages.setPageSize(pageSize);
		final int goToPage = pageNumber;
		if (goToPage <= pages.getPageCount() && goToPage >= 0) {
			pages.setPage(goToPage);
		}

		request.getSession().setAttribute("accountlist", pages);
		int current = pages.getPage();
		int begin = 1;
		int end = pages.getPageCount();
		int totalPageCount = pages.getPageCount();
		String baseUrl = "/admin/account/page/";

		model.addAttribute("beginIndex", begin);
		model.addAttribute("endIndex", end);
		model.addAttribute("currentIndex", current);
		model.addAttribute("totalPageCount", totalPageCount);
		model.addAttribute("baseUrl", baseUrl);
		model.addAttribute("accounts", pages);

		return "admin/list_account";
	}

	// product list pagination begin
	@RequestMapping(value = "/admin/account", method = RequestMethod.GET)
	public String listAccount(HttpServletRequest request) {
		request.getSession().setAttribute("accountlist", null);

		return "redirect:/admin/account/page/1";
	}

	@RequestMapping(value = "/admin/account/page/{pageNumber}", method = RequestMethod.GET)
	public String showPageAccount(Model model, HttpServletRequest request, @PathVariable int pageNumber,
			Principal principal) {
		// fill admin name sign in
		String adminname = principal.getName();
		model.addAttribute("name", adminname);

		PagedListHolder<?> pages = (PagedListHolder<?>) request.getSession().getAttribute("accountlist");
		int pageSize = 6;
		List<UserInfo> users = userService.findAllUserInfo();
		if (pages == null) {
			pages = new PagedListHolder<>(users);
			pages.setPageSize(pageSize);
		} else {
			final int goToPage = pageNumber - 1;
			if (goToPage <= pages.getPageCount() && goToPage >= 0) {
				pages.setPage(goToPage);
			}
		}
		request.getSession().setAttribute("accountlist", pages);
		int current = pages.getPage() + 1;
		int begin = 1;
		int end = pages.getPageCount();
		int totalPageCount = pages.getPageCount();
		String baseUrl = "/admin/account/page/";

		model.addAttribute("beginIndex", begin);
		model.addAttribute("endIndex", end);
		model.addAttribute("currentIndex", current);
		model.addAttribute("totalPageCount", totalPageCount);
		model.addAttribute("baseUrl", baseUrl);
		model.addAttribute("accounts", pages);
		return "admin/list_account";
	}
	// product list pagination end

	// save account begin

	@RequestMapping(value = "/admin/account-save", method = RequestMethod.GET)
	public String getInfoAccount(Model model) {
		// create new user
		UserForm userForm = new UserForm();
		model.addAttribute("userForm", userForm);

		// get All roles
		List<AppRole> roles = roleService.findAll();
		model.addAttribute("roles", roles);

		return "admin/save_account";
	}

	@RequestMapping(value = "/admin/account-save", method = RequestMethod.POST)
	public String createAccount(@ModelAttribute UserForm userForm,RedirectAttributes redirect, Model model) {
		if (userForm == null) {
			return "/admin/account-save";
		}

		System.out.println("POST username: " + userForm.getRole_id());

		if (userForm != null) {

			List<AppUser> appUsers = userService.findAll();

			for (AppUser u : appUsers) {
				if (u == null) {
					return "customer/register";
				} else {
					if (userService.isDuplicateUser(model, userForm.getUserName(), u.getUserName()) == true) {
						model.addAttribute("duplicate_name", "Tên này đã được sử dụng!");
						return "redirect:/admin/account";
					}

					if (userService.isDuplicateUser(model, userForm.getEmail(), u.getEmail()) == true) {
						model.addAttribute("duplicate_email", "Email này đã được sử dụng!");
						return "redirect:/admin/account";
					}

				}
			}
			// create new user
				// 1.mapping userForm => AppUSer
			AppUser user = UserFormMapping.UserFormToAppUser(userForm);
			userService.createUser(user);
			// get id from username
			AppUser AppU = userService.findByName(user.getUserName());
			System.out.println("info user: " + AppU);
			System.out.println("info id from new user: " + AppU.getUserId());
			// add role for new user by user id
			roleService.addRoleForEmployee(AppU.getUserId(), userForm.getRole_id());	
			
		}
		return "redirect:/admin/account";

	}
	// save account end
}
