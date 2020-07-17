package com.example.shopping.mapping;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.example.shopping.entities.AppUser;
import com.example.shopping.model.UserInfo;
import com.example.shopping.service.Impl.RoleServiceImpl;

@Controller
public class UserMapping {

	@Autowired
	private RoleServiceImpl roleServiceImpl;

	public UserInfo UserToInfo(AppUser appUser) {
		if (appUser != null) {
			UserInfo userInfo = new UserInfo();
			userInfo.setUserId(appUser.getUserId());
			userInfo.setUserName(appUser.getUserName());
			userInfo.setEncrytedPassword("PROTECTED");
			if (appUser.isEnabled() == true) {
				userInfo.setEnabled("Hoạt động");
			} else {
				userInfo.setEnabled("Khóa");

			}
			if (appUser.getFirstName() == null || appUser.getLastName() == null) {
				userInfo.setFullname("");
			}
			userInfo.setFullname(appUser.getLastName() + " " + appUser.getFirstName());
			userInfo.setStreet(appUser.getStreet());
			userInfo.setTown(appUser.getTown());
			userInfo.setEmail(appUser.getEmail());
			userInfo.setPhone(appUser.getPhone());
			userInfo.setResetToken("Token");
			// set roles for a user
			// 1. get roles from a user
			try {
				List<String> roleNames = roleServiceImpl.getRoleNames(appUser.getUserId());
				userInfo.setRole(roleNames);
			} catch (Exception e) {
				e.printStackTrace();
			}

			return userInfo;
		}

		return null;
	}

	public List<UserInfo> usersToInfos(List<AppUser> appUsers) {
		List<UserInfo> userInfos = new ArrayList<>();
		if (appUsers.size() > 0 && appUsers != null) {
			for (AppUser appUser : appUsers) {
				UserInfo userInfo = UserToInfo(appUser);
				userInfos.add(userInfo);
			}
		}
		return userInfos;
	}
}
