package com.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
 
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
 
import com.dao.UsersDAO;
import com.model.Users;
 
@Service
public class CustomUserDetailService implements UserDetailsService {
	
	@Autowired
	UsersDAO userdao;
 
	@Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = userdao.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        
        Hibernate.initialize(user.getRoles());

        List<SimpleGrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getRole_name()))
                .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(), authorities);
    }
 
}
