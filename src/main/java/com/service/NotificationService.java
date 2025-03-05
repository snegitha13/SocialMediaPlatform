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

    public Notifications markNotificationAsRead(int notificationId) {
        Notifications notification = notificationDAO.findById(notificationId).orElse(null);
        if (notification != null) {
            notification.setRead(true);
            return notificationDAO.save(notification);
        }
        return null;
    }

    public void deleteNotification(int notificationId) {
        notificationDAO.deleteById(notificationId);
    }
}
