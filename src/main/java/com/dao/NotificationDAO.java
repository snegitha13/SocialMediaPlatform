package com.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.model.Notifications;

@Repository
public interface NotificationDAO extends JpaRepository<Notifications,Integer>{
	 List<Notifications> findByUser_UserId(int userId);

}
