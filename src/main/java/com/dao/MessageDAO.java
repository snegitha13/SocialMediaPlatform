package com.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.model.Messages;
import com.model.Users;

@Repository
public interface MessageDAO extends JpaRepository<Messages,Integer> {
	
	@Query("SELECT m FROM Messages m WHERE m.sender.userId = :senderId AND m.receiver.userId = :receiverId")
    List<Messages> findMessagesBetweenUsers(@Param("senderId") int senderId, @Param("receiverId") int receiverId);
	
	@Query("SELECT m FROM Messages m WHERE (m.sender IN :users AND m.receiver IN :users)")
    List<Messages> findAllBySenderInAndReceiverIn(@Param("users") List<Users> users1, @Param("users") List<Users> users2);
	
	@Query(nativeQuery=true,value="SELECT m.message_text, m.receiver_id, m.senderid FROM messages m JOIN group_members gm ON m.senderid = gm.user_id JOIN groups g ON gm.group_id = g.group_id WHERE gm.group_id = :groupid AND (m.senderid IN (SELECT user_id FROM group_members WHERE group_id = :groupid)OR m.senderid = g.adminid) AND (m.receiver_id IN (SELECT user_id FROM group_members WHERE group_id = :groupid) OR m.receiver_id = g.adminid) ORDER BY m.timestamp;")
	List<Object> getCustomMessages(@Param("groupid") long groupid);
}
