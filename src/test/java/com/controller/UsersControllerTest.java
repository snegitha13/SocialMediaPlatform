package com.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.model.Comments;
import com.model.Friends;
import com.model.Groups;
import com.model.Likes;
import com.model.Messages;
import com.model.Notifications;
import com.model.Posts;
import com.model.Users;
import com.service.UsersService;

class UsersControllerTest {

	@InjectMocks
    private UsersController usersController;

    @Mock
    private UsersService usersService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllUsers() {
        List<Users> usersList = new ArrayList<>();
        usersList.add(new Users());
        when(usersService.getAllUsers()).thenReturn(new ResponseEntity<>(usersList, HttpStatus.OK));
        ResponseEntity<List<Users>> response = usersController.getAllUsers();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(usersList, response.getBody());
    }

    @Test
    public void testGetUserById() {
        Users user = new Users();
        when(usersService.getUserById(1)).thenReturn(new ResponseEntity<>(user, HttpStatus.OK));
        ResponseEntity<Users> response = usersController.getUserById(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    public void testSearchUsersByUsername() {
        List<Users> usersList = new ArrayList<>();
        usersList.add(new Users());
        when(usersService.searchUsersByUsername("test")).thenReturn(new ResponseEntity<>(usersList, HttpStatus.OK));
        ResponseEntity<List<Users>> response = usersController.searchUsersByUsername("test");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(usersList, response.getBody());
    }

    @Test
    public void testAddUser() {
        when(usersService.addUser(any(Users.class))).thenReturn(new ResponseEntity<>("User Record Added", HttpStatus.ACCEPTED));
        ResponseEntity<String> response = usersController.addUser(new Users());
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals("User Record Added", response.getBody());
    }

    public void testUpdateUser() {
        Users userDetails = new Users();
        when(usersService.updateUser(1, userDetails)).thenReturn(new ResponseEntity<>("User Record Updated", HttpStatus.ACCEPTED));
        ResponseEntity<String> response = usersController.updateUser(1, userDetails);
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals("User Record Updated", response.getBody());
    }
    @Test
    public void testDeleteUser() {
        when(usersService.deleteUser(1)).thenReturn(new ResponseEntity<>("User Record Deleted", HttpStatus.ACCEPTED));
        ResponseEntity<String> response = usersController.deleteUser(1);
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals("User Record Deleted", response.getBody());
    }

    @Test
    public void testGetPostsByUserId() {
        List<Posts> postsList = new ArrayList<>();
        postsList.add(new Posts());
        when(usersService.getPostsByUserId(1)).thenReturn(new ResponseEntity<>(postsList, HttpStatus.OK));
        ResponseEntity<List<Posts>> response = usersController.getPostsByUserId(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(postsList, response.getBody());
    }

    @Test
    public void testGetCommentsByUserId() {
        List<Comments> commentsList = new ArrayList<>();
        commentsList.add(new Comments());
        when(usersService.getCommentsByUserId(1)).thenReturn(new ResponseEntity<>(commentsList, HttpStatus.OK));
        ResponseEntity<List<Comments>> response = usersController.getCommentsByUserId(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(commentsList, response.getBody());
    }

    @Test
    public void testGetFriendsByUserId() {
        List<Friends> friendsList = new ArrayList<>();
        friendsList.add(new Friends());
        when(usersService.getFriendsByUserId(1)).thenReturn(new ResponseEntity<>(friendsList, HttpStatus.OK));
        ResponseEntity<List<Friends>> response = usersController.getFriendsByUserId(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(friendsList, response.getBody());
    }

    @Test
    public void testGetPendingFriendRequests() {
        List<Friends> pendingRequests = new ArrayList<>();
        pendingRequests.add(new Friends());
        when(usersService.getPendingFriendRequests(1)).thenReturn(new ResponseEntity<>(pendingRequests, HttpStatus.OK));
        ResponseEntity<List<Friends>> response = usersController.getPendingFriendRequests(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(pendingRequests, response.getBody());
    }

    @Test
    public void testSendFriendRequest() {
        when(usersService.sendFriendRequest(1, 2)).thenReturn(new ResponseEntity<>("Friend Request Sent", HttpStatus.ACCEPTED));
        ResponseEntity<String> response = usersController.sendFriendRequest(1, 2);
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals("Friend Request Sent", response.getBody());
    }

    @Test
    public void testGetMessagesBetweenUsers() {
        List<Messages> messagesList = new ArrayList<>();
        messagesList.add(new Messages());
        when(usersService.getMessagesBetweenUsers(1, 2)).thenReturn(new ResponseEntity<>(messagesList, HttpStatus.OK));
        ResponseEntity<List<Messages>> response = usersController.getMessagesBetweenUsers(1, 2);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(messagesList, response.getBody());
    }

    @Test
    public void testSendMessage() {
        Messages message = new Messages();
        when(usersService.sendMessage(1, 2, message)).thenReturn(new ResponseEntity<>("Message Sent", HttpStatus.ACCEPTED));
        ResponseEntity<String> response = usersController.sendMessage(1, 2, message);
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals("Message Sent", response.getBody());
    }

    @Test
    public void testGetLikesByUserPosts() {
        List<Likes> likesList = new ArrayList<>();
        likesList.add(new Likes());
        when(usersService.getLikesByUserPosts(1)).thenReturn(new ResponseEntity<>(likesList, HttpStatus.OK));
        ResponseEntity<List<Likes>> response = usersController.getLikesByUserPosts(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(likesList, response.getBody());
    }

    @Test
    public void testGetLikesByUser() {
        List<Likes> likesList = new ArrayList<>();
        likesList.add(new Likes());
        when(usersService.getLikesByUserId(1)).thenReturn(new ResponseEntity<>(likesList, HttpStatus.OK));
        ResponseEntity<List<Likes>> response = usersController.getLikesByUser(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(likesList, response.getBody());
    }

    @Test
    public void testGetNotificationsByUserId() {
        List<Notifications> notificationsList = new ArrayList<>();
        notificationsList.add(new Notifications());
        when(usersService.getNotificationsByUserId(1)).thenReturn(new ResponseEntity<>(notificationsList, HttpStatus.OK));
        ResponseEntity<List<Notifications>> response = usersController.getNotificationsByUserId(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(notificationsList, response.getBody());
    }

    @Test
    public void testGetGroupsByUserId() {
        List<Groups> groupsList = new ArrayList<>();
        groupsList.add(new Groups());
        when(usersService.getGroupsByUserId(1)).thenReturn(new ResponseEntity<>(groupsList, HttpStatus.OK));
        ResponseEntity<List<Groups>> response = usersController.getGroupsByUserId(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(groupsList, response.getBody());
    }

    @Test
    public void testGetUserFriendsGroups() {
        List<Groups> groupsList = new ArrayList<>();
        groupsList.add(new Groups());
        when(usersService.getUserFriendsGroups(1)).thenReturn(new ResponseEntity<>(groupsList, HttpStatus.OK));
        ResponseEntity<List<Groups>> response = usersController.getUserFriendsGroups(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(groupsList, response.getBody());
    }
}
