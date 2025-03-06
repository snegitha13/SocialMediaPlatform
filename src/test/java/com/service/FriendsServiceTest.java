package com.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

}
