package com.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
 
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
 
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
 
import com.dao.FriendDAO;
import com.dao.MessageDAO;
import com.dao.UsersDAO;
import com.globalException.UserNotFoundException;
import com.model.Friends;
import com.model.Messages;
import com.model.Users;
 
class FriendsServiceTest {
 
    @Mock
    private FriendDAO friendsRepository;
 
    @Mock
    private UsersDAO usersRepository;
 
    @Mock
    private MessageDAO messageRepository;
 
    @InjectMocks
    private FriendsService friendsService;
 
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
 
    @Test
    void testGetFriends_UserNotFound() {
        int userId = 1;
        when(friendsRepository.findAllByUserID1_UserIdOrUserID2_UserId(userId)).thenReturn(List.of());
 
        assertThrows(UserNotFoundException.class, () -> friendsService.getFriends(userId));
    }
 
    @Test
    void testGetFriends_Success() {
        int userId = 1;
        Friends friend = new Friends();
        when(friendsRepository.findAllByUserID1_UserIdOrUserID2_UserId(userId)).thenReturn(List.of(friend));
 
        List<Friends> friendsList = friendsService.getFriends(userId);
        assertFalse(friendsList.isEmpty());
    }
 
    @Test
    void testAddFriend_Success() {
        int userId1 = 1;
        int userId2 = 2;
        Users user1 = new Users();
        Users user2 = new Users();
        when(usersRepository.findById(userId1)).thenReturn(Optional.of(user1));
        when(usersRepository.findById(userId2)).thenReturn(Optional.of(user2));
 
        Friends friend = new Friends();
        when(friendsRepository.save(any(Friends.class))).thenReturn(friend);
 
        Friends result = friendsService.addFriend(userId1, userId2);
        assertNotNull(result);
    }
 
    @Test
    void testRemoveFriend_FriendshipNotFound() {
        int friendshipId = 1;
        when(friendsRepository.existsById(friendshipId)).thenReturn(false);
 
        assertThrows(RuntimeException.class, () -> friendsService.removeFriend(friendshipId));
    }
 
    @Test
    void testRemoveFriend_Success() {
        int friendshipId = 1;
        when(friendsRepository.existsById(friendshipId)).thenReturn(true);
 
        friendsService.removeFriend(friendshipId);
        verify(friendsRepository, times(1)).deleteById(friendshipId);
    }
 
    @Test
    void testGetMessages_FriendshipNotFound() {
        int friendshipId = 1;
        when(friendsRepository.findById(friendshipId)).thenReturn(Optional.empty());
 
        assertThrows(RuntimeException.class, () -> friendsService.getMessages(friendshipId));
    }
 
    @Test
    void testGetMessages_Success() {
        int friendshipId = 1;
        Friends friendship = new Friends();
        Users user1 = new Users();
        Users user2 = new Users();
        friendship.setUserID1(user1);
        friendship.setUserID2(user2);
        when(friendsRepository.findById(friendshipId)).thenReturn(Optional.of(friendship));
 
        Messages message = new Messages();
        when(messageRepository.findAllBySenderInAndReceiverIn(anyList(), anyList())).thenReturn(List.of(message));
 
        List<Messages> messagesList = friendsService.getMessages(friendshipId);
        assertFalse(messagesList.isEmpty());
    }
 
    @Test
    void testSendMessage_FriendshipNotFound() {
        int friendshipId = 1;
        when(friendsRepository.findById(friendshipId)).thenReturn(Optional.empty());
 
        assertThrows(RuntimeException.class, () -> friendsService.sendMessage(friendshipId, "Hello"));
    }
 
    @Test
    void testSendMessage_Success() {
        int friendshipId = 1;
        Friends friendship = new Friends();
        Users user1 = new Users();
        Users user2 = new Users();
        friendship.setUserID1(user1);
        friendship.setUserID2(user2);
        when(friendsRepository.findById(friendshipId)).thenReturn(Optional.of(friendship));
 
        ResponseEntity<String> response = friendsService.sendMessage(friendshipId, "Hello");
        assertEquals("Message sent successfully", response.getBody());
    }

 
    
 
    
 
    @Test
    void testGetMessages_NoMessages() {
        int friendshipId = 1;
        Friends friendship = new Friends();
        Users user1 = new Users();
        Users user2 = new Users();
        friendship.setUserID1(user1);
        friendship.setUserID2(user2);
        when(friendsRepository.findById(friendshipId)).thenReturn(Optional.of(friendship));
        when(messageRepository.findAllBySenderInAndReceiverIn(anyList(), anyList())).thenReturn(List.of());
 
        List<Messages> messagesList = friendsService.getMessages(friendshipId);
        assertTrue(messagesList.isEmpty());
    }
 
    @Test
    void testSendMessage_EmptyMessage() {
        int friendshipId = 1;
        Friends friendship = new Friends();
        Users user1 = new Users();
        Users user2 = new Users();
        friendship.setUserID1(user1);
        friendship.setUserID2(user2);
        when(friendsRepository.findById(friendshipId)).thenReturn(Optional.of(friendship));
 
        assertThrows(IllegalArgumentException.class, () -> friendsService.sendMessage(friendshipId, ""));
    }
 
    @Test
    void testSendMessage_NullMessage() {
        int friendshipId = 1;
        Friends friendship = new Friends();
        Users user1 = new Users();
        Users user2 = new Users();
        friendship.setUserID1(user1);
        friendship.setUserID2(user2);
        when(friendsRepository.findById(friendshipId)).thenReturn(Optional.of(friendship));
 
        assertThrows(IllegalArgumentException.class, () -> friendsService.sendMessage(friendshipId, null));
    }
}