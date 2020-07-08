package com.example.shopping.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.example.shopping.dao.EmailDAO;
import com.example.shopping.entities.AppUser;
import com.example.shopping.service.EmailService;

@Service
public class EmailServiceImpl implements EmailService {

	@Autowired
	private EmailDAO emailDAO;
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Override
	public AppUser findByEmail(String email) {
		AppUser user = emailDAO.findByEmail(email);

		return user;
	}

	@Override
	public AppUser findByResetToken(String resetToken) {
		AppUser user = emailDAO.findByResetToken(resetToken);

		return user;
	}

	@Override
	public void sendEmail(SimpleMailMessage email) {
		mailSender.send(email);
		
	}

}
