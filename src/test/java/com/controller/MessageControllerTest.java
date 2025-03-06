package com.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.model.Messages;
import com.service.MessageService;

public class MessageControllerTest {

    @InjectMocks
    private MessageController messageController;

    @Mock
    private MessageService messageService;
    
    private Messages message;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        message = new Messages();
        message.setMessageId(1);
        message.setMessage_text("Hello");
    }
 

    @Test
    public void testCreateMessage() {
        Messages message = new Messages();
        Messages createdMessage = new Messages();
        createdMessage.setMessage_text("Hello");

        when(messageService.createMessage(message)).thenReturn(createdMessage);

        ResponseEntity<Messages> response = messageController.createMessage(message);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdMessage, response.getBody());
    }
    
    @Test
    public void testGetAllMessages() {
        List<Messages> messagesList = Arrays.asList(message);
        when(messageService.getAllMessages()).thenAnswer(invocation -> new ResponseEntity<>(messagesList, HttpStatus.OK));
 
        ResponseEntity<List<Messages>> response = messageController.getAllMessages();
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(messagesList, response.getBody());
    }
 
    @Test
    public void testGetMessageById() {
        when(messageService.getMessageById(1)).thenAnswer(invocation -> new ResponseEntity<>(message, HttpStatus.OK));
 
        ResponseEntity<Messages> response = messageController.getMessageById(1);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(message, response.getBody());
    }
 
    @Test
    public void testUpdateMessage() {
        when(messageService.updateMessage(any(Integer.class), any(Messages.class))).thenAnswer(invocation -> new ResponseEntity<>(message, HttpStatus.OK));
 
        ResponseEntity<?> response = messageController.updateMessage(1, message);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(message, response.getBody());
    }
 
    @Test
    public void testDeleteMessage() {
        when(messageService.deleteMessage(1)).thenAnswer(invocation -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
 
        ResponseEntity<?> response = messageController.deleteMessage(1);
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    
}
