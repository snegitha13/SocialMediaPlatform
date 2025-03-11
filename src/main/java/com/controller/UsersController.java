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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.model.Comments;
import com.model.Friends;
import com.model.Groups;
import com.model.Likes;
import com.model.Messages;
import com.model.Notifications;
import com.model.Posts;
import com.model.Users;
import com.service.UsersService;

@RestController
@RequestMapping("/api/users")
@CrossOrigin("*")
public class UsersController {
	
	@Autowired
	UsersService service;
	
	@GetMapping("/all")
    public ResponseEntity<List<Users>> getAllUsers() {
        return service.getAllUsers();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Users> getUserById(@PathVariable int userId) {
        return service.getUserById(userId);
    }

    @GetMapping("/search/{username}")
    public ResponseEntity<List<Users>> searchUsersByUsername(@PathVariable String username) {
        return service.searchUsersByUsername(username);
    }

    @PostMapping
    public ResponseEntity<String> addUser(@RequestBody Users user) {
        return service.addUser(user);
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<String> updateUser(@PathVariable int userId, @RequestBody Users userDetails) {
        return service.updateUser(userId, userDetails);
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable int userId) {
        return service.deleteUser(userId);
    }
    
    @GetMapping("/{userId}/posts")
    public ResponseEntity<List<Posts>> getPostsByUserId(@PathVariable int userId) {
        return service.getPostsByUserId(userId);
    }

    @GetMapping("/{userId}/posts/comments")
    public ResponseEntity<List<Comments>> getCommentsByUserId(@PathVariable int userId) {
        return service.getCommentsByUserId(userId);
    }

    @GetMapping("/{userId}/friends")
    public ResponseEntity<List<Friends>> getFriendsByUserId(@PathVariable int userId) {
        return service.getFriendsByUserId(userId);
    }

    @GetMapping("/{userId}/friend-requests/pending")
    public ResponseEntity<List<Friends>> getPendingFriendRequests(@PathVariable int userId) {
        return service.getPendingFriendRequests(userId);
    }

    @PostMapping("/{userId}/friend-requests/send/{friendId}")
    public ResponseEntity<String> sendFriendRequest(@PathVariable int userId, @PathVariable int friendId) {
        return service.sendFriendRequest(userId, friendId);
    }

    @GetMapping("/{userId}/messages/{otherUserId}")
    public ResponseEntity<List<Messages>> getMessagesBetweenUsers(@PathVariable int userId, @PathVariable int otherUserId) {
        return service.getMessagesBetweenUsers(userId, otherUserId);
    }

    @PostMapping("/{userId}/messages/send/{otherUserId}")
    public ResponseEntity<String> sendMessage(@PathVariable int userId, @PathVariable int otherUserId, @RequestBody Messages message) {
        return service.sendMessage(userId, otherUserId, message);
    }
    
    @GetMapping("/{userId}/posts/likes")
    public ResponseEntity<List<Likes>> getLikesByUserPosts(@PathVariable int userId) {
        return service.getLikesByUserPosts(userId);
    }

    @GetMapping("/{userId}/likes")
    public ResponseEntity<List<Likes>> getLikesByUser(@PathVariable int userId) {
        return service.getLikesByUserId(userId);
    }

    @GetMapping("/{userId}/notifications")
    public ResponseEntity<List<Notifications>> getNotificationsByUserId(@PathVariable int userId) {
        return service.getNotificationsByUserId(userId);
    }

    @GetMapping("/{userId}/groups")
    public ResponseEntity<List<Groups>> getGroupsByUserId(@PathVariable int userId) {
        return service.getGroupsByUserId(userId);
    }
    
    @GetMapping("/{userId}/friends/groups")
    public ResponseEntity<List<Groups>> getUserFriendsGroups(@PathVariable int userId) {
        return service.getUserFriendsGroups(userId);
    }
}
