package com.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.model.Messages;
import com.service.MessageService;
@RestController
@RequestMapping("/api/messages")
@CrossOrigin("*")
public class MessageController {
	@Autowired 
	 private MessageService messageService;
	@PostMapping
    public ResponseEntity<Messages> createMessage(@RequestBody Messages message) {
        Messages createdMessage = messageService.createMessage(message);
        return new ResponseEntity<>(createdMessage, HttpStatus.CREATED);
    }
    @GetMapping
    public ResponseEntity<List<Messages>> getAllMessages() {
        return messageService.getAllMessages();
    }
 
    @GetMapping("/{id}")
    public ResponseEntity<Messages> getMessageById(@PathVariable Integer id) {
        return messageService.getMessageById(id);
    }
 
    @PutMapping("/{id}")
    public ResponseEntity<?> updateMessage(@PathVariable Integer id, @RequestBody Messages messageDetails) {
        return messageService.updateMessage(id, messageDetails);
    }
 
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMessage(@PathVariable Integer id) {
        return messageService.deleteMessage(id);
    }
}
