package com.example.shopping.controller;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
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
import com.example.shopping.model.CartLineInfo;
import com.example.shopping.model.CustomerInfo;
import com.example.shopping.model.ProductInfo;
import com.example.shopping.service.CategoryParentService;
import com.example.shopping.service.EmailService;
import com.example.shopping.service.Impl.EmailServiceImpl;
import com.example.shopping.service.Impl.OrderServiceImpl;
import com.example.shopping.service.Impl.ProductServiceImpl;
import com.example.shopping.service.Impl.UserServiceImpl;
import com.example.shopping.utils.Utils;
import com.example.shopping.validator.CustomerFormValidator;

@Controller
public class ShoppingCartController {
	@Autowired
	private UserServiceImpl userServiceImpl;

	@Autowired
	private ProductServiceImpl productServiceImpl;

	@Autowired
	private OrderServiceImpl orderServiceImpl;

	@Autowired
	private CustomerFormValidator customerFormValidator;


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
	public String shoppingCartConfirmationSave(HttpServletRequest request, Principal principal) {

		CartInfo cartInfo = Utils.getInfoCartInSession(request);

		if (cartInfo == null || cartInfo.isEmpty()) {
			return "redirect:/shoppingCart";
		} else if (!cartInfo.isValidCustomer()) {
			return "redirect:/shoppingCartCustomer";
		}
		if (principal != null) {
			// find id customer
			AppUser u = userServiceImpl.findByName(principal.getName());
			// set customer_id vao order
			orderServiceImpl.saveOrder(cartInfo, u.getUserId());
		}
		try {
			orderServiceImpl.saveOrder(cartInfo, null);

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

}
