package com.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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
import com.model.Users;

class UsersServiceTest {
	@InjectMocks
    private UsersService usersService;

    @Mock
    private UsersDAO usersDAO;

    @Mock
    private MessageDAO messageDAO;

    @Mock
    private FriendDAO friendDAO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllUsers() {
        List<Users> usersList = new ArrayList<>();
        usersList.add(new Users());
        when(usersDAO.findAll()).thenReturn(usersList);

        ResponseEntity<List<Users>> response = usersService.getAllUsers();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    public void testGetUserById() {
        Users user = new Users();
        when(usersDAO.findById(1)).thenReturn(Optional.of(user));

        ResponseEntity<Users> response = usersService.getUserById(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }


    @Test
    public void testSearchUsersByUsername() {
        List<Users> usersList = new ArrayList<>();
        usersList.add(new Users());
        when(usersDAO.findByUserNameContaining("test")).thenReturn(usersList);

        ResponseEntity<List<Users>> response = usersService.searchUsersByUsername("test");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }


    @Test
    public void testAddUser() {
        Users user = new Users();
        ResponseEntity<String> response = usersService.addUser(user);
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals("User Record Added", response.getBody());
    }

    @Test
    public void testUpdateUser() {
        Users user = new Users();
        when(usersDAO.findById(1)).thenReturn(Optional.of(user));

        Users userDetails = new Users();
        userDetails.setUserName("newName");
        userDetails.setPassword("newPassword");
        userDetails.setEmail("newEmail");
        userDetails.setProfilePicture(new byte[0]);

        ResponseEntity<String> response = usersService.updateUser(1, userDetails);
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals("User Record Updated", response.getBody());
    }


    @Test
    public void testDeleteUser() {
        when(usersDAO.existsById(1)).thenReturn(true);

        ResponseEntity<String> response = usersService.deleteUser(1);
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals("User Record Deleted", response.getBody());
    }


    @Test
    public void testGetPostsByUserId() {
        Users user = new Users();
        List<Posts> postsList = new ArrayList<>();
        postsList.add(new Posts());
        user.setPosts(postsList);
        when(usersDAO.findById(1)).thenReturn(Optional.of(user));

        ResponseEntity<List<Posts>> response = usersService.getPostsByUserId(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }


    @Test
    public void testGetCommentsByUserId() {
        Users user = new Users();
        List<Comments> commentsList = new ArrayList<>();
        commentsList.add(new Comments());
        user.setComments(commentsList);
        when(usersDAO.findById(1)).thenReturn(Optional.of(user));

        ResponseEntity<List<Comments>> response = usersService.getCommentsByUserId(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    public void testGetFriendsByUserId() {
        List<Friends> friendsList = new ArrayList<>();
        friendsList.add(new Friends());
        when(friendDAO.findFriendsByUserId(1)).thenReturn(friendsList);

        ResponseEntity<List<Friends>> response = usersService.getFriendsByUserId(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    public void testGetPendingFriendRequests() {
        List<Friends> pendingRequests = new ArrayList<>();
        pendingRequests.add(new Friends());
        when(friendDAO.findPendingFriendRequests(1)).thenReturn(pendingRequests);

        ResponseEntity<List<Friends>> response = usersService.getPendingFriendRequests(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    public void testSendFriendRequest() {
        Users user = new Users();
        Users friend = new Users();
        when(usersDAO.findById(1)).thenReturn(Optional.of(user));
        when(usersDAO.findById(2)).thenReturn(Optional.of(friend));

        ResponseEntity<String> response = usersService.sendFriendRequest(1, 2);
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals("Friend Request Sent", response.getBody());
    }
    @Test
    public void testGetMessagesBetweenUsers() {
        List<Messages> messagesList = new ArrayList<>();
        messagesList.add(new Messages());
        when(messageDAO.findMessagesBetweenUsers(1, 2)).thenReturn(messagesList);

        ResponseEntity<List<Messages>> response = usersService.getMessagesBetweenUsers(1, 2);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }


    @Test
    public void testSendMessage() {
        Users sender = new Users();
        Users receiver = new Users();
        when(usersDAO.findById(1)).thenReturn(Optional.of(sender));
        when(usersDAO.findById(2)).thenReturn(Optional.of(receiver));

        Messages message = new Messages();
        ResponseEntity<String> response = usersService.sendMessage(1, 2, message);
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals("Message Sent", response.getBody());
    }

    @Test
    public void testGetLikesByUserId() {
        Users user = new Users();
        List<Likes> likesList = new ArrayList<>();
        likesList.add(new Likes());
        user.setLikes(likesList);
        when(usersDAO.findById(1)).thenReturn(Optional.of(user));

        ResponseEntity<List<Likes>> response = usersService.getLikesByUserId(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    public void testGetLikesByUserPosts() {
        Users user = new Users();
        List<Posts> postsList = new ArrayList<>();
        Posts post = new Posts();
        List<Likes> likesList = new ArrayList<>();
        likesList.add(new Likes());
        post.setLikes(likesList);
        postsList.add(post);
        user.setPosts(postsList);
        when(usersDAO.findById(1)).thenReturn(Optional.of(user));

        ResponseEntity<List<Likes>> response = usersService.getLikesByUserPosts(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    public void testGetNotificationsByUserId() {
        Users user = new Users();
        List<Notifications> notificationsList = new ArrayList<>();
        notificationsList.add(new Notifications());
        user.setNotifications(notificationsList);
        when(usersDAO.findById(1)).thenReturn(Optional.of(user));

        ResponseEntity<List<Notifications>> response = usersService.getNotificationsByUserId(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    public void testGetGroupsByUserId() {
        Users user = new Users();
        List<Groups> groupsList = new ArrayList<>();
        groupsList.add(new Groups());
        user.setGroups(groupsList);
        when(usersDAO.findById(1)).thenReturn(Optional.of(user));

        ResponseEntity<List<Groups>> response = usersService.getGroupsByUserId(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }
    
}
