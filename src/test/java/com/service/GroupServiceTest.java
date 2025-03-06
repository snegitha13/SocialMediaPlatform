package com.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import com.model.Groups;
import com.model.Users;
import com.model.Messages;
import com.dao.MessageDAO;
import com.controller.GroupController;
import com.dao.GroupDAO;
import com.dao.UsersDAO;

import com.globalException.NoDataFoundException;
import com.globalException.ResourceNotFoundException;



class GroupServiceTest {
	 
    @InjectMocks
    private GroupService groupService;
    
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
    public void testGetAllGroups() {
        Groups group1 = new Groups();
        Groups group2 = new Groups();
        List<Groups> groupsList = Arrays.asList(group1, group2);
 
        when(groupsDAO.findAll()).thenReturn(groupsList);
 
        ResponseEntity<List<Groups>> response = groupService.getAllGroups();
 
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(groupsDAO, times(1)).findAll();
    }

    @Test
    public void testGetAllGroups_NoDataFound() {
        when(groupsDAO.findAll()).thenReturn(Collections.emptyList());
 
        assertThrows(NoDataFoundException.class, () -> {
            groupService.getAllGroups();
        });
 
        verify(groupsDAO, times(1)).findAll();
    }
 
    @Test
    public void testGetGroupById() {
        Groups group = new Groups();
        when(groupsDAO.findById(1)).thenReturn(Optional.of(group));
 
        ResponseEntity<Groups> response = groupService.getGroupById(1);
 
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(group, response.getBody());
        verify(groupsDAO, times(1)).findById(1);
    }
 
    @Test
    public void testGetGroupById_NotFound() {
        when(groupsDAO.findById(1)).thenReturn(Optional.empty());
 
        assertThrows(ResourceNotFoundException.class, () -> {
            groupService.getGroupById(1);
        });
 
        verify(groupsDAO, times(1)).findById(1);
    }
 
    @Test
    public void testCreateGroup() {
        Groups group = new Groups();
        Users admin = new Users();
        admin.setUserId(1);
        group.setAdmin(admin);
 
        when(usersDAO.findById(1)).thenReturn(Optional.of(admin));
        when(groupsDAO.save(group)).thenReturn(group);
 
        ResponseEntity<String> response = groupService.createGroup(group);
 
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Group Created", response.getBody());
        verify(usersDAO, times(1)).findById(1);
        verify(groupsDAO, times(1)).save(group);
    }
 
    @Test
    public void testCreateGroup_AdminNotFound() {
        Groups group = new Groups();
        Users admin = new Users();
        admin.setUserId(1);
        group.setAdmin(admin);
 
        when(usersDAO.findById(1)).thenReturn(Optional.empty());
 
        assertThrows(ResourceNotFoundException.class, () -> {
            groupService.createGroup(group);
        });
 
        verify(usersDAO, times(1)).findById(1);
        verify(groupsDAO, times(0)).save(group);
    }
 
    @Test
    public void testUpdateGroup() {
        Groups group = new Groups();
        Groups groupDetails = new Groups();
        groupDetails.setGroupName("Updated Group");
 
        when(groupsDAO.findById(1)).thenReturn(Optional.of(group));
        when(groupsDAO.save(group)).thenReturn(group);
 
        ResponseEntity<String> response = groupService.updateGroup(1, groupDetails);
 
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals("Group Updated", response.getBody());
        verify(groupsDAO, times(1)).findById(1);
        verify(groupsDAO, times(1)).save(group);
    }
 
    @Test
    public void testUpdateGroup_NotFound() {
        Groups groupDetails = new Groups();
        groupDetails.setGroupName("Updated Group");
 
        when(groupsDAO.findById(1)).thenReturn(Optional.empty());
 
        assertThrows(ResourceNotFoundException.class, () -> {
            groupService.updateGroup(1, groupDetails);
        });
 
        verify(groupsDAO, times(1)).findById(1);
        verify(groupsDAO, times(0)).save(any(Groups.class));
    }
 
    @Test
    public void testDeleteGroup() {
        when(groupsDAO.existsById(1)).thenReturn(true);
 
        ResponseEntity<String> response = groupService.deleteGroup(1);
 
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals("Group Deleted", response.getBody());
        verify(groupsDAO, times(1)).existsById(1);
        verify(groupsDAO, times(1)).deleteById(1);
    }
 
    @Test
    public void testDeleteGroup_NotFound() {
        when(groupsDAO.existsById(1)).thenReturn(false);
 
        assertThrows(ResourceNotFoundException.class, () -> {
            groupService.deleteGroup(1);
        });
 
        verify(groupsDAO, times(1)).existsById(1);
        verify(groupsDAO, times(0)).deleteById(1);
    }
 
    @Test
    public void testLeaveGroup() {
        Groups group = new Groups();
        Users user = new Users();
        user.setUserId(1);
 
        // Initialize the members list and add the user to avoid NullPointerException
        group.setMembers(new ArrayList<>());
        group.getMembers().add(user);
 
        when(groupsDAO.findById(1)).thenReturn(Optional.of(group));
        when(usersDAO.findById(1)).thenReturn(Optional.of(user));
 
        ResponseEntity<String> response = groupService.leaveGroup(1, 1);
 
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals("User Left Group", response.getBody());
        verify(groupsDAO, times(1)).findById(1);
        verify(usersDAO, times(1)).findById(1);
        verify(groupsDAO, times(1)).save(group);
 
        // Verify that the user was removed from the group's members list
        assertFalse(group.getMembers().contains(user));
    }
    @Test
    public void testLeaveGroup_GroupNotFound() {
        when(groupsDAO.findById(1)).thenReturn(Optional.empty());
 
        assertThrows(ResourceNotFoundException.class, () -> {
            groupService.leaveGroup(1, 1);
        });
 
        verify(groupsDAO, times(1)).findById(1);
        verify(usersDAO, times(0)).findById(1);
        verify(groupsDAO, times(0)).save(any(Groups.class));
    }
 
    @Test
    public void testLeaveGroup_UserNotFound() {
        Groups group = new Groups();
 
        when(groupsDAO.findById(1)).thenReturn(Optional.of(group));
        when(usersDAO.findById(1)).thenReturn(Optional.empty());
 
        assertThrows(ResourceNotFoundException.class, () -> {
            groupService.leaveGroup(1, 1);
        });
 
        verify(groupsDAO, times(1)).findById(1);
        verify(usersDAO, times(1)).findById(1);
        verify(groupsDAO, times(0)).save(any(Groups.class));
    }
 
    @Test
    public void testJoinGroup() {
        Groups group = new Groups();
        Users user = new Users();
        user.setUserId(1);
 
        // Initialize the members list to avoid NullPointerException
        group.setMembers(new ArrayList<>());
 
        when(groupsDAO.findById(1)).thenReturn(Optional.of(group));
        when(usersDAO.findById(1)).thenReturn(Optional.of(user));
 
        ResponseEntity<String> response = groupService.joinGroup(1, 1);
 
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals("User Joined Group", response.getBody());
        verify(groupsDAO, times(1)).findById(1);
        verify(usersDAO, times(1)).findById(1);
        verify(groupsDAO, times(1)).save(group);
 
        // Verify that the user was added to the group's members list
        assertTrue(group.getMembers().contains(user));
    }
 
    @Test
    public void testJoinGroup_GroupNotFound() {
        when(groupsDAO.findById(1)).thenReturn(Optional.empty());
 
        assertThrows(ResourceNotFoundException.class, () -> {
            groupService.joinGroup(1, 1);
        });
 
        verify(groupsDAO, times(1)).findById(1);
        verify(usersDAO, times(0)).findById(1);
        verify(groupsDAO, times(0)).save(any(Groups.class));
    }
 
    @Test
    public void testJoinGroup_UserNotFound() {
        Groups group = new Groups();
 
        when(groupsDAO.findById(1)).thenReturn(Optional.of(group));
        when(usersDAO.findById(1)).thenReturn(Optional.empty());
 
        assertThrows(ResourceNotFoundException.class, () -> {
            groupService.joinGroup(1, 1);
        });
 
        verify(groupsDAO, times(1)).findById(1);
        verify(usersDAO, times(1)).findById(1);
        verify(groupsDAO, times(0)).save(any(Groups.class));
    }

}
