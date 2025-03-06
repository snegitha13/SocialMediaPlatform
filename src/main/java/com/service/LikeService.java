package com.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dao.LikeDAO;
import com.dao.PostDAO;
import com.dao.UsersDAO;
import com.globalException.LikesNotFoundException;
import com.globalException.ResourceNotFoundException;
import com.model.Likes;
import com.model.Posts;
import com.model.Users;

@Service
public class LikeService {
	@Autowired
	private LikeDAO likesDAO;

	@Autowired
	private PostDAO postsDAO;

	@Autowired
	private UsersDAO usersDAO;

	public ResponseEntity<Likes> addLike(int postId, int userId) {
        Posts post = postsDAO.findById(postId).orElse(null);
        Users user = usersDAO.findById(userId).orElse(null);
        if (post != null && user != null) {
            Likes like = new Likes();
            like.setPostID(post);
            like.setUserID(user);
            like.setTimestamp(new Timestamp(System.currentTimeMillis()));
            Likes savedLike = likesDAO.save(like);
            return new ResponseEntity<>(savedLike, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<List<Likes>> getLikesByPost(int postId) {
        List<Likes> likes = likesDAO.findByPost_PostId(postId);
        return new ResponseEntity<>(likes, HttpStatus.OK);
    }

    public ResponseEntity<String> removeLike(int likeId) {
        Likes like = likesDAO.findById(likeId).orElse(null);
        if (like != null) {
            likesDAO.delete(like);
            return new ResponseEntity<>("Like removed successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Like not found", HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<List<Likes>> getLikesByUserPosts(int userId) {
        List<Likes> likes = likesDAO.findByPost_User_UserId(userId);
        return new ResponseEntity<>(likes, HttpStatus.OK);
    }
    
    public ResponseEntity<?> getAllLikes() {
        List<Likes> likesList = likesDAO.findAll();
        if (likesList.isEmpty()) {
            throw new LikesNotFoundException("No likes found");
        }
        return new ResponseEntity<>(likesList, HttpStatus.OK);
    }
 
    public ResponseEntity<?> getLikeById(int likeID) {
        Optional<Likes> like = likesDAO.findById(likeID);
        if (like.isPresent()) {
            return new ResponseEntity<>(like.get(), HttpStatus.OK);
        } else {
            throw new ResourceNotFoundException("Like not found with ID: " + likeID);
        }
    }
 
    public ResponseEntity<?> getLikesByPostId(int postID) {
        List<Likes> likesList = likesDAO.findByPostId(postID);
        if (likesList.isEmpty()) {
            throw new LikesNotFoundException("No likes found for post ID: " + postID);
        }
        return new ResponseEntity<>(likesList, HttpStatus.OK);
    }
 
    public ResponseEntity<?> getLikesByUserId(int userID) {
        List<Likes> likesList = likesDAO.findByUserId(userID);
        if (likesList.isEmpty()) {
            throw new LikesNotFoundException("No likes found for user ID: " + userID);
        }
        return new ResponseEntity<>(likesList, HttpStatus.OK);
    }
    public ResponseEntity<?> addLike(Likes like) {
        likesDAO.save(like);
        return new ResponseEntity<>("Like added", HttpStatus.CREATED);
    }
 
    public ResponseEntity<?> deleteLike(int likeID) {
        if (likesDAO.existsById(likeID)) {
            likesDAO.deleteById(likeID);
            return new ResponseEntity<>("Like deleted", HttpStatus.OK);
        } else {
            throw new ResourceNotFoundException("Like not found with ID: " + likeID);
        }
    }
 
    public ResponseEntity<String> deleteLikesByPostId(int postId) {
        if (likesDAO.existsByPost_PostId(postId)) {
            likesDAO.deleteByPost_PostId(postId);
            return ResponseEntity.ok("Likes for post deleted");
        } else {
            throw new ResourceNotFoundException("Post not found with ID: " + postId);
        }
    }
 
    @Transactional
    public ResponseEntity<String> deleteLikesByUserId(int userID) {
        if (likesDAO.existsByUser_UserId(userID)) {
            likesDAO.deleteByUser_UserId(userID);
            return ResponseEntity.ok("Likes by user deleted");
        } else {
            throw new ResourceNotFoundException("User not found with ID: " + userID);
        }
    }

}
