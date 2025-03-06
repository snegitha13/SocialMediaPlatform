package com.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.dao.NotificationDAO;
import com.model.Notifications;

class NotificationServiceTest {

	@InjectMocks
    private NotificationService notificationsService;
 
    @Mock
    private NotificationDAO notificationsDAO;
 
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
 
    @Test
    public void testMarkNotificationAsRead() {
        int notificationId = 1;
        Notifications notification = new Notifications();
        notification.setNotificationId(notificationId);
        notification.setRead(false);
 
        when(notificationsDAO.findById(notificationId)).thenReturn(Optional.of(notification));
        when(notificationsDAO.save(notification)).thenReturn(notification);
 
        Notifications updatedNotification = notificationsService.markNotificationAsRead(notificationId);
 
        assertTrue(updatedNotification.isRead());
        verify(notificationsDAO, times(1)).save(notification);
    }
 
    @Test
    public void testDeleteNotification() {
        int notificationId = 1;
 
        when(notificationsDAO.existsById(notificationId)).thenReturn(true);
 
        notificationsService.deleteNotification(notificationId);
 
        verify(notificationsDAO, times(1)).deleteById(notificationId);
    }
 
    @Test
    public void testMarkNotificationAsRead_NotFound() {
        int notificationId = 999;
 
        when(notificationsDAO.findById(notificationId)).thenReturn(Optional.empty());
 
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            notificationsService.markNotificationAsRead(notificationId);
        });
 
        assertEquals("Notification not found for the given ID", exception.getMessage());
    }
 
    @Test
    public void testDeleteNotification_NotFound() {
        int notificationId = 999;
 
        when(notificationsDAO.existsById(notificationId)).thenReturn(false);
 
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            notificationsService.deleteNotification(notificationId);
        });
 
        assertEquals("Notification not found for the given ID", exception.getMessage());
    }

}
