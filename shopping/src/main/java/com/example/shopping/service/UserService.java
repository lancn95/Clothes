package com.example.shopping.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import com.example.shopping.entities.AppUser;
import com.example.shopping.model.UserInfo;

@Transactional
public interface UserService {
	List<AppUser> findAll();
	
	List<UserInfo> findAllUserInfo();
	
	AppUser findByName(String name);
	
	void createUser(AppUser appUser);
	
	void updateUser(AppUser appUser);
	
	void deleteUser(AppUser appUser);
	
	void updateUserByName(String firstName, String lastName, String street,
			 String town, String email ,String phone, String userName);
	
	boolean isDuplicateUser(Model model,String s1, String s2);
	
	void saveToken (String token, String email);
	
	public void addRoleForUser(Long userId);
	
	public Long getUserIDMax();

	List<AppUser> searchByNameLike(String name);
	
	List<UserInfo> searchByNameLikeUserInfo(String name);
}
