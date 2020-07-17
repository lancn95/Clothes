package com.example.shopping.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.shopping.dao.AppRoleDAO;
import com.example.shopping.dao.UserRoleDAO;
import com.example.shopping.entities.AppRole;
import com.example.shopping.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {

	@Autowired
	private AppRoleDAO appRoleDAO;

	@Autowired
	private UserRoleDAO userRoleDAO;

	@Override
	public List<String> getRoleNames(Long userId) {
		List<String> roleNames = appRoleDAO.getRoleNames(userId);

		return roleNames;
	}

	@Override
	public List<AppRole> findAll() {
		List<AppRole> roles = appRoleDAO.findAll();

		return roles;
	}

	@Override
	public void addRoleForEmployee(Long userId, Long roleId) {
		userRoleDAO.addRoleForEmployee(userId, roleId);

	}

}
