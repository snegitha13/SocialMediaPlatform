package com.dao;

import java.util.List;

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

	Users findByUserName(String username);

//	// Custom query to find friends by userId and status
//	@Query("SELECT f FROM Friends f WHERE f.userID1.userId = :userId AND f.status = 'pending'")
//	
//	List<Friends> findPendingFriendRequests(@Param("userId") int userId);
//
//	// Custom query to find messages between two users
//	@Query("SELECT m FROM Messages m WHERE (m.sender.userId = :userId AND m.receiver.userId = :otherUserId) OR (m.sender.userId = :otherUserId AND m.receiver.userId = :userId)")
//	List<Messages> findMessagesBetweenUsers(@Param("userId") int userId, @Param("otherUserId") int otherUserId);

}
