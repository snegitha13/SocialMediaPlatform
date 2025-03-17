package com.service;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.dao.MessageDAO;
import com.dao.UsersDAO;
import com.globalException.MessageIsInvalidException;
import com.globalException.ResourceNotFoundException;
import com.globalException.UserNotFoundException;
import com.model.Messages;
import com.model.Users;

 
@Service
public class MessageService {
	
	@Autowired 
	private MessageDAO messagesDao;
	 
	@Autowired 
	private UsersDAO userdao;
	 
	public ResponseEntity<List<Messages>> getAllMessages() {
	    List<Messages> messagelist = messagesDao.findAll();
	    if (messagelist == null || messagelist.isEmpty()) {
	        throw new MessageIsInvalidException("It is empty");
	    }
	    return new ResponseEntity<>(messagelist, HttpStatus.OK);
	}
	 
	public ResponseEntity<Messages> getMessageById(Integer id) {
	    Messages messages = messagesDao.findById(id)
	            .orElseThrow(() -> new MessageIsInvalidException("Message not found with id: " + id));
	    return new ResponseEntity<>(messages, HttpStatus.OK);
	}
	 
	public Messages createMessage(Messages message) {
	    Users sender = userdao.findById(message.getSender().getUserId())
	            .orElseThrow(() -> new ResourceNotFoundException("Sender not found"));
	    Users receiver = userdao.findById(message.getReceiver().getUserId())
	            .orElseThrow(() -> new ResourceNotFoundException("Receiver not found"));
	
	    message.setSender(sender);
	    message.setReceiver(receiver);
	    message.setTimestamp(Timestamp.from(Instant.now())); // Set the current timestamp
	
	    return messagesDao.save(message);
	}
	 
	public ResponseEntity<Messages> updateMessage(Integer id, Messages messageDetails) {
	    Messages message = messagesDao.findById(id)
	            .orElseThrow(() -> new MessageIsInvalidException("Message not found with id: " + id));
	 
	    // Do not set messageId as it should not be updated
	    message.setSender(messageDetails.getSender());
	    message.setReceiver(messageDetails.getReceiver());
	    message.setMessage_text(messageDetails.getMessage_text());
	    message.setTimestamp(Timestamp.from(Instant.now()));
	 
	    messagesDao.save(message);
	 
	    return new ResponseEntity<>(message, HttpStatus.ACCEPTED);
	}
	 
	public ResponseEntity<Messages> deleteMessage(Integer messageId) {
	    Messages message = messagesDao.findById(messageId)
	            .orElseThrow(() -> new MessageIsInvalidException("id not exist"));
	    messagesDao.delete(message);
	    return new ResponseEntity<>(message, HttpStatus.OK);
	}
	 
	
}
