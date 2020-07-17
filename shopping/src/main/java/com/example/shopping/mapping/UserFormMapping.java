package com.example.shopping.mapping;

import com.example.shopping.entities.AppUser;
import com.example.shopping.form.UserForm;

public class UserFormMapping {
	public static AppUser UserFormToAppUser(UserForm userForm) {
		if(userForm != null) {
			AppUser user = new AppUser();
			user.setUserName(userForm.getUserName());
			user.setPhone(userForm.getPhone());
			user.setEncrytedPassword(userForm.getPassword());
			user.setEmail(userForm.getEmail());
			return user;
		}
		return null;
	}
}
