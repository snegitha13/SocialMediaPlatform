package com.service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dao.FriendDAO;
import com.dao.MessageDAO;
import com.dao.UsersDAO;
import com.globalException.NoDataFoundException;
import com.globalException.ResourceNotFoundException;
import com.model.Comments;
import com.model.Friends;
import com.model.Groups;
import com.model.Likes;
import com.model.Messages;
import com.model.Notifications;
import com.model.Posts;
import com.model.Role;
import com.model.Users;

@Service
public class UsersService {
	
	@Autowired
	UsersDAO usersDAO;
	
	@Autowired
	MessageDAO messageDAO;
	
	@Autowired
	FriendDAO friendDAO;
	
	@Autowired
    private RolesService rolesService;

    @Autowired
    private PasswordEncoder passwordEncoder;
	
	
	public ResponseEntity<?> registerUser(Users user) {
        // Encode the password before saving the user
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Create and assign roles
        Role userRole = new Role();
        userRole.setRole_name("ROLE_USER"); // Default role for new users
        rolesService.saveRole(userRole); // Save role to the database
        userRole.setUser(user);

        user.setRoles(List.of(userRole));

        try {
            usersDAO.save(user);
            return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating user");
        }
    }
	
    public ResponseEntity<?> registerAdmin(Users user) {
        // Encode the password before saving the user
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Create and assign roles
        Role adminRole = new Role();
        adminRole.setRole_name("ROLE_ADMIN"); // Role for Admin
        rolesService.saveRole(adminRole); // Save role to the database
        adminRole.setUser(user);

        user.setRoles(List.of(adminRole));

        try {
            usersDAO.save(user);
            return ResponseEntity.status(HttpStatus.CREATED).body("Admin created successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating admin");
        }
    }
    
    public ResponseEntity<List<Users>> getAllUsers() {
        List<Users> users = usersDAO.findAll();
        if (users.isEmpty()) {
            throw new NoDataFoundException("No users present");
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    public ResponseEntity<Users> getUserById(int userId) {
        Users user = usersDAO.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    public ResponseEntity<List<Users>> searchUsersByUsername(String username) {
        List<Users> users = usersDAO.findByUserNameContaining(username);
        if (users.isEmpty()) {
            throw new NoDataFoundException("No users found with username: " + username);
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
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
        List<Posts> posts = user.getPosts();
        if (posts.isEmpty()) {
            throw new NoDataFoundException("No posts found for user with id: " + userId);
        }
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    public ResponseEntity<List<Comments>> getCommentsByUserId(int userId) {
        Users user = usersDAO.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        List<Comments> comments = user.getComments();
        if (comments.isEmpty()) {
            throw new NoDataFoundException("No comments found for user with id: " + userId);
        }
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    public ResponseEntity<List<Friends>> getFriendsByUserId(int userId) {
        List<Friends> friends = friendDAO.findFriendsByUserId(userId);
        if (friends.isEmpty()) {
            throw new NoDataFoundException("No friends found for user with id: " + userId);
        }
        return new ResponseEntity<>(friends, HttpStatus.OK);
    }

    public ResponseEntity<List<Friends>> getPendingFriendRequests(int userId) {
        List<Friends> pendingRequests = friendDAO.findPendingFriendRequests(userId);
        if (pendingRequests.isEmpty()) {
            throw new NoDataFoundException("No pending friend requests for user with id: " + userId);
        }
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
        if (messages.isEmpty()) {
            throw new NoDataFoundException("No messages found between users with ids: " + userId + " and " + otherUserId);
        }
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    public ResponseEntity<String> sendMessage(int userId, int otherUserId, Messages message) {
        Users sender = usersDAO.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        Users receiver = usersDAO.findById(otherUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + otherUserId));
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setTimestamp(Timestamp.from(Instant.now()));
        messageDAO.save(message);
        return new ResponseEntity<>("Message Sent", HttpStatus.ACCEPTED);
    }

    public ResponseEntity<List<Likes>> getLikesByUserId(int userId) {
        Users user = usersDAO.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        List<Likes> likes = user.getLikes();
        if (likes.isEmpty()) {
            throw new NoDataFoundException("No likes found for user with id: " + userId);
        }
        return new ResponseEntity<>(likes, HttpStatus.OK);
    }

    public ResponseEntity<List<Likes>> getLikesByUserPosts(int userId) {
        Users user = usersDAO.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        List<Likes> likes = new ArrayList<>();
        for (Posts post : user.getPosts()) {
            likes.addAll(post.getLikes());
        }
        if (likes.isEmpty()) {
            throw new NoDataFoundException("No likes found for posts of user with id: " + userId);
        }
        return new ResponseEntity<>(likes, HttpStatus.OK);
    }

    public ResponseEntity<List<Notifications>> getNotificationsByUserId(int userId) {
        Users user = usersDAO.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        List<Notifications> notifications = user.getNotifications();
        if (notifications.isEmpty()) {
            throw new NoDataFoundException("No notifications found for user with id: " + userId);
        }
        return new ResponseEntity<>(notifications, HttpStatus.OK);
    }

    public ResponseEntity<List<Groups>> getGroupsByUserId(int userId) {
        Users user = usersDAO.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        List<Groups> groups = user.getGroups();
        if (groups.isEmpty()) {
            throw new NoDataFoundException("No groups found for user with id: " + userId);
        }
        return new ResponseEntity<>(groups, HttpStatus.OK);
    }

    public ResponseEntity<List<Groups>> getUserFriendsGroups(int userId) {
        Users user = usersDAO.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        List<Groups> friendsGroups = user.getFriends().stream()
                .flatMap(friend -> friend.getGroups().stream())
                .distinct()
                .collect(Collectors.toList());
        if (friendsGroups.isEmpty()) {
            throw new NoDataFoundException("No groups found for friends of user with id: " + userId);
        }
        return new ResponseEntity<>(friendsGroups, HttpStatus.OK);
    }
    
}
