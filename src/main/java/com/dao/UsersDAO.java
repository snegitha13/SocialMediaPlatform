package com.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.model.Friends;
import com.model.Messages;
import com.model.Users;

@Repository
public interface UsersDAO extends JpaRepository<Users,Integer>{
	
	//Custom query to find users by username
	List<Users> findByUserNameContaining(String username);
	
	Optional<Users> findByUserName(String userName);
}
