package com.example.shopping.controller;

import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.shopping.entities.AppUser;
import com.example.shopping.model.UserInfo;
import com.example.shopping.service.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService userService;

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
			final int goToPage = pageNumber;
			if (goToPage <= pages.getPageCount() && goToPage >= 0) {
				pages.setPage(goToPage);
			}
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
	// product list pagination end
}
