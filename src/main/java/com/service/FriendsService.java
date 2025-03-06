package com.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.dao.FriendDAO;
import com.dao.MessageDAO;
import com.dao.UsersDAO;
import com.globalException.UserNotFoundException;
import com.model.Friends;
import com.model.Messages;
import com.model.Users;
 
import java.sql.Timestamp;
import java.util.List;
 
@Service
public class FriendsService {
 
    @Autowired
    private FriendDAO friendsRepository;
 
    @Autowired
    private UsersDAO usersRepository;
 
    @Autowired
    private MessageDAO messageRepository;
 
    public ResponseEntity<?> removeFriend(int friendshipId) {
    	System.out.println("removal invoked");
        if (!friendsRepository.existsById(friendshipId)) {
            throw new RuntimeException("Friendship not found");
        } else {
            friendsRepository.deleteById(friendshipId);
            return new ResponseEntity<>("Friend Removed", HttpStatus.OK);
        }
    }

 
    public List<Messages> getMessages(int friendshipId) {
        Friends friendship = friendsRepository.findById(friendshipId).orElseThrow(() -> new RuntimeException("Friendship not found"));
        return messageRepository.findAllBySenderInAndReceiverIn(
            List.of(friendship.getUserID1(), friendship.getUserID2()),
            List.of(friendship.getUserID1(), friendship.getUserID2())
        );
    }
 
    public ResponseEntity<String> sendMessage(int friendshipId, String messageText) {
        Friends friendship = friendsRepository.findById(friendshipId).orElseThrow(() -> new RuntimeException("Friendship not found"));
        Messages message = new Messages();
        message.setSender(friendship.getUserID1());
        message.setReceiver(friendship.getUserID2());
        message.setMessage_text(messageText);
        message.setTimestamp(new Timestamp(System.currentTimeMillis()));
        messageRepository.save(message);
        return ResponseEntity.ok("Message sent successfully");
    }
}