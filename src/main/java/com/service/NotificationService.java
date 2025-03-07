package com.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dao.NotificationDAO;
import com.model.Notifications;

@Service
public class NotificationService {
	@Autowired
    private NotificationDAO notificationDAO;

	public Notifications markNotificationAsRead(int notificationId) throws RuntimeException {
        Notifications notification = notificationDAO.findById(notificationId).orElse(null);
        if (notification != null) {
            notification.setRead(true);
            return notificationDAO.save(notification);
        } else {
            throw new RuntimeException("Notification not found for the given ID");
        }
    }
 
    public void deleteNotification(int notificationId) throws RuntimeException {
        if (notificationDAO.existsById(notificationId)) {
            notificationDAO.deleteById(notificationId);
        } else {
            throw new RuntimeException("Notification not found for the given ID");
        }
    }
}
