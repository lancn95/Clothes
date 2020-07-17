package com.example.shopping.service;

import java.util.List;

import com.example.shopping.entities.AppRole;

public interface RoleService {
	public List<String> getRoleNames(Long userId);
	
	public List<AppRole> findAll();
	
	public void addRoleForEmployee(Long userId, Long roleId);
}
