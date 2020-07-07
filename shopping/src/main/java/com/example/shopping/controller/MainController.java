package com.example.shopping.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.shopping.entities.AppUser;
import com.example.shopping.entities.Product;
import com.example.shopping.form.CustomerForm;
import com.example.shopping.model.CartInfo;
import com.example.shopping.model.CustomerInfo;
import com.example.shopping.model.ProductInfo;
import com.example.shopping.service.Impl.OrderServiceImpl;
import com.example.shopping.service.Impl.ProductServiceImpl;
import com.example.shopping.service.Impl.UserServiceImpl;
import com.example.shopping.utils.Utils;
import com.example.shopping.utils.WebUtils;

@Controller
public class MainController {

	@Autowired
	private UserServiceImpl userServiceImpl;

	@Autowired
	private ProductServiceImpl productServiceImpl;
	
	@Autowired
	private OrderServiceImpl orderServiceImpl;
	
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
		// else if (target.getClass() == CustomerForm.class) {
		// dataBinder.setValidator(customerFormValidator);
		// }

	}

	@RequestMapping(value = { "/", "/welcome" }, method = RequestMethod.GET)
	public String welcomePage(Model model) {
		model.addAttribute("title", "Welcome");
		model.addAttribute("message", "This is welcome page!");
		return "customer/index";
	}

	@RequestMapping(value = "/productList", method = RequestMethod.GET)
	public String listProductHandler(HttpServletRequest request, Model model) {
		List<Product> products = productServiceImpl.findAll();
		model.addAttribute("products", products);

		CartInfo mycart = Utils.getInfoCartInSession(request);
		if (mycart != null) {
			System.out.println("so luong trong gio: " + mycart.getQuantityTotal());
			model.addAttribute("quantity", mycart.getQuantityTotal());
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
		return "customer/product";
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

		CustomerInfo customerInfo = cartInfo.getCustomerInfo();
		CustomerForm customerForm = new CustomerForm(customerInfo);

		model.addAttribute("customerForm", customerForm);
		return "customer/shoppingCartCustomer";
	}

	// POST: Save thông tin khách hàng.
	@RequestMapping(value = "/shoppingCartCustomer", method = RequestMethod.POST)
	public String saveInfoCustomer(HttpServletRequest request,
			@ModelAttribute("customerForm") CustomerForm customerForm, BindingResult result,
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
			
		}catch(Exception e) {
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
		
		if(lastOrderCart == null) {
			return "redirect:/shoppingCartConfirmation";
		}
		
		model.addAttribute("lastOrderCart", lastOrderCart);
		
		return "customer/shoppingCartFinalize";
	}
	
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String register(Model model) {
		AppUser appUser = new AppUser();
		model.addAttribute("appUser", appUser);

		System.out.println("GET username: " + appUser.getUserName());
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
						model.addAttribute("duplicate_name");
						return "customer/register";
					}

					if (userServiceImpl.isDuplicateUser(model, appUser.getEmail(), u.getEmail()) == true) {
						model.addAttribute("duplicate_email", "Email này đã được sử dụng!");
						return "customer/register";
					}

				}
			}
			userServiceImpl.createUser(appUser);

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

	@RequestMapping(value = "/admin", method = RequestMethod.GET)
	public String adminPage(Model model, Principal principal) {

		User loginedUser = (User) ((Authentication) principal).getPrincipal();
		System.out.println("useradminname: " + principal.getName());

		String userInfo = WebUtils.toString(loginedUser);
		String adminname = principal.getName();
		model.addAttribute("userInfo", userInfo);
		model.addAttribute("name", adminname);
		return "admin/index";
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
	
	@RequestMapping(value = { "/blog" }, method = RequestMethod.GET)
	public String blog(HttpServletRequest request, HttpServletResponse response, Model model) {
		
		return "customer/blog";
	}
	
	@RequestMapping(value = { "/blogDetail" }, method = RequestMethod.GET)
	public String blogDetail(HttpServletRequest request, HttpServletResponse response, Model model) {
		
		return "customer/blog-details";
	}
}
