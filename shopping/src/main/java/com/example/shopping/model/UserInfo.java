package com.example.shopping.model;

import com.example.shopping.entities.AppUser;

public class UserInfo {
	private String encrytedPassword;
	private String resetToken;

	public UserInfo() {

	}

	public UserInfo(AppUser appUser) {
		
		this.resetToken = appUser.getResetToken();
	}

	public String getEncrytedPassword() {
		return encrytedPassword;
	}

	public void setEncrytedPassword(String encrytedPassword) {
		this.encrytedPassword = encrytedPassword;
	}

	public String getResetToken() {
		return resetToken;
	}

	public void setResetToken(String resetToken) {
		this.resetToken = resetToken;
	}

}
