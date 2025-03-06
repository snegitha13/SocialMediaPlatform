package com.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.dao.MessageDAO;
import com.dao.UsersDAO;
import com.globalException.MessageIsInvalidException;
import com.globalException.ResourceNotFoundException;
import com.model.Messages;
import com.model.Users;

public class MessageServiceTest {

    @InjectMocks
    private MessageService messageService;

    @Mock
    private MessageDAO messageDAO;

    @Mock
    private UsersDAO usersDAO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllMessages() {
        Messages message1 = new Messages();
        Messages message2 = new Messages();
        List<Messages> messages = Arrays.asList(message1, message2);

        when(messageDAO.findAll()).thenReturn(messages);

        ResponseEntity<List<Messages>> response = messageService.getAllMessages();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    public void testGetMessageById() {
        Messages message = new Messages();
        when(messageDAO.findById(1)).thenReturn(Optional.of(message));

        ResponseEntity<Messages> response = messageService.getMessageById(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(message, response.getBody());
    }

    @Test
    public void testCreateMessage() {
        Users sender = new Users();
        sender.setUserId(1);
        Users receiver = new Users();
        receiver.setUserId(2);

        Messages message = new Messages();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setMessage_text("Hello");

        when(usersDAO.findById(1)).thenReturn(Optional.of(sender));
        when(usersDAO.findById(2)).thenReturn(Optional.of(receiver));
        when(messageDAO.save(message)).thenReturn(message);

        Messages createdMessage = messageService.createMessage(message);
        assertEquals(sender, createdMessage.getSender());
        assertEquals(receiver, createdMessage.getReceiver());
        assertEquals("Hello", createdMessage.getMessage_text());
    }

    @Test
    public void testUpdateMessage() {
        Messages existingMessage = new Messages();
        existingMessage.setMessageId(1);

        Messages updatedMessage = new Messages();
        updatedMessage.setSender(new Users());
        updatedMessage.setReceiver(new Users());
        updatedMessage.setMessage_text("Updated message");

        when(messageDAO.findById(1)).thenReturn(Optional.of(existingMessage));

        ResponseEntity<?> response = messageService.updateMessage(1, updatedMessage);
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        verify(messageDAO).save(existingMessage);
    }

    @Test
    public void testDeleteMessage() {
        Messages message = new Messages();
        when(messageDAO.findById(1)).thenReturn(Optional.of(message));

        ResponseEntity<?> response = messageService.deleteMessage(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(messageDAO).delete(message);
    }

    @Test
    public void testGetAllMessages_EmptyList() {
        when(messageDAO.findAll()).thenReturn(Arrays.asList());

        assertThrows(MessageIsInvalidException.class, () -> {
            messageService.getAllMessages();
        });
    }

    @Test
    public void testGetMessageById_NotFound() {
        when(messageDAO.findById(1)).thenReturn(Optional.empty());

        assertThrows(MessageIsInvalidException.class, () -> {
            messageService.getMessageById(1);
        });
    }

    @Test
    public void testCreateMessage_SenderNotFound() {
        Messages message = new Messages();
        Users sender = new Users();
        sender.setUserId(1);
        message.setSender(sender);

        when(usersDAO.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            messageService.createMessage(message);
        });
    }

    @Test
    public void testCreateMessage_ReceiverNotFound() {
        Messages message = new Messages();
        Users sender = new Users();
        sender.setUserId(1);
        Users receiver = new Users();
        receiver.setUserId(2);
        message.setSender(sender);
        message.setReceiver(receiver);

        when(usersDAO.findById(1)).thenReturn(Optional.of(sender));
        when(usersDAO.findById(2)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            messageService.createMessage(message);
        });
    }
	
}
