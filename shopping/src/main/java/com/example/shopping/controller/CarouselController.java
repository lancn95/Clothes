package com.example.shopping.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import com.example.shopping.entities.Carousel;
import com.example.shopping.form.CarouselForm;
import com.example.shopping.service.CarouselService;

@Controller
public class CarouselController {

	@Autowired
	private CarouselService carouselService;

	// carousel admin list pagination begin
	@RequestMapping(value = "/admin/carousel", method = RequestMethod.GET)
	public String listCarouselAdmin(Model model, HttpServletRequest request) {

		request.getSession().setAttribute("carousellist", null);

		return "redirect:/admin/carousel/page/1";
	}

	@RequestMapping(value = "/admin/carousel/page/{pageNumber}", method = RequestMethod.GET)
	public String showListCarouselAdmin(Model model, @PathVariable int pageNumber, HttpServletRequest request,
			Principal principal) {

		// fill admin name sign in
		String adminname = principal.getName();
		model.addAttribute("name", adminname);

		// pagination
		PagedListHolder<?> pages = (PagedListHolder<?>) request.getSession().getAttribute("carousellist");
		int pageSize = 6;
		List<Carousel> list = carouselService.findAll();
		if (pages == null) {
			pages = new PagedListHolder<>(list);
			pages.setPageSize(pageSize);
		} else {
			final int goToPage = pageNumber - 1;
			if (goToPage <= pages.getPageCount() && goToPage >= 0) {
				pages.setPage(goToPage);
			}
		}

		request.getSession().setAttribute("carousellist", pages);
		int current = pages.getPage() + 1;
		int begin = Math.max(1, current - list.size());
		int end = Math.min(begin + 5, pages.getPageCount());
		int totalPageCount = pages.getPageCount();
		String baseUrl = "/admin/carousel/page/";

		model.addAttribute("beginIndex", begin);
		model.addAttribute("endIndex", end);
		model.addAttribute("currentIndex", current);
		model.addAttribute("totalPageCount", totalPageCount);
		model.addAttribute("baseUrl", baseUrl);
		model.addAttribute("carousels", pages);

		return "admin/list_carousel";
	}
	// carousel admin list pagination end
	
	//carousel save begin
	
	@RequestMapping(value = "/admin/carousel-save", method = RequestMethod.GET)
	public String getInfoCarousel(Model model) {
		CarouselForm carouselForm = new CarouselForm();
		
		model.addAttribute("carouselForm", carouselForm);
		return "admin/save_carousel";
	}
	
	@RequestMapping(value = "/admin/carousel-save", method = RequestMethod.POST)
	public String saveCarousel(@ModelAttribute CarouselForm carouselForm, RedirectAttributes redirect) {
		System.out.println("carouselForm: " + carouselForm);
		carouselService.save(carouselForm);
		
		redirect.addFlashAttribute("successMessage", "Tạo mới thành công");
		return "redirect:/admin/carousel";
	}
	
	//carousel save end 
	
	@RequestMapping(value = "carouselImage", method = RequestMethod.GET)
	public void carouselImage(HttpServletRequest request, HttpServletResponse response, Model model,
			@RequestParam("id") int id) throws IOException {
		Carousel carousel = null;
		if (id > 0) {
			carousel = carouselService.findById(id);
		}
		if (carousel != null && carousel.getImage() != null) {
			response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
			response.getOutputStream().write(carousel.getImage());
		}
		response.getOutputStream().close();
	}
}
