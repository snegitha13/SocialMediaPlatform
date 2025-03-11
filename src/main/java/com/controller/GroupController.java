package com.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.model.Groups;
import com.model.Messages;
import com.model.Users;
import com.service.GroupService;

@RestController
@RequestMapping("/api/groups")
@CrossOrigin("*")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @GetMapping
    public ResponseEntity<List<Groups>> getAllGroups() {
        return groupService.getAllGroups();
    }

    @GetMapping("/{groupId}")
    public ResponseEntity<Groups> getGroupById(@PathVariable int groupId) {
        return groupService.getGroupById(groupId);
    }

    @PostMapping
    public ResponseEntity<String> createGroup(@RequestBody Groups group) {
        return groupService.createGroup(group);
    }


    @PutMapping("/{groupId}")
    public ResponseEntity<String> updateGroup(@PathVariable int groupId, @RequestBody Groups groupDetails) {
        return groupService.updateGroup(groupId, groupDetails);
    }

    @DeleteMapping("/{groupId}")
    public ResponseEntity<String> deleteGroup(@PathVariable int groupId) {
        return groupService.deleteGroup(groupId);
    }

    @DeleteMapping("/{groupId}/leave/{userId}")
    public ResponseEntity<String> leaveGroup(@PathVariable int groupId, @PathVariable int userId) {
        return groupService.leaveGroup(groupId, userId);
    }

    @PostMapping("/{groupId}/join/{userId}")
    public ResponseEntity<String> joinGroup(@PathVariable int groupId, @PathVariable int userId) {
        return groupService.joinGroup(groupId, userId);
    }

    @GetMapping("/users/{userId}/groups")
    public ResponseEntity<List<Groups>> getUserGroups(@PathVariable int userId) {
        return groupService.getUserGroups(userId);
    }

    @GetMapping("/{groupId}/messages")
    public ResponseEntity<List<Object[]>> getMessagesWithUserAndGroup(@PathVariable int groupId) {
        return groupService.getMessagesWithUserAndGroup(groupId);
    }
    
    @PostMapping("/{groupId}/messages/send/{userId}")
    public ResponseEntity<String> sendMessageToGroup(@PathVariable int groupId, @PathVariable int userId, @RequestBody String messageText) {
        return groupService.sendMessageToGroup(groupId, userId, messageText);
    }


    @GetMapping("/{groupId}/friends")
    public ResponseEntity<List<Users>> getGroupFriends(@PathVariable int groupId) {
        return groupService.getGroupFriends(groupId);
    }

    @GetMapping("/{groupId}/members")
    public ResponseEntity<List<Users>> getGroupMembers(@PathVariable int groupId) {
        return groupService.getGroupMembers(groupId);
    }

    @PostMapping("/{groupId}/members/add/{userId}")
    public ResponseEntity<String> addGroupMember(@PathVariable int groupId, @PathVariable int userId) {
        return groupService.addGroupMember(groupId, userId);
    }

    @DeleteMapping("/{groupId}/members/remove/{userId}")
    public ResponseEntity<String> removeGroupMember(@PathVariable int groupId, @PathVariable int userId) {
        return groupService.removeGroupMember(groupId, userId);
    }
}