package com.example.shopping.controller;

import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.shopping.entities.Product;
import com.example.shopping.form.ProductForm;
import com.example.shopping.model.OrderDetailInfo;
import com.example.shopping.model.OrderInfo;
import com.example.shopping.service.Impl.OrderServiceImpl;
import com.example.shopping.service.Impl.ProductServiceImpl;

@Controller
@Transactional
public class AdminController {

	@Autowired
	private ProductServiceImpl productServiceImpl;
	
	@Autowired
	private OrderServiceImpl orderServiceImpl;
	
	@RequestMapping(value = "/admin/product", method = RequestMethod.GET)
	private String listPro(HttpServletRequest request) {
		request.getSession().setAttribute("productlist", null);
		
		return "redirect:/admin/product/page/1";
	}
	
	@RequestMapping(value = "/admin/product/page/{pageNumber}", method = RequestMethod.GET)
	private String listProduct(HttpServletRequest request, Model model, Principal principal, @PathVariable int pageNumber) {
		// fill admin name sign in 
		String adminname = principal.getName();
		model.addAttribute("name", adminname);
		
		// pagination 
		PagedListHolder<?> pages = (PagedListHolder<?>) request.getSession().getAttribute("productlist");
		int pagesize = 3;
		List<Product> list = productServiceImpl.findAll();
		if(pages == null) {
			pages = new PagedListHolder<>(list);
			pages.setPageSize(pagesize);
		}else {
			final int goToPage = pageNumber - 1;
			if(goToPage <= pages.getPageCount() && goToPage >= 0) {
				pages.setPage(goToPage);
			}
		}
		request.getSession().setAttribute("productlist", pages);
		int current = pages.getPage() + 1;
		int begin = Math.max(1, current - list.size());
		int end = Math.min(begin + 5, pages.getPageCount());
		int totalPageCount = pages.getPageCount();
		String baseUrl = "/admin/product/page/";

		model.addAttribute("beginIndex", begin);
		model.addAttribute("endIndex", end);
		model.addAttribute("currentIndex", current);
		model.addAttribute("totalPageCount", totalPageCount);
		model.addAttribute("baseUrl", baseUrl);
		model.addAttribute("products", pages);

		return "admin/list_product";
	}
	
	@RequestMapping(value = "/admin/orders", method = RequestMethod.GET)
	private String listOrder(Model model) {
		List<OrderInfo> orderInfos = orderServiceImpl.findAll();
		
		model.addAttribute("orders", orderInfos);
		return "admin/list_order";
	}
	
	@RequestMapping(value = "/admin/order", method = RequestMethod.GET)
	private String viewOrderDetail(Model model,@RequestParam("orderId") String orderId) {
		OrderInfo orderInfo = null;
		if(orderId != null) {
			orderInfo = orderServiceImpl.findOrderInfo(orderId);
		}
		if(orderInfo == null) {
			return "redirect:/admin/orders";
		}
		List<OrderDetailInfo> details = orderServiceImpl.findAllOrderDetail(orderId);
		orderInfo.setDetails(details);
		model.addAttribute("orderInfo", orderInfo);
		
		return "admin/order_detail";
	}
	
	@RequestMapping(value = "/admin/product-save", method = RequestMethod.GET)
	private String getInfoProduct(Model model) {
		ProductForm productForm = new ProductForm();
		model.addAttribute("productForm", productForm);
		return "admin/save_product";
	}

	@RequestMapping(value = "/admin/product-save", method = RequestMethod.POST)
	private String createNewProduct(@ModelAttribute ProductForm productForm) {
		productServiceImpl.save(productForm);

		return "redirect:/admin/products";
	}

	@RequestMapping(value = "/admin/product-update", method = RequestMethod.GET)
	private String editProduct(@RequestParam("code") String code, Model model) {
		System.out.println("code: " + code);
		ProductForm productForm = null;

		if (code != null && code.length() > 0) {
			Product product = productServiceImpl.findProduct(code);
			if (product != null) {
				productForm = new ProductForm(product);
			}
		}

		model.addAttribute("productForm", productForm);
		return "admin/update_product";
	}

	@RequestMapping(value = "/admin/product-update", method = RequestMethod.POST)
	private String updateProduct(@ModelAttribute ProductForm productForm) {
		productServiceImpl.update(productForm);

		return "redirect:/admin/products";
	}

	@RequestMapping(value = "/admin/product-delete", method = RequestMethod.GET)
	private String deleteProduct(@RequestParam("code") String code) {
		ProductForm productForm = null;
		if (code != null && code.length() > 0) {
			Product product = productServiceImpl.findProduct(code);
			if (product != null) {
				productForm = new ProductForm(product);
			}
		}
		productServiceImpl.delete(productForm);
		return "redirect:/admin/products";
	}
}
