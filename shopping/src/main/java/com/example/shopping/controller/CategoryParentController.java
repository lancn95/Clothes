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
import com.example.shopping.entities.CategoryParent;
import com.example.shopping.form.CategoryForm;
import com.example.shopping.form.CategoryParentForm;
import com.example.shopping.service.CategoryParentService;

@Controller
public class CategoryParentController {
	
	@Autowired
	private CategoryParentService categoryParentService;
	
	@RequestMapping(value = "/categoryParent-search/{pageNumber}", method = RequestMethod.GET)
	public String search(@RequestParam("name") String name, Model model, HttpServletRequest request,
			@PathVariable int pageNumber, Principal principal) {
		// fill admin name sign in
		String adminname = principal.getName();
		model.addAttribute("name", adminname);

		if (name.isEmpty()) {
			return "redirect:/admin/categoryParent";
		}

		List<CategoryParent> categoryParents = categoryParentService.searchByNameLike(name);
		if (categoryParents == null || categoryParents.size() < 0) {
			return "redirect:/admin/category";
		}
		PagedListHolder<?> pages = (PagedListHolder<?>) request.getSession().getAttribute("categoryParentlist");
		int pageSize = 6;
		pages = new PagedListHolder<>(categoryParents);
		pages.setPageSize(pageSize);

		final int goToPage = pageNumber - 1;
		if (goToPage <= pages.getPageCount() && goToPage >= 0) {
			pages.setPage(goToPage);
		}

		request.getSession().setAttribute("categorylist", pages);
		int current = pages.getPage() + 1;
		int begin = Math.max(1, current - categoryParents.size());
		int end = Math.min(begin + 5, pages.getPageCount());
		int totalPageCount = pages.getPageCount();
		String baseUrl = "/admin/categoryParent/page/";

		model.addAttribute("beginIndex", begin);
		model.addAttribute("endIndex", end);
		model.addAttribute("currentIndex", current);
		model.addAttribute("totalPageCount", totalPageCount);
		model.addAttribute("baseUrl", baseUrl);
		model.addAttribute("categoriesParent", pages);

		return "admin/list_categoryParent";
	}
	
	// category parent list pagination begin
		@RequestMapping(value = "/admin/categoryParent", method = RequestMethod.GET)
		private String listCateParent(HttpServletRequest request) {
			request.getSession().setAttribute("categoryParentlist", null);

			return "redirect:/admin/categoryParent/page/1";
		}

		@RequestMapping(value = "/admin/categoryParent/page/{pageNumber}", method = RequestMethod.GET)
		private String listCategoryParent(HttpServletRequest request, @PathVariable int pageNumber, Model model,
				Principal principal) {
			// fill admin name sign in
			String adminname = principal.getName();
			model.addAttribute("name", adminname);
			
			// pagination
			PagedListHolder<?> pages = (PagedListHolder<?>) request.getSession().getAttribute("categoryParentlist");
			int pageSize = 6;
			List<CategoryParent> list = categoryParentService.findAll();
			if (pages == null) {
				pages = new PagedListHolder<>(list);
				pages.setPageSize(pageSize);
			} else {
				final int goToPage = pageNumber - 1;
				if (goToPage <= pages.getPageCount() && goToPage >= 0) {
					pages.setPage(goToPage);
				}
			}

			request.getSession().setAttribute("categoryParentlist", pages);
			int current = pages.getPage() + 1;
			int begin = Math.max(1, current - list.size());
			int end = Math.min(begin + 5, pages.getPageCount());
			int totalPageCount = pages.getPageCount();
			String baseUrl = "/admin/categoryParent/page/";

			model.addAttribute("beginIndex", begin);
			model.addAttribute("endIndex", end);
			model.addAttribute("currentIndex", current);
			model.addAttribute("totalPageCount", totalPageCount);
			model.addAttribute("baseUrl", baseUrl);
			model.addAttribute("categoriesParent", pages);
			return "admin/list_categoryParent";
		}
		// category parent list pagination end

		// save category parent begin
		@RequestMapping(value = "/admin/categoryParent-save", method = RequestMethod.GET)
		private String getInfoCategoryparent(Model model) {
			CategoryParentForm categoryParentForm = new CategoryParentForm();
			model.addAttribute("categoryParentForm", categoryParentForm);

			return "admin/save_categoryParent";
		}

		@RequestMapping(value = "/admin/categoryParent-save", method = RequestMethod.POST)
		private String createNewCategoryParent(@ModelAttribute CategoryParentForm categoryParentForm, RedirectAttributes reidrect) {

			// save category parent here
			categoryParentService.create(categoryParentForm);
			reidrect.addFlashAttribute("successMessage", "Tạo mới thành công");
			return "redirect:/admin/categoryParent";
		}
		// save category end

		// update category begin
		@RequestMapping(value = "/admin/categoryParent-update", method = RequestMethod.GET)
		private String editCategoryParent(@RequestParam("id") int id, Model model) {
			CategoryParentForm categoryParentForm = null;

			if (id > 0) {
				CategoryParent categoryParent = categoryParentService.findById(id);
				if (categoryParent != null) {
					categoryParentForm = new CategoryParentForm(categoryParent);
				}
			}

			model.addAttribute("categoryParentForm", categoryParentForm);
			return "admin/update_categoryParent";
		}

		@RequestMapping(value = "/admin/categoryParent-update", method = RequestMethod.POST)
		private String updateCategoryParent(@ModelAttribute CategoryParentForm categoryParentForm, RedirectAttributes reidrect) {

			categoryParentService.update(categoryParentForm);
			reidrect.addFlashAttribute("successMessage", "Cập nhật thành công");
			return "redirect:/admin/categoryParent";

		}
		// update category end

		// delete category begin
		@RequestMapping(value = "/admin/categoryParent-delete", method = RequestMethod.GET)
		private String deleteCategoryParent(@RequestParam("id") int id, RedirectAttributes reidrect) {
			CategoryParentForm categoryParentForm = null;
			if (id > 0) {
				CategoryParent categoryParent = categoryParentService.findById(id);
				if (categoryParent != null) {
					categoryParentForm = new CategoryParentForm(categoryParent);
				}
			}

			categoryParentService.delete(categoryParentForm);
			reidrect.addFlashAttribute("successMessage", "Xóa thành công");
			return "redirect:/admin/categoryParent";
		}
		// delete category end
}
