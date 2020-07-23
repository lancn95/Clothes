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

import com.example.shopping.entities.CategoryParent;
import com.example.shopping.model.CartInfo;
import com.example.shopping.model.FilterInfo;
import com.example.shopping.model.ProductInfo;
import com.example.shopping.service.CategoryParentService;
import com.example.shopping.service.CategoryService;
import com.example.shopping.service.ProductService;
import com.example.shopping.utils.Utils;

@Controller
public class FilterController {

	@Autowired
	private ProductService ProductService;
	
	@Autowired
	private CategoryParentService categoryParentService;

	@RequestMapping(value = "/filter/{pageNumber}", method = RequestMethod.POST)
	public String showFilter(@ModelAttribute FilterInfo filterInfo, HttpServletRequest request,
			@PathVariable int pageNumber, Principal principal, Model model) {
		// show lish danh mục cha ra ngoải view
		List<CategoryParent> list = categoryParentService.findAll();
		model.addAttribute("categoryParents", list);
		String min = filterInfo.getFrom();
		String max = filterInfo.getTo();

		if (min == null || max == null) {
			return "redirect:/product";
		}
		List<ProductInfo> productInfos = ProductService.filter(min, max);
		if (productInfos == null || productInfos.size() <= 0) {
			return "redirect:/product";
		}

		PagedListHolder<?> pages = (PagedListHolder<?>) request.getSession().getAttribute("productli");
		int pageSize = 9;
		pages = new PagedListHolder<>(productInfos);
		pages.setPageSize(pageSize);

		final int goToPage = pageNumber - 1;
		if (goToPage <= pages.getPageCount() && goToPage >= 0) {
			pages.setPage(goToPage);
		}

		request.getSession().setAttribute("productlist", pages);
		int current = pages.getPage() + 1;
		int begin = Math.max(1, current - productInfos.size());
		int end = Math.min(begin + 5, pages.getPageCount());
		int totalPageCount = pages.getPageCount();
		String baseUrl = "/admin/product/page/";

		model.addAttribute("beginIndex", begin);
		model.addAttribute("endIndex", end);
		model.addAttribute("currentIndex", current);
		model.addAttribute("totalPageCount", totalPageCount);
		model.addAttribute("baseUrl", baseUrl);
		model.addAttribute("products", pages);

		// Đẩy các thông tin từng sản phẩm trong giỏ hàng ra ngoài view
		CartInfo mycart = Utils.getInfoCartInSession(request);
		model.addAttribute("quantity", mycart.getQuantityTotal());
		model.addAttribute("total", mycart.getAmountTotal());
		model.addAttribute("cartLines", mycart.getCartLines());
		return "customer/shop";
	}
}
