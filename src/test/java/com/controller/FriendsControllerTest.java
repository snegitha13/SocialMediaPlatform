package com.controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
 
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.dao.FriendDAO;
import com.model.Friends;
import com.model.Messages;
import com.model.Users;
import com.service.FriendsService;
 
import java.util.Arrays;
import java.util.List;
 
public class FriendsControllerTest {
		@Mock
		private FriendDAO friendsRepository;
	 
	    @Mock
	    private FriendsService friendsService;
	 
	    @InjectMocks
	    private FriendsController friendsController;
	 
	    @BeforeEach
	    public void setUp() {
	        MockitoAnnotations.openMocks(this);
	    }
	 
	    @Test
	    public void testRemoveFriend() {
 
	    	int friendshipId = 2;
	    	when(friendsRepository.existsById(friendshipId)).thenReturn(true);
	    	doNothing().when(friendsRepository).deleteById(friendshipId);
	    	ResponseEntity<?> response = friendsController.removeFriend(1, friendshipId);
	    	assertNotNull(response);
	    	assertEquals(HttpStatus.OK, response.getStatusCode());
	    	assertEquals("Friend Removed", response.getBody());
	    	verify(friendsRepository, times(1)).existsById(friendshipId);
	    	verify(friendsRepository, times(1)).deleteById(friendshipId);
	    }
	    @Test
	    public void testGetMessages() {
	        Messages message1 = new Messages();
	        message1.setMessageId(1);
	        message1.setMessage_text("Hello");
	        Messages message2 = new Messages();
	        message2.setMessageId(2);
	        message2.setMessage_text("Hi");
	        List<Messages> messagesList = Arrays.asList(message1, message2);
	        doReturn(messagesList).when(friendsService).getMessages(1);
	        ResponseEntity<List<Messages>> response = friendsController.getMessages(1);
	        assertNotNull(response);
	        assertEquals(HttpStatus.OK, response.getStatusCode());
	        assertFalse(response.getBody().isEmpty());
	        assertEquals(1, response.getBody().get(0).getMessageId());
	        assertEquals(2, response.getBody().get(1).getMessageId());
	    }

	}