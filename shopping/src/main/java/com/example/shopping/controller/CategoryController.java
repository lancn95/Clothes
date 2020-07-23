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

import com.example.shopping.entities.Category;
import com.example.shopping.form.CategoryForm;
import com.example.shopping.service.Impl.CategoryServiceImpl;

@Controller
public class CategoryController {

	@Autowired
	private CategoryServiceImpl categoryServiceImpl;

	@RequestMapping(value = "/category-search/{pageNumber}", method = RequestMethod.GET)
	public String search(@RequestParam("name") String name, Model model, HttpServletRequest request,
			@PathVariable int pageNumber, Principal principal) {
		// fill admin name sign in
		String adminname = principal.getName();
		model.addAttribute("name", adminname);

		if (name.isEmpty()) {
			return "redirect:/admin/category";
		}

		List<Category> categories = categoryServiceImpl.searchByNameLike(name);
		if (categories == null || categories.size() < 0) {
			return "redirect:/admin/category";
		}
		PagedListHolder<?> pages = (PagedListHolder<?>) request.getSession().getAttribute("categorylist");
		int pageSize = 3;
		pages = new PagedListHolder<>(categories);
		pages.setPageSize(pageSize);

		final int goToPage = pageNumber - 1;
		if (goToPage <= pages.getPageCount() && goToPage >= 0) {
			pages.setPage(goToPage);
		}

		request.getSession().setAttribute("categorylist", pages);
		int current = pages.getPage() + 1;
		int begin = Math.max(1, current - categories.size());
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

	// category list pagination begin
	@RequestMapping(value = "/admin/category", method = RequestMethod.GET)
	private String listCate(HttpServletRequest request) {
		request.getSession().setAttribute("categorylist", null);

		return "redirect:/admin/category/page/1";
	}

	@RequestMapping(value = "/admin/category/page/{pageNumber}", method = RequestMethod.GET)
	private String listCategory(HttpServletRequest request, @PathVariable int pageNumber, Model model,
			Principal principal) {
		// fill admin name sign in
		String adminname = principal.getName();
		model.addAttribute("name", adminname);
		
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

	// save category begin
	@RequestMapping(value = "/admin/category-save", method = RequestMethod.GET)
	private String getInfoCategory(Model model) {
		CategoryForm categoryForm = new CategoryForm();
		model.addAttribute("categoryForm", categoryForm);

		return "admin/save_category";
	}

	@RequestMapping(value = "/admin/category-save", method = RequestMethod.POST)
	private String createNewCategory(@ModelAttribute CategoryForm categoryForm, RedirectAttributes redirect) {

		// save category here
		categoryServiceImpl.saveCategory(categoryForm);
		redirect.addFlashAttribute("successMessage", "Tạo mới thành công");
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
	private String updateCategory(@ModelAttribute CategoryForm categoryForm, RedirectAttributes reidrect) {

		categoryServiceImpl.update(categoryForm);
		reidrect.addFlashAttribute("successMessage", "Cập nhật thành công");
		return "redirect:/admin/category";

	}
	// update category end

	// delete category begin
	@RequestMapping(value = "/admin/category-delete", method = RequestMethod.GET)
	private String deleteCategory(@RequestParam("id") String id, RedirectAttributes reidrect) {
		CategoryForm categoryForm = null;
		if (id != null && id.length() > 0) {
			Category category = categoryServiceImpl.findCategory(id);
			if (category != null) {
				categoryForm = new CategoryForm(category);
			}
		}

		categoryServiceImpl.delete(categoryForm);
		reidrect.addFlashAttribute("successMessage", "Xóa thành công");
		return "redirect:/admin/category";
	}
	// delete category end
}
