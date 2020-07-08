package com.example.shopping.service;

import com.example.shopping.entities.AppUser;
import org.springframework.mail.SimpleMailMessage;

public interface EmailService {
	public AppUser findByEmail(String email);
	
	public AppUser findByResetToken(String resetToken);
	
	public void sendEmail(SimpleMailMessage email);
}
