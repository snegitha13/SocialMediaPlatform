package com.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.model.Notifications;
import com.service.NotificationService;

@RestController
@RequestMapping("/api/users/{userId}/notifications")
@CrossOrigin("*")
public class NotificationController {
	@Autowired
	private NotificationService service;

    @PutMapping("/mark-read/{notificationId}")
    public Notifications markNotificationAsRead(@PathVariable int notificationId) {
   
        return service.markNotificationAsRead(notificationId);
    }

    @DeleteMapping("/delete/{notificationId}")
    public void deleteNotification(@PathVariable int notificationId) {
    	System.out.println(notificationId);
        service.deleteNotification(notificationId);
    }
}
