package com.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.model.Role;
import com.model.Users;
import com.service.RolesService;
import com.service.UsersService;

@RestController
@CrossOrigin("*")
@RequestMapping("/api")
public class UserauthController {
	
	 @Autowired
	 private UsersService userService;

	 @PostMapping("/user/register")
	    public ResponseEntity<?> registerUser(@RequestBody Users user) {
	        return userService.registerUser(user);
	    }

	    @PostMapping("/admin/register")
	    public ResponseEntity<?> registerAdmin(@RequestBody Users user) {
	        return userService.registerAdmin(user);
	    }
}
