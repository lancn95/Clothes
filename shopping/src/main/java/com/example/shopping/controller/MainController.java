package com.example.shopping.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.shopping.entities.AppUser;
import com.example.shopping.entities.Carousel;
import com.example.shopping.entities.CategoryParent;
import com.example.shopping.entities.Contact;
import com.example.shopping.entities.Product;
import com.example.shopping.form.CustomerForm;
import com.example.shopping.model.CartInfo;
import com.example.shopping.model.FilterInfo;
import com.example.shopping.model.OrderDetailInfo;
import com.example.shopping.model.OrderInfo;
import com.example.shopping.model.ProductInfo;
import com.example.shopping.service.CarouselService;
import com.example.shopping.service.CategoryParentService;
import com.example.shopping.service.ContactService;
import com.example.shopping.service.Impl.OrderServiceImpl;
import com.example.shopping.service.Impl.ProductServiceImpl;
import com.example.shopping.service.Impl.UserServiceImpl;
import com.example.shopping.utils.Utils;
import com.example.shopping.utils.WebUtils;
import com.example.shopping.validator.CustomerFormValidator;

@Controller
public class MainController {

	@Autowired
	private CarouselService carouselService;

	@Autowired
	private UserServiceImpl userServiceImpl;

	@Autowired
	private ProductServiceImpl productServiceImpl;

	@Autowired
	private OrderServiceImpl orderServiceImpl;

	@Autowired
	private CustomerFormValidator customerFormValidator;

	@Autowired
	private CategoryParentService categoryParentService;

	@Autowired
	private ContactService contactService;

	@InitBinder
	public void myInitBinder(WebDataBinder dataBinder) {
		Object target = dataBinder.getTarget();
		if (target == null) {
			return;
		}
		System.out.println("Target=" + target);

		// Trường hợp update SL trên giỏ hàng.
		// (@ModelAttribute("cartForm") @Validated CartInfo cartForm)
		if (target.getClass() == CartInfo.class) {

		}

		// Trường hợp save thông tin khách hàng.
		// (@ModelAttribute @Validated CustomerInfo customerForm)
		else if (target.getClass() == CustomerForm.class) {
			dataBinder.setValidator(customerFormValidator);
		}

	}

	@RequestMapping(value = { "/", "/welcome" }, method = RequestMethod.GET)
	public String welcomePage(Model model, HttpServletRequest request) {
		model.addAttribute("title", "Welcome");
		model.addAttribute("message", "This is welcome page!");
		// show contact ra ngoải view
		List<Contact> contacts = contactService.finđAll();
		model.addAttribute("contacts", contacts);

		// show lish ảnh bìa ra ngoải view
		List<Carousel> carousels = carouselService.findAll();
		model.addAttribute("carousels", carousels);

		// show lish danh mục cha ra ngoải view
		List<CategoryParent> list = categoryParentService.findAll();
		model.addAttribute("categoryParents", list);

		// Đẩy các thông tin từng sản phẩm trong giỏ hàng ra ngoài view
		CartInfo mycart = Utils.getInfoCartInSession(request);
		model.addAttribute("quantity", mycart.getQuantityTotal());
		model.addAttribute("total", mycart.getAmountTotal());
		model.addAttribute("cartLines", mycart.getCartLines());

		return "customer/index";
	}

	// search product begin

	@RequestMapping(value = "/search/{pageNumber}", method = RequestMethod.GET)
	public String search(@RequestParam("name") String name, Model model, HttpServletRequest request,

			@PathVariable int pageNumber, Principal principal) {
		// show contact ra ngoải view
		List<Contact> contacts = contactService.finđAll();
		model.addAttribute("contacts", contacts);

		FilterInfo filterInfo = new FilterInfo();
		model.addAttribute("filterInfo", filterInfo);

		// get all category parent to view begin
		List<CategoryParent> list = categoryParentService.findAll();

		model.addAttribute("categoryParents", list);
		// get all category parent to view end
		if (name.isEmpty()) {
			return "redirect:/product";
		}

		List<ProductInfo> products = productServiceImpl.seachByNameLike(name);
		if (products == null || products.size() < 0) {
			return "redirect:/product";
		}
		PagedListHolder<?> pages = (PagedListHolder<?>) request.getSession().getAttribute("productlis");
		int pageSize = 9;
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

		// Đẩy các thông tin từng sản phẩm trong giỏ hàng ra ngoài view
		CartInfo mycart = Utils.getInfoCartInSession(request);
		model.addAttribute("quantity", mycart.getQuantityTotal());
		model.addAttribute("total", mycart.getAmountTotal());
		model.addAttribute("cartLines", mycart.getCartLines());
		return "customer/shop";
	}
	// search product end

	@RequestMapping(value = "/product", method = RequestMethod.GET)
	public String listProduct(HttpServletRequest request, Model model) {

		request.getSession().setAttribute("productlist", null);

		return "redirect:/product/page/1";
	}

	@RequestMapping(value = "/product/page/{pageNumber}", method = RequestMethod.GET)
	public String listProductHandler(HttpServletRequest request, @PathVariable int pageNumber, Model model) {
		// show contact ra ngoải view
		List<Contact> contacts = contactService.finđAll();
		model.addAttribute("contacts", contacts);

		// filter lọc
		FilterInfo filterInfo = new FilterInfo();
		model.addAttribute("filterInfo", filterInfo);
		// danh mục cha
		List<CategoryParent> list = categoryParentService.findAll();
		model.addAttribute("categoryParents", list);

		PagedListHolder<?> pages = (PagedListHolder<?>) request.getSession().getAttribute("productlis");
		int pageSize = 9;
		List<Product> products = productServiceImpl.findAll();
		if (pages == null) {
			pages = new PagedListHolder<>(products);
			pages.setPageSize(pageSize);
		} else {
			final int goToPage = pageNumber - 1;
			if (goToPage <= pages.getPageCount() && goToPage >= 0) {
				pages.setPage(goToPage);
			}
		}
		request.getSession().setAttribute("productlis", pages);
		int current = pages.getPage() + 1; // vị trí hiện tại = số trang hiện tại + 1
		int begin = Math.max(1, current - products.size()); // 1 vi current - products.size()
		int end = Math.min(begin + 5, pages.getPageCount()); // pages.getPageCount()
		int totalPageCount = pages.getPageCount();
		String baseUrl = "/product/page/";

		model.addAttribute("beginIndex", begin);
		model.addAttribute("endIndex", end);
		model.addAttribute("currentIndex", current);
		model.addAttribute("totalPageCount", totalPageCount);
		model.addAttribute("baseUrl", baseUrl);
		model.addAttribute("products", pages);

		CartInfo mycart = Utils.getInfoCartInSession(request);
		if (mycart != null) {
			System.out.println("so luong trong gio: " + mycart.getQuantityTotal());
			model.addAttribute("quantity", mycart.getQuantityTotal());
			model.addAttribute("total", mycart.getAmountTotal());
			System.out.println("cartLine: " + mycart.getCartLines());

		}

		model.addAttribute("cartLines", mycart.getCartLines());
		return "customer/shop";
	}

	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String register(Model model) {
		// show contact ra ngoải view
		List<Contact> contacts = contactService.finđAll();
		model.addAttribute("contacts", contacts);

		AppUser appUser = new AppUser();
		model.addAttribute("appUser", appUser);

		// System.out.println("GET username: " + appUser.getUserName());
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
						model.addAttribute("duplicate_name", "Tên này đã được sử dụng!");
						return "customer/register";
					}

					if (userServiceImpl.isDuplicateUser(model, appUser.getEmail(), u.getEmail()) == true) {
						model.addAttribute("duplicate_email", "Email này đã được sử dụng!");
						return "customer/register";
					}

				}
			}
			// create new user
			userServiceImpl.createUser(appUser);
			// get id from username
			AppUser user = userServiceImpl.findByName(appUser.getUserName());
			System.out.println("info user: " + user);
			System.out.println("info id from new user: " + user.getUserId());
			// add role for new user by user id
			userServiceImpl.addRoleForUser(user.getUserId());

		}

		redirect.addFlashAttribute("success", "Bạn đã đăng ký thành công, hãy đăng nhập nhé");
		return "redirect:/login";
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginpage(Model model) {
		// show contact ra ngoải view
		List<Contact> contacts = contactService.finđAll();
		model.addAttribute("contacts", contacts);
		return "customer/login";
	}

	@RequestMapping(value = "/userAccountInfo", method = RequestMethod.GET)
	public String userAccountInfo(Model model, Principal principal) {

		return "redirect:/userInfo";
	}

	@RequestMapping(value = "/userInfo", method = RequestMethod.GET)
	public String userInfo(Model model, Principal principal) {
		// show contact ra ngoải view
		List<Contact> contacts = contactService.finđAll();
		model.addAttribute("contacts", contacts);

		// Sau khi user login thành công sẽ có principal
		String userName = principal.getName();

		// principal là 1 đối tượng sẽ lưu toàn bộ thông tin của 1 tk authentication
		// đăng nhập thành công

		System.out.println("User Name: " + userName);

		User loginedUser = (User) ((Authentication) principal).getPrincipal();

		String userInfo = WebUtils.toString(loginedUser);
		System.out.println("info: " + userInfo);
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

	// load ảnh hiện thị
	@RequestMapping(value = { "/productImage" }, method = RequestMethod.GET)
	public void productImage(HttpServletRequest request, HttpServletResponse response, Model model,
			@RequestParam("code") String code) throws IOException {
		Product product = null;
		if (code != null) {
			product = this.productServiceImpl.findProduct(code);
		}
		if (product != null && product.getImage() != null) {
			response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
			response.getOutputStream().write(product.getImage());
		}
		response.getOutputStream().close();
	}

	// don hang cua khach
	@RequestMapping(value = "/yourOrders", method = RequestMethod.GET)
	public String shoppingCartOfCustomer(Principal principal, Model model) {
		// show contact ra ngoải view
		List<Contact> contacts = contactService.finđAll();
		model.addAttribute("contacts", contacts);
		//
		if (principal.getName() == null) {
			return "redirect:/";
		}
		AppUser User = userServiceImpl.findByName(principal.getName());

		Long customerId = User.getUserId();
		if (User.getEmail() == null || User.getPhone() == null) {
			return "redirect:/";
		}
		// find order by customer id

		List<OrderInfo> orderInfos = orderServiceImpl.findOrdersByCusID(customerId);

		// OrderInfo orderInfo = new OrderInfo();
		// List<OrderDetailInfo> details2 = new ArrayList<>();
		// for (OrderInfo order : orderInfos) {
		// List<OrderDetailInfo> details =
		// orderServiceImpl.findAllOrderDetail(order.getId());
		// for (OrderDetailInfo dinfo : details) {
		// details2.add(dinfo);
		// }

		// }
		// orderInfo.setDetails(details2);

		model.addAttribute("orderInfos", orderInfos);

		return "customer/yourOrders";
	}

	@RequestMapping(value = "/orderDetail", method = RequestMethod.GET)
	private String viewOrderDetailCustomer(Model model, @RequestParam("orderId") String orderId) {
		// show contact ra ngoải view
		List<Contact> contacts = contactService.finđAll();
		model.addAttribute("contacts", contacts);
		//
		OrderInfo orderInfo = null;
		if (orderId != null) {
			orderInfo = orderServiceImpl.findOrderInfo(orderId);
		}
		if (orderInfo == null) {
			return "redirect:/";
		}
		List<OrderDetailInfo> details = orderServiceImpl.findAllOrderDetail(orderId);
		orderInfo.setDetails(details);
		model.addAttribute("orderInfo", orderInfo);

		return "customer/yourOrderDetail";
	}

}
