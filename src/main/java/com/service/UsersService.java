package com.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.dao.FriendDAO;
import com.dao.MessageDAO;
import com.dao.UsersDAO;
import com.globalException.ResourceNotFoundException;
import com.model.Comments;
import com.model.Friends;
import com.model.Groups;
import com.model.Likes;
import com.model.Messages;
import com.model.Notifications;
import com.model.Posts;
import com.model.Users;

@Service
public class UsersService {
	
	@Autowired
	UsersDAO usersDAO;
	
	@Autowired
	MessageDAO messageDAO;
	
	@Autowired
	FriendDAO friendDAO;
		
		public ResponseEntity<List<Users>> getAllUsers() {
	        return new ResponseEntity<>(usersDAO.findAll(), HttpStatus.OK);
	    }

	    public ResponseEntity<Users> getUserById(int userId) {
	        Users user = usersDAO.findById(userId)
	                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
	        return new ResponseEntity<>(user, HttpStatus.OK);
	    }

	    public ResponseEntity<List<Users>> searchUsersByUsername(String username) {
	        return new ResponseEntity<>(usersDAO.findByUserNameContaining(username), HttpStatus.OK);
	    }

	    public ResponseEntity<String> addUser(Users user) {
	        usersDAO.save(user);
	        return new ResponseEntity<>("User Record Added", HttpStatus.ACCEPTED);
	    }

	    public ResponseEntity<String> updateUser(int userId, Users userDetails) {
	        Users user = usersDAO.findById(userId)
	                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
	        user.setUserName(userDetails.getUserName());
	        user.setPassword(userDetails.getPassword());
	        user.setEmail(userDetails.getEmail());
	        user.setProfilePicture(userDetails.getProfilePicture());
	        usersDAO.save(user);
	        return new ResponseEntity<>("User Record Updated", HttpStatus.ACCEPTED);
	    }

	    public ResponseEntity<String> deleteUser(int userId) {
	        if (usersDAO.existsById(userId)) {
	            usersDAO.deleteById(userId);
	            return new ResponseEntity<>("User Record Deleted", HttpStatus.ACCEPTED);
	        } else {
	            throw new ResourceNotFoundException("User not found with id: " + userId);
	        }
	    }
	    
	    public ResponseEntity<List<Posts>> getPostsByUserId(int userId) {
	        Users user = usersDAO.findById(userId)
	                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
	        return new ResponseEntity<>(user.getPosts(), HttpStatus.OK);
	    }

	    public ResponseEntity<List<Comments>> getCommentsByUserId(int userId) {
	        Users user = usersDAO.findById(userId)
	                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
	        return new ResponseEntity<>(user.getComments(), HttpStatus.OK);
	    }

	    public ResponseEntity<List<Friends>> getFriendsByUserId(int userId) {
	        List<Friends> friends = friendDAO.findFriendsByUserId(userId);
	        return new ResponseEntity<>(friends, HttpStatus.OK);
	    }

	    public ResponseEntity<List<Friends>> getPendingFriendRequests(int userId) {
	        List<Friends> pendingRequests = friendDAO.findPendingFriendRequests(userId);
	        return new ResponseEntity<>(pendingRequests, HttpStatus.OK);
	    }

	    public ResponseEntity<String> sendFriendRequest(int userId, int friendId) {
	        Users user = usersDAO.findById(userId)
	                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
	        Users friend = usersDAO.findById(friendId)
	                .orElseThrow(() -> new ResourceNotFoundException("Friend not found with id: " + friendId));
	        Friends friendRequest = new Friends();
	        friendRequest.setUserID1(user);
	        friendRequest.setUserID2(friend);
	        friendRequest.setStatus(Friends.Status.pending);
	        friendDAO.save(friendRequest);
	        return new ResponseEntity<>("Friend Request Sent", HttpStatus.ACCEPTED);
	    }

	    public ResponseEntity<List<Messages>> getMessagesBetweenUsers(int userId, int otherUserId) {
	        List<Messages> messages = messageDAO.findMessagesBetweenUsers(userId, otherUserId);
	        return new ResponseEntity<>(messages, HttpStatus.OK);
	    }

	    public ResponseEntity<String> sendMessage(int userId, int otherUserId, Messages message) {
	        Users sender = usersDAO.findById(userId)
	                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
	        Users receiver = usersDAO.findById(otherUserId)
	                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + otherUserId));
	        message.setSender(sender);
	        message.setReceiver(receiver);
	        messageDAO.save(message);
	        return new ResponseEntity<>("Message Sent", HttpStatus.ACCEPTED);
	    }

	    public ResponseEntity<List<Likes>> getLikesByUserId(int userId) {
	        Users user = usersDAO.findById(userId)
	                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
	        return new ResponseEntity<>(user.getLikes(), HttpStatus.OK);
	    }

	    public ResponseEntity<List<Likes>> getLikesByUserPosts(int userId) {
	        Users user = usersDAO.findById(userId)
	                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
	        List<Likes> likes = new ArrayList<>();
	        for (Posts post : user.getPosts()) {
	            likes.addAll(post.getLikes());
	        }
	        return new ResponseEntity<>(likes, HttpStatus.OK);
	    }

	    public ResponseEntity<List<Notifications>> getNotificationsByUserId(int userId) {
	        Users user = usersDAO.findById(userId)
	                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
	        return new ResponseEntity<>(user.getNotifications(), HttpStatus.OK);
	    }

	    public ResponseEntity<List<Groups>> getGroupsByUserId(int userId) {
	        Users user = usersDAO.findById(userId)
	                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
	        return new ResponseEntity<>(user.getGroups(), HttpStatus.OK);
	    }

	    public ResponseEntity<List<Groups>> getUserFriendsGroups(int userId) {
	        Users user = usersDAO.findById(userId)
	                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
	        List<Groups> friendsGroups = user.getFriends().stream()
	                .flatMap(friend -> friend.getGroups().stream())
	                .distinct()
	                .collect(Collectors.toList());
	        return new ResponseEntity<>(friendsGroups, HttpStatus.OK);
	    }
		
	
//	public ResponseEntity<List<Users>> getAllUsers()
//	{
//        return new ResponseEntity<>(usersDAO.findAll(), HttpStatus.OK);
//    }
//
//    public ResponseEntity<Users> getUserById(int userId) 
//    {
//        return usersDAO.findById(userId)
//        		.map(user -> new ResponseEntity<>(user, HttpStatus.OK))
//                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
//    }
//
//    public ResponseEntity<List<Users>> searchUsersByUsername(String username) 
//    {
//        return new ResponseEntity<>(usersDAO.findByUserNameContaining(username), HttpStatus.OK);
//    }
//
//    public ResponseEntity<String> addUser(Users user) {
//        usersDAO.save(user);
//        return new ResponseEntity<>("User Record Added", HttpStatus.ACCEPTED);
//    }
//
//    public ResponseEntity<String> updateUser(int userId, Users userDetails) 
//    {
//        return usersDAO.findById(userId)
//                .map(user -> {
//                    user.setUserName(userDetails.getUserName());
//                    user.setPassword(userDetails.getPassword());
//                    user.setEmail(userDetails.getEmail());
//                    user.setProfilePicture(userDetails.getProfilePicture());
//                    usersDAO.save(user);
//                    return new ResponseEntity<>("User Record Updated", HttpStatus.ACCEPTED);
//                })
//                .orElse(new ResponseEntity<>("User Not Found", HttpStatus.NOT_FOUND));
//    }
//
//    public ResponseEntity<String> deleteUser(int userId) 
//    {
//        if (usersDAO.existsById(userId)) 
//        {
//            usersDAO.deleteById(userId);
//            return new ResponseEntity<>("User Record Deleted", HttpStatus.ACCEPTED);
//        } 
//        else 
//        {
//            return new ResponseEntity<>("User Not Found", HttpStatus.NOT_FOUND);
//        }
//    }
//    
//    public ResponseEntity<List<Posts>> getPostsByUserId(int userId) {
//        Users user = usersDAO.findById(userId).orElse(null);
//        return user != null ? new ResponseEntity<>(user.getPosts(), HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
//    }
//
//    public ResponseEntity<List<Comments>> getCommentsByUserId(int userId) {
//        Users user = usersDAO.findById(userId).orElse(null);
//        return user != null ? new ResponseEntity<>(user.getComments(), HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
//    }
//
//    public ResponseEntity<List<Friends>> getFriendsByUserId(int userId) {
//        List<Friends> friends = friendDAO.findFriendsByUserId(userId);
//        return new ResponseEntity<>(friends, HttpStatus.OK);
//    }
//
//    public ResponseEntity<List<Friends>> getPendingFriendRequests(int userId) {
//        List<Friends> pendingRequests = friendDAO.findPendingFriendRequests(userId);
//        return new ResponseEntity<>(pendingRequests, HttpStatus.OK);
//    }
//
//    public ResponseEntity<String> sendFriendRequest(int userId, int friendId) {
//        Users user = usersDAO.findById(userId).orElse(null);
//        Users friend = usersDAO.findById(friendId).orElse(null);
//        if (user != null && friend != null) {
//            Friends friendRequest = new Friends();
//            friendRequest.setUserID1(user);
//            friendRequest.setUserID2(friend);
//            friendRequest.setStatus(Friends.Status.pending);
//            friendDAO.save(friendRequest);
//            return new ResponseEntity<>("Friend Request Sent", HttpStatus.ACCEPTED);
//        } else {
//            return new ResponseEntity<>("User Not Found", HttpStatus.NOT_FOUND);
//        }
//    }
//
//    public ResponseEntity<List<Messages>> getMessagesBetweenUsers(int userId, int otherUserId) {
//        List<Messages> messages = messageDAO.findMessagesBetweenUsers(userId, otherUserId);
//        return new ResponseEntity<>(messages, HttpStatus.OK);
//    }
//
//    public ResponseEntity<String> sendMessage(int userId, int otherUserId, Messages message) {
//        Users sender = usersDAO.findById(userId).orElse(null);
//        Users receiver = usersDAO.findById(otherUserId).orElse(null);
//        if (sender != null && receiver != null) {
//            message.setSender(sender);
//            message.setReceiver(receiver);
//            messageDAO.save(message);
//            return new ResponseEntity<>("Message Sent", HttpStatus.ACCEPTED);
//        } else {
//            return new ResponseEntity<>("User Not Found", HttpStatus.NOT_FOUND);
//        }
//    }
//
//    public ResponseEntity<List<Likes>> getLikesByUserId(int userId) {
//        Users user = usersDAO.findById(userId).orElse(null);
//        return user != null ? new ResponseEntity<>(user.getLikes(), HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
//    }
//
//    public ResponseEntity<List<Likes>> getLikesByUserPosts(int userId) {
//        Users user = usersDAO.findById(userId).orElse(null);
//        if (user != null) {
//            List<Likes> likes = new ArrayList<>();
//            for (Posts post : user.getPosts()) {
//                likes.addAll(post.getLikes());
//            }
//            return new ResponseEntity<>(likes, HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }
//
//    public ResponseEntity<List<Notifications>> getNotificationsByUserId(int userId) {
//        Users user = usersDAO.findById(userId).orElse(null);
//        return user != null ? new ResponseEntity<>(user.getNotifications(), HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
//    }
//
//    public ResponseEntity<List<Groups>> getGroupsByUserId(int userId) {
//        Users user = usersDAO.findById(userId).orElse(null);
//        return user != null ? new ResponseEntity<>(user.getGroups(), HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
//    }
//    public ResponseEntity<List<Groups>> getUserFriendsGroups(int userId) {
//        Users user = usersDAO.findById(userId).orElse(null);
//        if (user != null) {
//            List<Groups> friendsGroups = user.getFriends().stream()
//                    .flatMap(friend -> friend.getGroups().stream())
//                    .distinct()
//                    .collect(Collectors.toList());
//            return new ResponseEntity<>(friendsGroups, HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }
}
