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

import com.example.shopping.entities.Category;
import com.example.shopping.entities.Product;
import com.example.shopping.form.CategoryForm;
import com.example.shopping.form.ProductForm;
import com.example.shopping.model.OrderDetailInfo;
import com.example.shopping.model.OrderInfo;
import com.example.shopping.model.ProductInfo;
import com.example.shopping.service.Impl.CategoryServiceImpl;
import com.example.shopping.service.Impl.OrderServiceImpl;
import com.example.shopping.service.Impl.ProductServiceImpl;

@Controller
@Transactional
public class AdminController {

	@Autowired
	private ProductServiceImpl productServiceImpl;

	@Autowired
	private OrderServiceImpl orderServiceImpl;

	@Autowired
	private CategoryServiceImpl categoryServiceImpl;
	
	// product list pagination begin
	@RequestMapping(value = "/admin/product", method = RequestMethod.GET)
	private String listPro(HttpServletRequest request) {
		request.getSession().setAttribute("productlist", null);

		return "redirect:/admin/product/page/1";
	}

	@RequestMapping(value = "/admin/product/page/{pageNumber}", method = RequestMethod.GET)
	private String listProduct(HttpServletRequest request, Model model, Principal principal,
			@PathVariable int pageNumber) {
		// fill admin name sign in
		String adminname = principal.getName();
		model.addAttribute("name", adminname);

		// pagination
		PagedListHolder<?> pages = (PagedListHolder<?>) request.getSession().getAttribute("productlist");
		int pagesize = 3;
		//List<Product> list = productServiceImpl.findAll();
		List<ProductInfo> list = productServiceImpl.findAllProInfo();
		if (pages == null) {
			pages = new PagedListHolder<>(list);
			pages.setPageSize(pagesize);
		} else {
			final int goToPage = pageNumber - 1;
			if (goToPage <= pages.getPageCount() && goToPage >= 0) {
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
	// product list pagination end
	
	// category list pagination begin
	@RequestMapping(value = "/admin/category", method = RequestMethod.GET)
	private String listCate(HttpServletRequest request) {
		request.getSession().setAttribute("categorylist", null);

		return "redirect:/admin/category/page/1";
	}
	

	@RequestMapping(value = "/admin/category/page/{pageNumber}", method = RequestMethod.GET)
	private String listCategory(HttpServletRequest request, @PathVariable int pageNumber, Model model) {

		// pagination
		PagedListHolder<?> pages = (PagedListHolder<?>) request.getSession().getAttribute("categorylist");
		int pageSize = 3;
		List<Category> list = categoryServiceImpl.findAll();
		if (pages == null) {
			pages = new PagedListHolder<>(list);
			pages.setPageSize(pageSize);
		} else {
			final int goToPage = pageNumber - 1;
			if (goToPage <= pages.getPageCount() && goToPage >= 0) {
				pages.setPage(goToPage);
			}
		}

		request.getSession().setAttribute("categorylist", pages);
		int current = pages.getPage() + 1;
		int begin = Math.max(1, current - list.size());
		int end = Math.min(begin + 5, pages.getPageCount());
		int totalPageCount = pages.getPageCount();
		String baseUrl = "/admin/category/page/";

		model.addAttribute("beginIndex", begin);
		model.addAttribute("endIndex", end);
		model.addAttribute("currentIndex", current);
		model.addAttribute("totalPageCount", totalPageCount);
		model.addAttribute("baseUrl", baseUrl);
		model.addAttribute("categories", pages);
		return "admin/list_category";
	}
	// category list pagination end
	
	// order list
	@RequestMapping(value = "/admin/orders", method = RequestMethod.GET)
	private String listOrder(Model model) {
		List<OrderInfo> orderInfos = orderServiceImpl.findAll();

		model.addAttribute("orders", orderInfos);
		return "admin/list_order";
	}
	
	
	// order detail 
	@RequestMapping(value = "/admin/order", method = RequestMethod.GET)
	private String viewOrderDetail(Model model, @RequestParam("orderId") String orderId) {
		OrderInfo orderInfo = null;
		if (orderId != null) {
			orderInfo = orderServiceImpl.findOrderInfo(orderId);
		}
		if (orderInfo == null) {
			return "redirect:/admin/orders";
		}
		List<OrderDetailInfo> details = orderServiceImpl.findAllOrderDetail(orderId);
		orderInfo.setDetails(details);
		model.addAttribute("orderInfo", orderInfo);

		return "admin/order_detail";
	}

	// save product begin
	@RequestMapping(value = "/admin/product-save", method = RequestMethod.GET)
	private String getInfoProduct(Model model) {
		ProductForm productForm = new ProductForm();
		model.addAttribute("productForm", productForm);

		// get list categories
		List<Category> categories = categoryServiceImpl.findAll();
		model.addAttribute("categories", categories);
		return "admin/save_product";
	}

	@RequestMapping(value = "/admin/product-save", method = RequestMethod.POST)
	private String createNewProduct(@ModelAttribute ProductForm productForm) {
		productServiceImpl.save(productForm);

		return "redirect:/admin/product";
	}
	// save product end
	
	// update product begin
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

		return "redirect:/admin/product";
	}
	// update product end
	
	// save category begin
		@RequestMapping(value = "/admin/category-save", method = RequestMethod.GET)
		private String getInfoCategory(Model model) {
			CategoryForm categoryForm = new CategoryForm();
			model.addAttribute("categoryForm", categoryForm);

			return "admin/save_category";
		}

		@RequestMapping(value = "/admin/category-save", method = RequestMethod.POST)
		private String createNewCategory(@ModelAttribute CategoryForm categoryForm) {
			System.out.println("categoryformId: " + categoryForm.getId());
			System.out.println("categoryformName: " + categoryForm.getName());
			// save category here
			categoryServiceImpl.saveCategory(categoryForm);
			return "redirect:/admin/product-save";
		}
		// save category end
	
	// update category begin
	@RequestMapping(value = "/admin/category-update", method = RequestMethod.GET)
	private String editCategory(@RequestParam("id") String id, Model model) {
		CategoryForm categoryForm = null;

		if (id != null && id.length() > 0) {
			Category category = categoryServiceImpl.findCategory(id);
			if (category != null) {
				categoryForm = new CategoryForm(category);
			}
		}

		model.addAttribute("categoryForm", categoryForm);
		return "admin/update_category";
	}

	@RequestMapping(value = "/admin/category-update", method = RequestMethod.POST)
	private String updateCategory(@ModelAttribute CategoryForm categoryForm) {

		categoryServiceImpl.update(categoryForm);
		return "redirect:/admin/category";

	}
	// update category end
	
	
	// delete category begin
	@RequestMapping(value = "/admin/category-delete", method = RequestMethod.GET)
	private String deleteCategory(@RequestParam("id") String id) {
		CategoryForm categoryForm = null;
		if (id != null && id.length() > 0) {
			Category category = categoryServiceImpl.findCategory(id);
			if (category != null) {
				categoryForm = new CategoryForm(category);
			}
		}

		categoryServiceImpl.delete(categoryForm);
		return "redirect:/admin/category";
	}
	// delete category end
	
	// delete product begin
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
		return "redirect:/admin/product";
	}
	// delete product end
	
	
}
