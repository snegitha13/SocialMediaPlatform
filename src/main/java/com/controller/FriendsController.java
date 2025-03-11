package com.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dao.FriendDAO;
import com.model.Friends;
import com.model.Messages;
import com.service.FriendsService;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class FriendsController {
 
    @Autowired
    private FriendsService friendsService;
    @Autowired
    private FriendDAO friendsRepository;
 

    @DeleteMapping("/friends/{userId}/{friendshipId}")
    public ResponseEntity<String> removeFriend(@PathVariable int userId, @PathVariable int friendshipId) {
        if (friendsRepository.existsById(friendshipId)) {
            friendsRepository.deleteById(friendshipId);
            return new ResponseEntity<>("Friend Removed", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Friendship not found", HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/friends/{friendshipId}/messages")
    public ResponseEntity<List<Messages>> getMessages(@PathVariable int friendshipId) {
        List<Messages> messages = friendsService.getMessages(friendshipId);
        return ResponseEntity.ok(messages);
    }
    
    @PostMapping("friends/{friendshipId}/messages/send")
    public ResponseEntity<String> sendMessage(@PathVariable int friendshipId, @RequestBody Messages messageText) {
        System.out.println(messageText);
    	return friendsService.sendMessage(friendshipId, messageText.getMessage_text());
    }
}
