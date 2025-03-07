package com.service;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
 
import com.dao.FriendDAO;
import com.dao.MessageDAO;
import com.dao.UsersDAO;
import com.globalException.FriendshipNotFoundException;
import com.globalException.ResourceNotFoundException;
import com.globalException.UserNotFoundException;
import com.model.Friends;
import com.model.Messages;
import com.model.Users;
 
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
 
@Service
public class FriendsService {
 
    @Autowired
    private FriendDAO friendsRepository;
 
    @Autowired
    private UsersDAO usersRepository;
 
    @Autowired
    private MessageDAO messageRepository;
 
    public List<Friends> getFriends(int userId) {
        List<Friends> friendsList = friendsRepository.findAllByUserID1_UserIdOrUserID2_UserId(userId);
        if (friendsList.isEmpty()) {
            throw new UserNotFoundException("User not found with ID: " + userId);
        }
        return friendsList;
    }
 
    public Friends addFriend(int userId1, int userId2) {
        Users user1 = usersRepository.findById(userId1).orElseThrow(() -> new RuntimeException("User2 not found"));
        Users user2 = usersRepository.findById(userId2).orElseThrow(() -> new RuntimeException("User1 not found"));
 
        Friends friendship = new Friends();
        friendship.setUserID1(user1);
        friendship.setUserID2(user2);
        friendship.setStatus(Friends.Status.pending);
 
        return friendsRepository.save(friendship);
    }
 
    public ResponseEntity<?> removeFriend(int friendshipId) {
    	System.out.println("removal invoked");
        if (!friendsRepository.existsById(friendshipId)) {
            throw new RuntimeException("Friendship not found");
        } else {
            friendsRepository.deleteById(friendshipId);
            return new ResponseEntity<>("Friend Removed", HttpStatus.OK);
        }
    }

 
    public List<Messages> getMessages(int friendshipId) {
        Friends friendship = friendsRepository.findById(friendshipId).orElseThrow(() -> new RuntimeException("Friendship not found"));
        return messageRepository.findAllBySenderInAndReceiverIn(
            List.of(friendship.getUserID1(), friendship.getUserID2()),
            List.of(friendship.getUserID1(), friendship.getUserID2())
        );
    }
 
    public ResponseEntity<String> sendMessage(int friendshipId, String messageText) {
    	if (messageText == null || messageText.trim().isEmpty()) {
            throw new IllegalArgumentException("Message cannot be empty or null");
        }
 
        Optional<Friends> friendship = friendsRepository.findById(friendshipId);
        if (friendship.isEmpty()) {
            throw new FriendshipNotFoundException("Friendship not found with id: " + friendshipId);
        }
 
    	
    	Friends friendship1 = friendsRepository.findById(friendshipId).orElseThrow(() -> new RuntimeException("Friendship not found"));
        Messages message = new Messages();
        message.setSender(friendship1.getUserID1());
        message.setReceiver(friendship1.getUserID2());
        message.setMessage_text(messageText);
        message.setTimestamp(new Timestamp(System.currentTimeMillis()));
        messageRepository.save(message);
        return ResponseEntity.ok("Message sent successfully");
    }
}