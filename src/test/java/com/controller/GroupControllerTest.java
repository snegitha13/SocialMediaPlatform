package com.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.model.Groups;
import com.model.Messages;
import com.model.Users;
import com.service.GroupService;

class GroupControllerTest {
	 
	    private MockMvc mockMvc;
	 
	    @Mock
	    private GroupService groupService;
	 
	    @InjectMocks
	    private GroupController groupsController;
	 
	    @BeforeEach
	    public void setUp() {
	        MockitoAnnotations.openMocks(this);
	        mockMvc = MockMvcBuilders.standaloneSetup(groupsController).build();
	    }
	 
	    @Test
	    public void testGetAllGroups() throws Exception {
	        List<Groups> groups = Arrays.asList(new Groups(), new Groups());
	        when(groupService.getAllGroups()).thenReturn(new ResponseEntity<>(groups, HttpStatus.OK));
	 
	        mockMvc.perform(get("/api/groups"))
	               .andExpect(status().isOk())
	               .andExpect(jsonPath("$.length()").value(groups.size()));
	    }
	 
	    @Test
	    public void testGetGroupById() throws Exception {
	        Groups group = new Groups();
	        when(groupService.getGroupById(1)).thenReturn(new ResponseEntity<>(group, HttpStatus.OK));
	 
	        mockMvc.perform(get("/api/groups/1"))
	                .andExpect(status().isOk())
	                .andExpect(jsonPath("$.groupId").value(group.getGroupId()));
	    }
	    @Test
	    public void testCreateGroup() throws Exception {
	        Groups group = new Groups();
	        group.setGroupName("Test Group");
	        when(groupService.createGroup(any(Groups.class))).thenReturn(new ResponseEntity<>(group, HttpStatus.CREATED));
	        mockMvc.perform(post("/api/groups")
	                .contentType("application/json")
	                .content("{\"groupName\":\"Test Group\"}"))
	                .andExpect(status().isCreated());
	    }
	    @Test
	    public void testUpdateGroup() throws Exception {
	        Groups group = new Groups();
	        group.setGroupName("Updated Group");
	        when(groupService.updateGroup(eq(1), any(Groups.class))).thenReturn(new ResponseEntity<>(group, HttpStatus.ACCEPTED));
	        mockMvc.perform(put("/api/groups/1")
	                .contentType("application/json")
	                .content("{\"groupName\":\"Updated Group\"}"))
	                .andExpect(status().isAccepted());
	    }
	    @Test
	    public void testDeleteGroup() throws Exception {
	        when(groupService.deleteGroup(1)).thenReturn(new ResponseEntity<>( HttpStatus.ACCEPTED));
	        mockMvc.perform(delete("/api/groups/1"))
	                .andExpect(status().isAccepted());
	    }
	 
	    @Test
	    public void testLeaveGroup() throws Exception {
	        when(groupService.leaveGroup(1, 1)).thenReturn(new ResponseEntity<>( HttpStatus.ACCEPTED));
	 
	        mockMvc.perform(delete("/api/groups/1/leave/1"))
	                .andExpect(status().isAccepted());
	    }
	 
	    @Test
	    public void testJoinGroup() throws Exception {
	        when(groupService.joinGroup(1, 1)).thenReturn(new ResponseEntity<>("User Joined Group", HttpStatus.ACCEPTED));
	 
	        mockMvc.perform(post("/api/groups/1/join/1"))
	                .andExpect(status().isAccepted())
	                .andExpect(content().string("User Joined Group"));
	    }
	 
	    @Test
	    public void testGetUserGroups() throws Exception {
	        List<Groups> groups = Arrays.asList(new Groups(), new Groups());
	        when(groupService.getUserGroups(1)).thenReturn(new ResponseEntity<>(groups, HttpStatus.OK));
	 
	        mockMvc.perform(get("/api/groups/users/1/groups"))
	                .andExpect(status().isOk())
	                .andExpect(jsonPath("$.length()").value(groups.size()));
	    }
	 
	    @Test
	    public void getCustomMessages() throws Exception {
	        List<Object> messages = new ArrayList<>();
	        Messages message1 = new Messages();
	        message1.setMessageId(1);
	        message1.setMessage_text("Test message 1");
	        message1.setTimestamp(new Timestamp(System.currentTimeMillis()));
	        message1.setSender(new Users());
	        message1.setReceiver(new Users());
	        Messages message2 = new Messages();
	        message2.setMessageId(2);
	        message2.setMessage_text("Test message 2");
	        message2.setTimestamp(new Timestamp(System.currentTimeMillis()));
	        message2.setSender(new Users());
	        message2.setReceiver(new Users());
	        messages.add(message1);
	        messages.add(message2);
	        when(groupService.getCustomMessages(1)).thenReturn(new ResponseEntity<>(messages, HttpStatus.OK));
	        mockMvc.perform(get("/api/groups/1/messages"))
	                .andExpect(status().isOk())
	                .andExpect(jsonPath("$.length()").value(messages.size()))
	                .andExpect(jsonPath("$[0].message_text").value("Test message 1"))
	                .andExpect(jsonPath("$[1].message_text").value("Test message 2"));
	    }
	 
	    @Test
	    public void testSendMessageToGroup() throws Exception {
	        when(groupService.sendMessageToGroup(eq(1), eq(1), anyString())).thenReturn(new ResponseEntity<>("Message Sent to Group", HttpStatus.CREATED));
	 
	        mockMvc.perform(post("/api/groups/1/messages/send/1")
	                .contentType("application/json")
	                .content("\"Test Message\""))
	                .andExpect(status().isCreated())
	                .andExpect(content().string("Message Sent to Group"));
	    }
	 
	    @Test
	    public void testGetGroupFriends() throws Exception {
	        List<Users> friends = Arrays.asList(new Users(), new Users());
	        when(groupService.getGroupFriends(1)).thenReturn(new ResponseEntity<>(friends, HttpStatus.OK));
	 
	        mockMvc.perform(get("/api/groups/1/friends"))
	                .andExpect(status().isOk())
	                .andExpect(jsonPath("$.length()").value(friends.size()));
	    }
	 
	    @Test
	    public void testGetGroupMembers() throws Exception {
	        List<Users> members = Arrays.asList(new Users(), new Users());
	        when(groupService.getGroupMembers(1)).thenReturn(new ResponseEntity<>(members, HttpStatus.OK));
	 
	        mockMvc.perform(get("/api/groups/1/members"))
	                .andExpect(status().isOk())
	                .andExpect(jsonPath("$.length()").value(members.size()));
	    }
	 
	    @Test
	    public void testAddGroupMember() throws Exception {
	        when(groupService.addGroupMember(1, 1)).thenReturn(new ResponseEntity<>("User Added to Group", HttpStatus.ACCEPTED));
	 
	        mockMvc.perform(post("/api/groups/1/members/add/1"))
	                .andExpect(status().isAccepted())
	                .andExpect(content().string("User Added to Group"));
	    }
	 
	    @Test
	    public void testRemoveGroupMember() throws Exception {
	        when(groupService.removeGroupMember(1, 1)).thenReturn(new ResponseEntity<>("User Removed from Group", HttpStatus.ACCEPTED));
	 
	        mockMvc.perform(delete("/api/groups/1/members/remove/1"))
	                .andExpect(status().isAccepted())
	                .andExpect(content().string("User Removed from Group"));
	    }

}
