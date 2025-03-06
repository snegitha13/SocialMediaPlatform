package com.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dao.RoleDAO;
import com.model.Role;

@Service
public class RolesService {
 
	@Autowired
    private RoleDAO roleDAO;

    public Role findByRoleName(String roleName) {
        return roleDAO.findByRoleName(roleName);
    }

    public void saveRole(Role role) {
        roleDAO.save(role);
    }

    public boolean existsByRoleName(String roleName) {
        return roleDAO.findByRoleName(roleName) != null;
    }
}
