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
import com.example.shopping.form.ProductForm;
import com.example.shopping.model.ProductInfo;
import com.example.shopping.service.Impl.CategoryServiceImpl;
import com.example.shopping.service.Impl.ProductServiceImpl;

@Controller
public class ProductAdminController {

	@Autowired
	private ProductServiceImpl productServiceImpl;

	@Autowired
	private CategoryServiceImpl categoryServiceImpl;

	// search product begin

	@RequestMapping(value = "/product-search/{pageNumber}", method = RequestMethod.GET)
	public String search(@RequestParam("name") String name, Model model, HttpServletRequest request,

			@PathVariable int pageNumber, Principal principal) {
		// fill admin name sign in
		String adminname = principal.getName();
		model.addAttribute("name", adminname);

		if (name.isEmpty()) {
			return "redirect:/admin/product";
		}

		List<ProductInfo> products = productServiceImpl.seachByNameLike(name);
		if (products == null || products.size() < 0) {
			return "redirect:/admin/product";
		}
		PagedListHolder<?> pages = (PagedListHolder<?>) request.getSession().getAttribute("productlist");
		int pageSize = 3;
		pages = new PagedListHolder<>(products);
		pages.setPageSize(pageSize);

		final int goToPage = pageNumber - 1;
		if (goToPage <= pages.getPageCount() && goToPage >= 0) {
			pages.setPage(goToPage);
		}

		request.getSession().setAttribute("productlist", pages);
		int current = pages.getPage() + 1;
		int begin = Math.max(1, current - products.size());
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

	// search product end

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
		try {
			PagedListHolder<?> pages = (PagedListHolder<?>) request.getSession().getAttribute("productlist");
			int pagesize = 3;
			// List<Product> list = productServiceImpl.findAll();

			List<ProductInfo> lists = productServiceImpl.findAllProInfo();
			if (pages == null) {
				pages = new PagedListHolder<>(lists);
				pages.setPageSize(pagesize);
			} else {
				final int goToPage = pageNumber - 1;
				if (goToPage <= pages.getPageCount() && goToPage >= 0) {
					pages.setPage(goToPage);
				}
			}
			request.getSession().setAttribute("productlist", pages);
			int current = pages.getPage() + 1;
			int begin = 1;
			int end = pages.getPageCount();
			int totalPageCount = pages.getPageCount();
			String baseUrl = "/admin/product/page/";

			model.addAttribute("beginIndex", begin);
			model.addAttribute("endIndex", end);
			model.addAttribute("currentIndex", current);
			model.addAttribute("totalPageCount", totalPageCount);
			model.addAttribute("baseUrl", baseUrl);
			model.addAttribute("products", pages);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "admin/list_product";

	}
	// product list pagination end

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
		// get list categories
		List<Category> categories = categoryServiceImpl.findAll();
		model.addAttribute("categories", categories);
		model.addAttribute("productForm", productForm);
		return "admin/update_product";
	}

	@RequestMapping(value = "/admin/product-update", method = RequestMethod.POST)
	private String updateProduct(@ModelAttribute ProductForm productForm) {
		productServiceImpl.update(productForm);

		return "redirect:/admin/product";
	}
	// update product end

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
