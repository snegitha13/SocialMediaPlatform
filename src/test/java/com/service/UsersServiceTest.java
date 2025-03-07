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
    public void testGetAllUsers_Success() {
        List<Users> usersList = new ArrayList<>();
        usersList.add(new Users());
        when(usersDAO.findAll()).thenReturn(usersList);
 
        ResponseEntity<List<Users>> response = usersService.getAllUsers();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }
 
    @Test
    public void testGetAllUsers_NoDataFound() {
        when(usersDAO.findAll()).thenReturn(new ArrayList<>());
 
        assertThrows(NoDataFoundException.class, () -> {
            usersService.getAllUsers();
        });
    }
 
    @Test
    public void testGetUserById_Success() {
        Users user = new Users();
        when(usersDAO.findById(1)).thenReturn(Optional.of(user));
 
        ResponseEntity<Users> response = usersService.getUserById(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }
 
    @Test
    public void testGetUserById_ResourceNotFound() {
        when(usersDAO.findById(1)).thenReturn(Optional.empty());
 
        assertThrows(ResourceNotFoundException.class, () -> {
            usersService.getUserById(1);
        });
    }
 
    @Test
    public void testSearchUsersByUsername_Success() {
        List<Users> usersList = new ArrayList<>();
        usersList.add(new Users());
        when(usersDAO.findByUserNameContaining("test")).thenReturn(usersList);
 
        ResponseEntity<List<Users>> response = usersService.searchUsersByUsername("test");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }
 
    @Test
    public void testSearchUsersByUsername_NoDataFound() {
        when(usersDAO.findByUserNameContaining("test")).thenReturn(new ArrayList<>());
 
        assertThrows(NoDataFoundException.class, () -> {
            usersService.searchUsersByUsername("test");
        });
    }
 
    @Test
    public void testAddUser_Success() {
        Users user = new Users();
        ResponseEntity<String> response = usersService.addUser(user);
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals("User Record Added", response.getBody());
    }
 
    @Test
    public void testUpdateUser_Success() {
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
    public void testUpdateUser_ResourceNotFound() {
        when(usersDAO.findById(1)).thenReturn(Optional.empty());
 
        Users userDetails = new Users();
        assertThrows(ResourceNotFoundException.class, () -> {
            usersService.updateUser(1, userDetails);
        });
    }
 
    @Test
    public void testDeleteUser_Success() {
        when(usersDAO.existsById(1)).thenReturn(true);
 
        ResponseEntity<String> response = usersService.deleteUser(1);
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals("User Record Deleted", response.getBody());
    }
 
    @Test
    public void testDeleteUser_ResourceNotFound() {
        when(usersDAO.existsById(1)).thenReturn(false);
 
        assertThrows(ResourceNotFoundException.class, () -> {
            usersService.deleteUser(1);
        });
    }
 
    @Test
    public void testGetPostsByUserId_Success() {
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
    public void testGetPostsByUserId_NoDataFound() {
        Users user = new Users();
        user.setPosts(new ArrayList<>());
        when(usersDAO.findById(1)).thenReturn(Optional.of(user));
 
        assertThrows(NoDataFoundException.class, () -> {
            usersService.getPostsByUserId(1);
        });
    }
 
    @Test
    public void testGetCommentsByUserId_Success() {
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
    public void testGetCommentsByUserId_NoDataFound() {
        Users user = new Users();
        user.setComments(new ArrayList<>());
        when(usersDAO.findById(1)).thenReturn(Optional.of(user));
 
        assertThrows(NoDataFoundException.class, () -> {
            usersService.getCommentsByUserId(1);
        });
    }
 
    @Test
    public void testGetFriendsByUserId_Success() {
        List<Friends> friendsList = new ArrayList<>();
        friendsList.add(new Friends());
        when(friendDAO.findFriendsByUserId(1)).thenReturn(friendsList);
 
        ResponseEntity<List<Friends>> response = usersService.getFriendsByUserId(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }
 
    @Test
    public void testGetFriendsByUserId_NoDataFound() {
        when(friendDAO.findFriendsByUserId(1)).thenReturn(new ArrayList<>());
 
        assertThrows(NoDataFoundException.class, () -> {
            usersService.getFriendsByUserId(1);
        });
    }
 
    @Test
    public void testGetPendingFriendRequests_Success() {
        List<Friends> pendingRequests = new ArrayList<>();
        pendingRequests.add(new Friends());
        when(friendDAO.findPendingFriendRequests(1)).thenReturn(pendingRequests);
 
        ResponseEntity<List<Friends>> response = usersService.getPendingFriendRequests(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }
 
    @Test
    public void testGetPendingFriendRequests_NoDataFound() {
        when(friendDAO.findPendingFriendRequests(1)).thenReturn(new ArrayList<>());
 
        assertThrows(NoDataFoundException.class, () -> {
            usersService.getPendingFriendRequests(1);
        });
    }
 
    @Test
    public void testSendFriendRequest_Success() {
        Users user = new Users();
        Users friend = new Users();
        when(usersDAO.findById(1)).thenReturn(Optional.of(user));
        when(usersDAO.findById(2)).thenReturn(Optional.of(friend));
 
        ResponseEntity<String> response = usersService.sendFriendRequest(1, 2);
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals("Friend Request Sent", response.getBody());
    }
 
    @Test
    public void testSendFriendRequest_UserNotFound() {
        when(usersDAO.findById(1)).thenReturn(Optional.empty());
 
        assertThrows(ResourceNotFoundException.class, () -> {
            usersService.sendFriendRequest(1, 2);
        });
    }
 
    @Test
    public void testSendFriendRequest_FriendNotFound() {
        Users user = new Users();
        when(usersDAO.findById(1)).thenReturn(Optional.of(user));
        when(usersDAO.findById(2)).thenReturn(Optional.empty());
 
        assertThrows(ResourceNotFoundException.class, () -> {
            usersService.sendFriendRequest(1, 2);
        });
    }
    @Test
    public void testGetMessagesBetweenUsers_Success() {
        List<Messages> messagesList = new ArrayList<>();
        messagesList.add(new Messages());
        when(messageDAO.findMessagesBetweenUsers(1, 2)).thenReturn(messagesList);
 
        ResponseEntity<List<Messages>> response = usersService.getMessagesBetweenUsers(1, 2);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }
 
    @Test
    public void testGetMessagesBetweenUsers_NoDataFound() {
        when(messageDAO.findMessagesBetweenUsers(1, 2)).thenReturn(new ArrayList<>());
 
        assertThrows(NoDataFoundException.class, () -> {
            usersService.getMessagesBetweenUsers(1, 2);
        });
    }
 
    @Test
    public void testSendMessage_Success() {
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
    public void testSendMessage_SenderNotFound() {
        when(usersDAO.findById(1)).thenReturn(Optional.empty());
 
        Messages message = new Messages();
        assertThrows(ResourceNotFoundException.class, () -> {
            usersService.sendMessage(1, 2, message);
        });
    }
 
    @Test
    public void testSendMessage_ReceiverNotFound() {
        Users sender = new Users();
        when(usersDAO.findById(1)).thenReturn(Optional.of(sender));
        when(usersDAO.findById(2)).thenReturn(Optional.empty());
 
        Messages message = new Messages();
        assertThrows(ResourceNotFoundException.class, () -> {
            usersService.sendMessage(1, 2, message);
        });
    }
 
    @Test
    public void testGetLikesByUserId_Success() {
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
    public void testGetLikesByUserId_NoDataFound() {
        Users user = new Users();
        user.setLikes(new ArrayList<>());
        when(usersDAO.findById(1)).thenReturn(Optional.of(user));
 
        assertThrows(NoDataFoundException.class, () -> {
            usersService.getLikesByUserId(1);
        });
    }
 
    @Test
    public void testGetLikesByUserPosts_Success() {
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
    public void testGetLikesByUserPosts_NoDataFound() {
        Users user = new Users();
        user.setPosts(new ArrayList<>());
        when(usersDAO.findById(1)).thenReturn(Optional.of(user));
 
        assertThrows(NoDataFoundException.class, () -> {
            usersService.getLikesByUserPosts(1);
        });
    }
 
    @Test
    public void testGetNotificationsByUserId_Success() {
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
    public void testGetNotificationsByUserId_NoDataFound() {
        Users user = new Users();
        user.setNotifications(new ArrayList<>());
        when(usersDAO.findById(1)).thenReturn(Optional.of(user));
 
        assertThrows(NoDataFoundException.class, () -> {
            usersService.getNotificationsByUserId(1);
        });
    }
 
    @Test
    public void testGetGroupsByUserId_Success() {
        Users user = new Users();
        List<Groups> groupsList = new ArrayList<>();
        groupsList.add(new Groups());
        user.setGroups(groupsList);
        when(usersDAO.findById(1)).thenReturn(Optional.of(user));
 
        ResponseEntity<List<Groups>> response = usersService.getGroupsByUserId(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }
 
    @Test
    public void testGetGroupsByUserId_NoDataFound() {
        Users user = new Users();
        user.setGroups(new ArrayList<>());
        when(usersDAO.findById(1)).thenReturn(Optional.of(user));
 
        assertThrows(NoDataFoundException.class, () -> {
            usersService.getGroupsByUserId(1);
        });
    }
    @Test
    public void testGetUserFriendsGroups_UserNotFound() {
        when(usersDAO.findById(1)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> {
            usersService.getUserFriendsGroups(1);
        });
    }
 
    @Test
    public void testGetUserFriendsGroups_NoGroupsFound() {
        Users user = new Users();
        List<Friends> friendsList = new ArrayList<>();
        user.setFriends1(friendsList);
        user.setFriends2(friendsList);
        when(usersDAO.findById(1)).thenReturn(Optional.of(user));
        assertThrows(NoDataFoundException.class, () -> {
            usersService.getUserFriendsGroups(1);
        });
    }
 
 
    public void testGetUserFriendsGroups_GroupsFound() {
        Users user = new Users();
        Friends friend1 = new Friends();
        Friends friend2 = new Friends();
        Groups group1 = new Groups();
        Groups group2 = new Groups();
 
        // Mocking the groups for friends
        List<Groups> groupsList1 = new ArrayList<>();
        groupsList1.add(group1);
        Users friendUser1 = new Users();
        friendUser1.setGroups(groupsList1);
        friend1.setUserID2(friendUser1);
 
        List<Groups> groupsList2 = new ArrayList<>();
        groupsList2.add(group2);
        Users friendUser2 = new Users();
        friendUser2.setGroups(groupsList2);
        friend2.setUserID2(friendUser2);
 
        // Mocking the friends list for the user
        List<Friends> friendsList1 = new ArrayList<>();
        friendsList1.add(friend1);
        user.setFriends1(friendsList1);
 
        List<Friends> friendsList2 = new ArrayList<>();
        friendsList2.add(friend2);
        user.setFriends2(friendsList2);
 
        when(usersDAO.findById(5)).thenReturn(Optional.of(user));
        ResponseEntity<List<Groups>> response = usersService.getUserFriendsGroups(5);
 
        List<Groups> expectedGroups = Stream.concat(groupsList1.stream(), groupsList2.stream())
                                            .distinct()
                                            .collect(Collectors.toList());
 
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedGroups, response.getBody());
    }
}