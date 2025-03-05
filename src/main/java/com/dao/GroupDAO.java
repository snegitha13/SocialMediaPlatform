package com.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.model.Groups;

@Repository
public interface GroupDAO  extends JpaRepository<Groups,Integer>{
	@Query("SELECT m.message_text, r.userId, g.groupId FROM Messages m " +
	           "JOIN m.receiver r " +
	           "JOIN r.groups g " +
	           "WHERE g.groupId = :groupId")
	    List<Object[]> getMessagesWithUserAndGroup(int groupId);
	}
