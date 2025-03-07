package com.service;

import com.dao.GroupDAO;
import com.dao.MessageDAO;
import com.dao.UsersDAO;
import com.globalException.NoDataFoundException;
import com.globalException.ResourceNotFoundException;
import com.model.Groups;
import com.model.Messages;
import com.model.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
 
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
 
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
 
public class GroupServiceTest {
 
    @InjectMocks
    private GroupService groupsService;
 
    @Mock
    private GroupDAO groupsDAO;
 
    @Mock
    private UsersDAO usersDAO;
 
    @Mock
    private MessageDAO messagesDAO;
 
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
 
    @Test
    public void testGetAllGroups_Success() {
        List<Groups> groupsList = new ArrayList<>();
        groupsList.add(new Groups());
        when(groupsDAO.findAll()).thenReturn(groupsList);
 
        ResponseEntity<List<Groups>> response = groupsService.getAllGroups();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }
 
    @Test
    public void testGetAllGroups_NoDataFound() {
        when(groupsDAO.findAll()).thenReturn(new ArrayList<>());
 
        assertThrows(NoDataFoundException.class, () -> {
            groupsService.getAllGroups();
        });
    }
 
    @Test
    public void testGetGroupById_Success() {
        Groups group = new Groups();
        when(groupsDAO.findById(1)).thenReturn(Optional.of(group));
 
        ResponseEntity<Groups> response = groupsService.getGroupById(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(group, response.getBody());
    }
 
    @Test
    public void testGetGroupById_ResourceNotFound() {
        when(groupsDAO.findById(1)).thenReturn(Optional.empty());
 
        assertThrows(ResourceNotFoundException.class, () -> {
            groupsService.getGroupById(1);
        });
    }
 
    @Test
    public void testCreateGroup_Success() {
        Groups group = new Groups();
        Users admin = new Users();
        admin.setUserId(1);
        group.setAdmin(admin);
 
        when(usersDAO.findById(1)).thenReturn(Optional.of(admin));
        when(groupsDAO.save(group)).thenReturn(group);
 
        ResponseEntity<String> response = groupsService.createGroup(group);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Group Created", response.getBody());
    }
 
    @Test
    public void testCreateGroup_AdminNotFound() {
        Groups group = new Groups();
        Users admin = new Users();
        admin.setUserId(1);
        group.setAdmin(admin);
 
        when(usersDAO.findById(1)).thenReturn(Optional.empty());
 
        assertThrows(ResourceNotFoundException.class, () -> {
            groupsService.createGroup(group);
        });
    }
 
    @Test
    public void testUpdateGroup_Success() {
        Groups group = new Groups();
        Groups groupDetails = new Groups();
        groupDetails.setGroupName("Updated Group");
 
        when(groupsDAO.findById(1)).thenReturn(Optional.of(group));
        when(groupsDAO.save(group)).thenReturn(group);
 
        ResponseEntity<String> response = groupsService.updateGroup(1, groupDetails);
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals("Group Updated", response.getBody());
    }
 
    @Test
    public void testUpdateGroup_ResourceNotFound() {
        Groups groupDetails = new Groups();
        groupDetails.setGroupName("Updated Group");
 
        when(groupsDAO.findById(1)).thenReturn(Optional.empty());
 
        assertThrows(ResourceNotFoundException.class, () -> {
            groupsService.updateGroup(1, groupDetails);
        });
    }
 
    @Test
    public void testDeleteGroup_Success() {
        when(groupsDAO.existsById(1)).thenReturn(true);
 
        ResponseEntity<String> response = groupsService.deleteGroup(1);
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals("Group Deleted", response.getBody());
    }
 
    @Test
    public void testDeleteGroup_ResourceNotFound() {
        when(groupsDAO.existsById(1)).thenReturn(false);
 
        assertThrows(ResourceNotFoundException.class, () -> {
            groupsService.deleteGroup(1);
        });
    }
 
    @Test
    public void testLeaveGroup_Success() {
        Groups group = new Groups();
        Users user = new Users();
        user.setUserId(1);
 
        group.setMembers(new ArrayList<>());
        group.getMembers().add(user);
 
        when(groupsDAO.findById(1)).thenReturn(Optional.of(group));
        when(usersDAO.findById(1)).thenReturn(Optional.of(user));
 
        ResponseEntity<String> response = groupsService.leaveGroup(1, 1);
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals("User Left Group", response.getBody());
    }
 
    @Test
    public void testLeaveGroup_GroupNotFound() {
        when(groupsDAO.findById(1)).thenReturn(Optional.empty());
 
        assertThrows(ResourceNotFoundException.class, () -> {
            groupsService.leaveGroup(1, 1);
        });
    }
 
    @Test
    public void testLeaveGroup_UserNotFound() {
        Groups group = new Groups();
 
        when(groupsDAO.findById(1)).thenReturn(Optional.of(group));
        when(usersDAO.findById(1)).thenReturn(Optional.empty());
 
        assertThrows(ResourceNotFoundException.class, () -> {
            groupsService.leaveGroup(1, 1);
        });
    }
 
    @Test
    public void testJoinGroup_Success() {
        Groups group = new Groups();
        Users user = new Users();
        user.setUserId(1);
 
        group.setMembers(new ArrayList<>());
 
        when(groupsDAO.findById(1)).thenReturn(Optional.of(group));
        when(usersDAO.findById(1)).thenReturn(Optional.of(user));
 
        ResponseEntity<String> response = groupsService.joinGroup(1, 1);
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals("User Joined Group", response.getBody());
    }
 
    @Test
    public void testJoinGroup_GroupNotFound() {
        when(groupsDAO.findById(1)).thenReturn(Optional.empty());
 
        assertThrows(ResourceNotFoundException.class, () -> {
            groupsService.joinGroup(1, 1);
        });
    }
 
    @Test
    public void testJoinGroup_UserNotFound() {
        Groups group = new Groups();
 
        when(groupsDAO.findById(1)).thenReturn(Optional.of(group));
        when(usersDAO.findById(1)).thenReturn(Optional.empty());
 
        assertThrows(ResourceNotFoundException.class, () -> {
            groupsService.joinGroup(1, 1);
        });
    }
 
    @Test
    public void testGetUserGroups_Success() {
        Users user = new Users();
        Groups group1 = new Groups();
        Groups group2 = new Groups();
        user.setUserId(1);
        user.setGroups(Arrays.asList(group1, group2));
 
        when(usersDAO.findById(1)).thenReturn(Optional.of(user));
 
        ResponseEntity<List<Groups>> response = groupsService.getUserGroups(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }
 
    @Test
    public void testGetUserGroups_NoDataFound() {
        Users user = new Users();
        user.setGroups(new ArrayList<>());
 
        when(usersDAO.findById(1)).thenReturn(Optional.of(user));
 
        assertThrows(NoDataFoundException.class, () -> {
            groupsService.getUserGroups(1);
        });
    }
 
    @Test
    public void testGetMessagesWithUserAndGroup_Success() {
        Object[] message1 = new Object[]{"Hello", 1, 1};
        Object[] message2 = new Object[]{"Hi", 2, 1};
        List<Object[]> messagesList = Arrays.asList(message1, message2);
 
        when(groupsDAO.getMessagesWithUserAndGroup(1)).thenReturn(messagesList);
 
        ResponseEntity<List<Object[]>> response = groupsService.getMessagesWithUserAndGroup(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }
 
    @Test
    public void testGetMessagesWithUserAndGroup_NoDataFound() {
        when(groupsDAO.getMessagesWithUserAndGroup(1)).thenReturn(new ArrayList<>());
 
        assertThrows(NoDataFoundException.class, () -> {
            groupsService.getMessagesWithUserAndGroup(1);
        });
    }
    @Test
    public void testSendMessageToGroup_Success() {
        Groups group = new Groups();
        Users sender = new Users();
        sender.setUserId(1);
        Users receiver = new Users();
        receiver.setUserId(2);
 
        group.setMembers(new ArrayList<>());
        group.getMembers().add(receiver);
 
        when(groupsDAO.findById(1)).thenReturn(Optional.of(group));
        when(usersDAO.findById(1)).thenReturn(Optional.of(sender));
 
		ResponseEntity<String> response = groupsService.sendMessageToGroup(1, 1, "Hello");
		 
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals("Message Sent to Group", response.getBody());
		verify(groupsDAO, times(1)).findById(1);
		verify(usersDAO, times(1)).findById(1);
		verify(messagesDAO, times(1)).save(any(Messages.class));
		 
		// Verify that the message was sent to the group members
		assertTrue(group.getMembers().contains(receiver));
		}
}