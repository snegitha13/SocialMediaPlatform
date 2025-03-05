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
        return new ResponseEntity<>(groups, HttpStatus.OK);
    }

    public ResponseEntity<Groups> getGroupById(int groupId) {
        Groups group = groupDAO.findById(groupId).orElse(null);
        if (group != null) {
            return new ResponseEntity<>(group, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    public ResponseEntity<String> createGroup(Groups group) {
        Users admin = userDAO.findById(group.getAdmin().getUserId()).orElse(null);
        if (admin != null) {
            group.setAdmin(admin);
            groupDAO.save(group);
            return new ResponseEntity<>("Group Created", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Admin User Not Found", HttpStatus.NOT_FOUND);
        }
    }


    public ResponseEntity<String> updateGroup(int groupId, Groups groupDetails) {
        Groups group = groupDAO.findById(groupId).orElse(null);
        if (group != null) {
            group.setGroupName(groupDetails.getGroupName());
            groupDAO.save(group);
            return new ResponseEntity<>("Group Updated", HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>("Group Not Found", HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<String> deleteGroup(int groupId) {
        if (groupDAO.existsById(groupId)) {
            groupDAO.deleteById(groupId);
            return new ResponseEntity<>("Group Deleted", HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>("Group Not Found", HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<String> leaveGroup(int groupId, int userId) {
        Groups group = groupDAO.findById(groupId).orElse(null);
        Users user = userDAO.findById(userId).orElse(null);
        if (group != null && user != null) {
            group.getMembers().remove(user);
            groupDAO.save(group);
            return new ResponseEntity<>("User Left Group", HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>("Group or User Not Found", HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<String> joinGroup(int groupId, int userId) {
        Groups group = groupDAO.findById(groupId).orElse(null);
        Users user = userDAO.findById(userId).orElse(null);
        if (group != null && user != null) {
            group.getMembers().add(user);
            groupDAO.save(group);
            return new ResponseEntity<>("User Joined Group", HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>("Group or User Not Found", HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<List<Groups>> getUserGroups(int userId) {
        Users user = userDAO.findById(userId).orElse(null);
        if (user != null) {
            return new ResponseEntity<>(user.getGroups(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<List<Object[]>> getMessagesWithUserAndGroup(int groupId) {
        List<Object[]> messages = groupDAO.getMessagesWithUserAndGroup(groupId);
        if (messages.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }
    
    public ResponseEntity<String> sendMessageToGroup(int groupId, int userId, String messageText) {
        Groups group = groupDAO.findById(groupId).orElse(null);
        Users sender = userDAO.findById(userId).orElse(null);

        if (group == null) {
            return new ResponseEntity<>("Group Not Found", HttpStatus.NOT_FOUND);
        }

        if (sender == null) {
            return new ResponseEntity<>("User Not Found", HttpStatus.NOT_FOUND);
        }

        List<Users> members = group.getMembers();
        for (Users receiver : members) {
            if (receiver.getUserId() != userId) { // To Avoid sending the message to the sender
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
        Groups group = groupDAO.findById(groupId).orElse(null);
        if (group != null) {
            List<Users> groupFriends = group.getMembers().stream()
                    .filter(member -> member.getFriends().contains(group.getAdmin()))
                    .collect(Collectors.toList());
            return new ResponseEntity<>(groupFriends, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<List<Users>> getGroupMembers(int groupId) {
        Groups group = groupDAO.findById(groupId).orElse(null);
        if (group != null) {
            return new ResponseEntity<>(group.getMembers(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<String> addGroupMember(int groupId, int userId) {
        Groups group = groupDAO.findById(groupId).orElse(null);
        Users user = userDAO.findById(userId).orElse(null);
        if (group != null && user != null) {
            group.getMembers().add(user);
            groupDAO.save(group);
            return new ResponseEntity<>("User Added to Group", HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>("Group or User Not Found", HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<String> removeGroupMember(int groupId, int userId) {
        Groups group = groupDAO.findById(groupId).orElse(null);
        Users user = userDAO.findById(userId).orElse(null);
        if (group != null && user != null) {
            group.getMembers().remove(user);
            groupDAO.save(group);
            return new ResponseEntity<>("User Removed from Group", HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>("Group or User Not Found", HttpStatus.NOT_FOUND);
        }
    }
}
