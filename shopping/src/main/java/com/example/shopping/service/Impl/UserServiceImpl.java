package com.example.shopping.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import com.example.shopping.dao.AppUserDAO;
import com.example.shopping.entities.AppUser;
import com.example.shopping.service.UserService;
import com.example.shopping.utils.EncrytedPasswordUtils;

@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	private AppUserDAO appUserDAO;
	
	@Override
	public List<AppUser> findAll() {
		List<AppUser> appUsers = appUserDAO.findAll();
		return appUsers;
	}

	@Override
	public AppUser findByName(String name) {
		AppUser appUser = appUserDAO.findUserAccount(name);
		return appUser;
	}

	@Override
	@Transactional(rollbackFor = {RuntimeException.class,Error.class})
	public void createUser(AppUser appUser) {
		AppUser user = new AppUser();
		String passwordEncode = EncrytedPasswordUtils.encrytePassword(appUser.getEncrytedPassword());
		user.setUserId(3L);
		user.setUserName(appUser.getUserName());
		user.setEmail(appUser.getEmail());
		user.setEncrytedPassword(passwordEncode);
		user.setEnabled(true);
		appUserDAO.save(user);
		
	}

	@Override
	public void updateUser(AppUser appUser) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteUser(AppUser appUser) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateUserByName(String firstName, String lastName, String street,
								 String town, String email ,String phone, String userName)
	{
		//appUserRepository.updateUserByName(firstName, lastName, street, town, email, phone, userName);
		appUserDAO.updateUserByName(firstName, lastName, street, town, email, phone, userName);
		
	}

	@Override
	public boolean isDuplicateUser(Model model, String s1, String s2) {
		if(s1.equalsIgnoreCase(s2)) {
			System.out.println("duplicate NAME");
			return true;
		}
		return false;
	}

	@Override
	public void saveToken(String token, String email) {
		appUserDAO.saveToken(token, email);
		
	}

	public void updatePassword(String password, String token) {
		appUserDAO.updatePasswordByToken(password, token);
	}

	
	

}
