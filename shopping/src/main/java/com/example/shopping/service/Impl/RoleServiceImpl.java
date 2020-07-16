package com.example.shopping.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.shopping.dao.AppRoleDAO;
import com.example.shopping.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService{
	
	@Autowired
	private AppRoleDAO appRoleDAO;
	
	@Override
	public List<String> getRoleNames(Long userId) {
		List<String> roleNames = appRoleDAO.getRoleNames(userId);
		
		return roleNames;
	}
	
}
