package com.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.model.Notifications;
import com.service.NotificationService;

class NotificationControllerTest {

	@InjectMocks
    private NotificationController notificationsController;
 
    @Mock
    private NotificationService notificationsService;
 
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
 
    @Test
    public void testMarkNotificationAsRead() {
        int notificationId = 1;
        Notifications notification = new Notifications();
        notification.setNotificationId(notificationId);
        notification.setRead(true);
 
        when(notificationsService.markNotificationAsRead(notificationId)).thenReturn(notification);
 
        Notifications response = notificationsController.markNotificationAsRead(notificationId);
 
        assertTrue(response.isRead());
        verify(notificationsService, times(1)).markNotificationAsRead(notificationId);
    }
 
    @Test
    public void testDeleteNotification() {
        int notificationId = 1;
 
        doNothing().when(notificationsService).deleteNotification(notificationId);
 
        notificationsController.deleteNotification(notificationId);
 
        verify(notificationsService, times(1)).deleteNotification(notificationId);
    }
 
    @Test
    public void testMarkNotificationAsRead_NotFound() {
        int notificationId = 999;
 
        when(notificationsService.markNotificationAsRead(notificationId)).thenThrow(new RuntimeException("Notification not found for the given ID"));
 
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            notificationsController.markNotificationAsRead(notificationId);
        });
 
        assertEquals("Notification not found for the given ID", exception.getMessage());
    }
 
    @Test
    public void testDeleteNotification_NotFound() {
        int notificationId = 999;
 
        doThrow(new RuntimeException("Notification not found for the given ID")).when(notificationsService).deleteNotification(notificationId);
 
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            notificationsController.deleteNotification(notificationId);
        });
 
        assertEquals("Notification not found for the given ID", exception.getMessage());
    }

}
