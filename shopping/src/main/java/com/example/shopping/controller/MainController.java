package com.example.shopping.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.shopping.entities.AppUser;
import com.example.shopping.entities.CategoryParent;
import com.example.shopping.entities.Product;
import com.example.shopping.form.CustomerForm;
import com.example.shopping.model.CartInfo;
import com.example.shopping.model.CartLineInfo;
import com.example.shopping.model.CustomerInfo;
import com.example.shopping.model.ProductInfo;
import com.example.shopping.model.UserInfo;
import com.example.shopping.service.CategoryParentService;
import com.example.shopping.service.EmailService;
import com.example.shopping.service.Impl.EmailServiceImpl;
import com.example.shopping.service.Impl.OrderServiceImpl;
import com.example.shopping.service.Impl.ProductServiceImpl;
import com.example.shopping.service.Impl.UserServiceImpl;
import com.example.shopping.utils.Utils;
import com.example.shopping.utils.WebUtils;
import com.example.shopping.validator.CustomerFormValidator;

@Controller
public class MainController {

	@Autowired
	private UserServiceImpl userServiceImpl;

	@Autowired
	private ProductServiceImpl productServiceImpl;

	@Autowired
	private OrderServiceImpl orderServiceImpl;

	@Autowired
	private EmailServiceImpl emailServiceImpl;

	@Autowired
	private EmailService emailService;

	@Autowired
	private CustomerFormValidator customerFormValidator;

	@Autowired
	private CategoryParentService categoryParentService;

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
	public String welcomePage(Model model) {
		model.addAttribute("title", "Welcome");
		model.addAttribute("message", "This is welcome page!");

		List<CategoryParent> list = categoryParentService.findAll();

		model.addAttribute("categoryParents", list);
		return "customer/index";
	}

	// test url category parent begin
	@RequestMapping(value = { "ao" }, method = RequestMethod.GET)
	public String wwelcomePage(Model model) {

		return "redirect:/product";
	}
	// test url category parent end

	// display forgotPassword page
	@RequestMapping(value = "/forgot-password", method = RequestMethod.GET)
	public String forgotPassword(Model model, AppUser user) {
		model.addAttribute("user", user);

		return "customer/forgotPassword";
	}

	// Process form submission from forgotPassword page
	@RequestMapping(value = "/forgot-password", method = RequestMethod.POST)
	public String processForgotPasswordForm(@ModelAttribute AppUser user, HttpServletRequest request, Model model) {
		System.out.println("Email user: " + user.getEmail());
		// Lookup user in database by e-mail
		AppUser u = emailServiceImpl.findByEmail(user.getEmail());

		System.out.println("Name user: " + u.getUserName());
		if (u.isEnabled() == false) {
			model.addAttribute("successMessage", "Tài khoản này đã bị khóa");
			return "customer/forgotPassword";
		}
		if (u == null) {
			model.addAttribute("errorMessage", "Địa chỉ email không tồn tại");

			return "redirect:/forgot-password";
		} else {
			// Generate random 36-character string token for reset password
			String resetToken = UUID.randomUUID().toString();
			u.setResetToken(resetToken);

			// Save token to database
			userServiceImpl.saveToken(resetToken, user.getEmail());

			// String appUrl = request.getScheme() + "://" + request.getServerName();

			// Email message
			SimpleMailMessage passwordResetEmail = new SimpleMailMessage();
			passwordResetEmail.setFrom("support@demo.com");
			passwordResetEmail.setTo(user.getEmail());
			passwordResetEmail.setSubject("Password Reset Request");
			/*
			 * passwordResetEmail.setText("To reset your password, click the link below:\n"
			 * + appUrl + "/reset?token=" + u.getResetToken());
			 */
			passwordResetEmail.setText("To reset your password, click the link below:\n" + "http://localhost:8080"
					+ "/reset?token=" + u.getResetToken());

			emailService.sendEmail(passwordResetEmail);
			// Add success message to view
			model.addAttribute("successMessage", "A password reset link has been sent to " + user.getEmail());

		}

		return "customer/forgotPassword";
	}

	@RequestMapping(value = "/reset", method = RequestMethod.GET)
	public String displayResetPassword(@RequestParam("token") String resetToken, Model model) {
		AppUser user = emailService.findByResetToken(resetToken);
		System.out.println("user: " + user.getFirstName());

		if (user != null) {
			UserInfo userInfo = new UserInfo();
			userInfo.setResetToken(resetToken);
			model.addAttribute("userInfo", userInfo);
		} else {

			model.addAttribute("errorMessage", "This is invalid password reset link");
		}

		return "customer/resetPassword";
	}

	@RequestMapping(value = "/reset", method = RequestMethod.POST)
	public String saveNewPassword(@ModelAttribute UserInfo userInfo) {
		System.out.println("reset password from user: " + userInfo.getEncrytedPassword());

		System.out.println("token userinfo: " + userInfo.getResetToken());

		String resetToken = userInfo.getResetToken();
		AppUser user = emailService.findByResetToken(resetToken);

		// update password by token

		userServiceImpl.updatePassword(userInfo.getEncrytedPassword(), userInfo.getResetToken());

		// update new token
		String newToken = UUID.randomUUID().toString();
		userServiceImpl.saveToken(newToken, user.getEmail());

		return "redirect:/login";
	}

	// search product begin

	@RequestMapping(value = "/search/{pageNumber}", method = RequestMethod.GET)
	public String search(@RequestParam("name") String name, Model model, HttpServletRequest request,

			@PathVariable int pageNumber, Principal principal) {
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
		}
		return "customer/shop";
	}

	@RequestMapping(value = "/productDetail", method = RequestMethod.GET)
	public String detailProduct(@RequestParam("code") String code, Model model) {
		Product product = null;
		if (code != null && code.length() > 0) {
			product = productServiceImpl.findProduct(code);
		}
		model.addAttribute("product", product);

		CartLineInfo cartLineInfo = new CartLineInfo();

		model.addAttribute("cartLineInfo", cartLineInfo);
		return "customer/product";
	}

	@RequestMapping(value = "/productDetail", method = RequestMethod.POST)
	public String buyProductWQ(@ModelAttribute CartLineInfo cartLineInfo, HttpServletRequest request,
			@RequestParam("code") String code) {
		Product product = null;

		if (code != null && code.length() > 0) {
			product = productServiceImpl.findProduct(code);
		}
		if (product != null) {

			CartInfo cartInfo = Utils.getInfoCartInSession(request);

			ProductInfo productInfo = new ProductInfo(product);

			cartInfo.addProduct(productInfo, cartLineInfo.getQuantity());

		}

		return "redirect:/shoppingCart";
	}

	@RequestMapping(value = "/buyProduct", method = RequestMethod.GET)
	public String buyProduct(HttpServletRequest request, Model model, @RequestParam("code") String code) {
		Product product = null;

		if (code != null && code.length() > 0) {
			product = productServiceImpl.findProduct(code);
		}
		if (product != null) {

			CartInfo cartInfo = Utils.getInfoCartInSession(request);

			ProductInfo productInfo = new ProductInfo(product);

			cartInfo.addProduct(productInfo, 1);

		}

		return "redirect:/shoppingCart";
	}

	@RequestMapping(value = "/shoppingCartRemoveProduct", method = RequestMethod.GET)
	public String shoppingCartRemoveProduct(HttpServletRequest request, Model model,
			@RequestParam("code") String code) {
		Product product = null;
		if (code != null && code.length() > 0) {
			product = productServiceImpl.findProduct(code);
		}
		if (product != null) {
			CartInfo cartInfo = Utils.getInfoCartInSession(request);

			ProductInfo productInfo = new ProductInfo(product);

			cartInfo.removeProduct(productInfo);
		}

		return "redirect:/shoppingCart";
	}

	// GET: Hiển thị giỏ hàng.
	@RequestMapping(value = "/shoppingCart", method = RequestMethod.GET)
	public String showShoppingCart(HttpServletRequest request, Model model) {
		CartInfo mycart = Utils.getInfoCartInSession(request);
		System.out.println("info cartForm in GET: " + mycart.getCartLines());

		model.addAttribute("cartForm", mycart);

		return "customer/shopping-cart";
	}

	// POST: Cập nhập số lượng cho các sản phẩm đã mua.
	@RequestMapping(value = "/shoppingCart", method = RequestMethod.POST)
	public String shoppingCartUpdateQuantity(HttpServletRequest request, Model model,
			@ModelAttribute("cartForm") CartInfo mycart) {
		System.out.println("info cartForm POST: " + mycart.getCartLines());

		CartInfo cartInfo = Utils.getInfoCartInSession(request);

		cartInfo.updateQuantity(mycart);

		return "redirect:/shoppingCart";
	}

	// GET: Nhập thông tin khách hàng.
	@RequestMapping(value = "/shoppingCartCustomer", method = RequestMethod.GET)
	public String getInfoCustomer(HttpServletRequest request, Model model) {
		CartInfo cartInfo = Utils.getInfoCartInSession(request);

		if (cartInfo.isEmpty()) {

			return "redirect:/shoppingCart";
		}

		CustomerInfo customerInfo = cartInfo.getCustomerInfo();
		CustomerForm customerForm = new CustomerForm(customerInfo);

		model.addAttribute("customerForm", customerForm);
		return "customer/shoppingCartCustomer";
	}

	// POST: Save thông tin khách hàng.
	@RequestMapping(value = "/shoppingCartCustomer", method = RequestMethod.POST)
	public String saveInfoCustomer(HttpServletRequest request,
			@ModelAttribute("customerForm") @Validated CustomerForm customerForm, BindingResult result,
			final RedirectAttributes redirect) {
		if (result.hasErrors()) {
			customerForm.setValid(false);
			return "redirect:/shoppingCartCustomer";
		}

		customerForm.setValid(true);
		CartInfo cartInfo = Utils.getInfoCartInSession(request);
		CustomerInfo customerInfo = new CustomerInfo(customerForm);
		cartInfo.setCustomerInfo(customerInfo);

		return "redirect:/shoppingCartConfirmation";
	}

	// GET: Xem lại thông tin để xác nhận.
	@RequestMapping(value = "/shoppingCartConfirmation", method = RequestMethod.GET)
	public String shoppingCartConfirmationReview(HttpServletRequest request, Model model) {
		CartInfo cartInfo = Utils.getInfoCartInSession(request);

		if (cartInfo == null || cartInfo.isEmpty()) {
			return "redirect:/shoppingCart";
		} else if (!cartInfo.isValidCustomer()) {
			return "redirect:/shoppingCartCustomer";
		}
		model.addAttribute("cartInfo", cartInfo);
		return "customer/check-out";
	}

	// POST: Gửi đơn hàng (Save).
	@RequestMapping(value = "/shoppingCartConfirmation", method = RequestMethod.POST)
	public String shoppingCartConfirmationSave(HttpServletRequest request) {
		CartInfo cartInfo = Utils.getInfoCartInSession(request);

		if (cartInfo == null || cartInfo.isEmpty()) {
			return "redirect:/shoppingCart";
		} else if (!cartInfo.isValidCustomer()) {
			return "redirect:/shoppingCartCustomer";
		}

		try {
			orderServiceImpl.saveOrder(cartInfo);

		} catch (Exception e) {
			e.printStackTrace();
			return "redirect:/shoppingCartConfirmation";
		}

		// Xóa giỏ hàng khỏi session.
		Utils.removeCartInSession(request);

		// Lưu thông tin đơn hàng cuối đã xác nhận mua.
		Utils.storeLastOrderedCartInSession(request, cartInfo);

		return "redirect:/shoppingCartFinalize";
	}

	@RequestMapping(value = "/shoppingCartFinalize", method = RequestMethod.GET)
	public String shoppingCartFinalize(HttpServletRequest request, Model model) {

		CartInfo lastOrderCart = Utils.getLastOrderCartInSession(request);

		if (lastOrderCart == null) {
			return "redirect:/shoppingCartConfirmation";
		}

		model.addAttribute("lastOrderCart", lastOrderCart);

		return "customer/shoppingCartFinalize";
	}

	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String register(Model model) {
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

		return "customer/login";
	}

	@RequestMapping(value = "/userAccountInfo", method = RequestMethod.GET)
	public String userAccountInfo(Model model, Principal principal) {

		return "redirect:/userInfo";
	}

	@RequestMapping(value = "/userInfo", method = RequestMethod.GET)
	public String userInfo(Model model, Principal principal) {

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

	/*
	 * @RequestMapping(value = { "/filter" }, method = RequestMethod.GET) public
	 * String getInfoFilter(Model model, HttpServletRequest request) { FilterInfo
	 * filter = new FilterInfo(); model.addAttribute("FilterInfo", filter); return
	 * "customer/shop"; }
	 * 
	 * @RequestMapping(value = { "/filter" }, method = RequestMethod.POST) public
	 * String Filter(@ModelAttribute FilterInfo filter, HttpServletRequest request)
	 * { System.out.println("From: " + filter.getFrom()); System.out.println("To: "
	 * + filter.getTo());
	 * 
	 * return "customer/shop"; }
	 */
}
