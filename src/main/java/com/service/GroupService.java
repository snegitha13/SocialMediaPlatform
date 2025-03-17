package com.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.dao.GroupDAO;
import com.dao.MessageDAO;
import com.dao.UsersDAO;
import com.globalException.NoDataFoundException;
import com.globalException.ResourceNotFoundException;
import com.model.Groups;
import com.model.Messages;
import com.model.Users;

@Service
public class GroupService {
	 @Autowired
	 private GroupDAO groupDAO;

     @Autowired
	 private UsersDAO userDAO;

	@Autowired
	private MessageDAO messageDAO;

	public ResponseEntity<List<Groups>> getAllGroups() {
        List<Groups> groups = groupDAO.findAll();
        if (groups.isEmpty()) {
            throw new NoDataFoundException("No groups present");
        }
        return new ResponseEntity<>(groups, HttpStatus.OK);
    }

    public ResponseEntity<Groups> getGroupById(int groupId) {
        Groups group = groupDAO.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found with id: " + groupId));
        return new ResponseEntity<>(group, HttpStatus.OK);
    }

    public ResponseEntity<Groups> createGroup(Groups group) {
        Users admin = userDAO.findById(group.getAdmin().getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Admin user not found with id: " + group.getAdmin().getUserId()));
        group.setAdmin(admin);
        groupDAO.save(group);
        return new ResponseEntity<>(group, HttpStatus.CREATED);
    }

    public ResponseEntity<Groups> updateGroup(int groupId, Groups groupDetails) {
        Groups group = groupDAO.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found with id: " + groupId));
        group.setGroupName(groupDetails.getGroupName());
        groupDAO.save(group);
        return new ResponseEntity<>(group, HttpStatus.ACCEPTED);
    }

    public ResponseEntity<Groups> deleteGroup(int groupId) {
        if (groupDAO.existsById(groupId)) {
            groupDAO.deleteById(groupId);
            return new ResponseEntity<>( HttpStatus.ACCEPTED);
        } else {
            throw new ResourceNotFoundException("Group not found with id: " + groupId);
        }
    }

    public ResponseEntity<Groups> leaveGroup(int groupId, int userId) {
        Groups group = groupDAO.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found with id: " + groupId));
        Users user = userDAO.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        group.getMembers().remove(user);
        groupDAO.save(group);
        return new ResponseEntity<>(group, HttpStatus.ACCEPTED);
    }

    public ResponseEntity<String> joinGroup(int groupId, int userId) {
        Groups group = groupDAO.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found with id: " + groupId));
        Users user = userDAO.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        group.getMembers().add(user);
        groupDAO.save(group);
        return new ResponseEntity<>("User Joined Group", HttpStatus.ACCEPTED);
    }

    public ResponseEntity<List<Groups>> getUserGroups(int userId) {
        Users user = userDAO.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        List<Groups> groups = user.getGroups();
        if (groups.isEmpty()) {
            throw new NoDataFoundException("No groups found for user with id: " + userId);
        }
        return new ResponseEntity<>(groups, HttpStatus.OK);
    }
    
    public ResponseEntity<List<Object>> getCustomMessages(long groupId)
    {
    	List<Object> messages=messageDAO.getCustomMessages(groupId);
    			if (messages.isEmpty()) {
  	            throw new NoDataFoundException("No messages found for group with id: " + groupId);
  	        }
   	        return new ResponseEntity<>(messages, HttpStatus.OK);  	  
   	 }


    public ResponseEntity<String> sendMessageToGroup(int groupId, int userId, String messageText) {
        Groups group = groupDAO.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found with id: " + groupId));
        Users sender = userDAO.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        List<Users> members = group.getMembers();
        for (Users receiver : members) {
            if (receiver.getUserId() != userId) { // To avoid sending the message to the sender
                Messages message = new Messages();
                message.setMessage_text(messageText);
                message.setSender(sender);
                message.setReceiver(receiver);
                message.setTimestamp(new Timestamp(System.currentTimeMillis()));
                messageDAO.save(message);
            }
        }

        return new ResponseEntity<>("Message Sent to Group", HttpStatus.CREATED);
    }

    public ResponseEntity<List<Users>> getGroupFriends(int groupId) {
        Groups group = groupDAO.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found with id: " + groupId));
        List<Users> groupFriends = group.getMembers().stream()
                .filter(member -> member.getFriends().contains(group.getAdmin()))
                .collect(Collectors.toList());
        if (groupFriends.isEmpty()) {
            throw new NoDataFoundException("No friends found in group with id: " + groupId);
        }
        return new ResponseEntity<>(groupFriends, HttpStatus.OK);
    }

    public ResponseEntity<List<Users>> getGroupMembers(int groupId) {
        Groups group = groupDAO.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found with id: " + groupId));
        List<Users> members = group.getMembers();
        if (members.isEmpty()) {
            throw new NoDataFoundException("No members found in group with id: " + groupId);
        }
        return new ResponseEntity<>(members, HttpStatus.OK);
    }

    public ResponseEntity<String> addGroupMember(int groupId, int userId) {
        Groups group = groupDAO.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found with id: " + groupId));
        Users user = userDAO.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        group.getMembers().add(user);
        groupDAO.save(group);
        return new ResponseEntity<>("User Added to Group", HttpStatus.ACCEPTED);
    }

    public ResponseEntity<String> removeGroupMember(int groupId, int userId) {
        Groups group = groupDAO.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found with id: " + groupId));
        Users user = userDAO.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        group.getMembers().remove(user);
        groupDAO.save(group);
        return new ResponseEntity<>("User Removed from Group", HttpStatus.ACCEPTED);
    }
}
