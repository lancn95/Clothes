package com.example.shopping.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.shopping.dao.AppRoleDAO;
import com.example.shopping.dao.AppUserDAO;
import com.example.shopping.entities.AppUser;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private AppUserDAO appUserDAO;

	@Autowired
	private AppRoleDAO appRoleDAO;

	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		AppUser appUser = appUserDAO.findUserAccount(userName);

		if (appUser == null) {
			System.out.println("User not found! " + userName);
			throw new UsernameNotFoundException("User" + userName + " was not found in the database");
		}

		System.out.println("Found User: " + appUser);
		// [ROLE_USER, ROLE_ADMIN,..]
		List<String> roleNames = appRoleDAO.getRoleNames(appUser.getUserId());
		System.out.println("roleNames: " + roleNames);

		List<GrantedAuthority> grantList = new ArrayList<GrantedAuthority>();
		if (roleNames != null) {
			for (String role : roleNames) {
				// ROLE_USER, ROLE_ADMIN
				GrantedAuthority authority = new SimpleGrantedAuthority(role);
				grantList.add(authority);
			}
		}
		
		boolean enabled = appUser.isEnabled();
		boolean accountNonExpired = true;
		boolean credentialsNonExpired = true;
		boolean accountNonLocked = true;
		
		UserDetails userDetails = (UserDetails) new User(appUser.getUserName(), appUser.getEncrytedPassword(),
				enabled, accountNonExpired, //
                credentialsNonExpired, accountNonLocked,grantList);

		return userDetails;

	}
}
