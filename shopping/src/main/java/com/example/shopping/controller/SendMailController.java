package com.example.shopping.controller;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.shopping.entities.AppUser;
import com.example.shopping.model.UserInfo;
import com.example.shopping.service.CategoryParentService;
import com.example.shopping.service.EmailService;
import com.example.shopping.service.Impl.EmailServiceImpl;
import com.example.shopping.service.Impl.OrderServiceImpl;
import com.example.shopping.service.Impl.ProductServiceImpl;
import com.example.shopping.service.Impl.UserServiceImpl;
import com.example.shopping.validator.CustomerFormValidator;

@Controller
public class SendMailController {

	@Autowired
	private UserServiceImpl userServiceImpl;

	@Autowired
	private EmailServiceImpl emailServiceImpl;

	@Autowired
	private EmailService emailService;

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
}
