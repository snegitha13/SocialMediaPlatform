package com.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.model.Messages;

@Repository
public interface MessageDAO extends JpaRepository<Messages,Integer> {
	
	@Query("SELECT m FROM Messages m WHERE m.sender.userId = :senderId AND m.receiver.userId = :receiverId")
    List<Messages> findMessagesBetweenUsers(@Param("senderId") int senderId, @Param("receiverId") int receiverId);

}
