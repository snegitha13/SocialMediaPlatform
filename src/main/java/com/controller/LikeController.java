package com.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.model.Likes;
import com.service.LikeService;

@RestController
@RequestMapping("/api")
public class LikeController {

    @Autowired
    private LikeService likeService;

    @PostMapping("/posts/{postId}/likes/add/{userId}")
    public ResponseEntity<Likes> addLike(@PathVariable int postId, @PathVariable int userId) {
        return likeService.addLike(postId, userId);
    }

    @GetMapping("/posts/{postId}/likes")
    public ResponseEntity<List<Likes>> getLikesByPost(@PathVariable int postId) {
        return likeService.getLikesByPost(postId);
    }

    @DeleteMapping("/posts/{postId}/likes/remove/{likeId}")
    public ResponseEntity<String> removeLike(@PathVariable int likeId) {
        return likeService.removeLike(likeId);
    }

    @GetMapping("/posts/users/{userId}/posts/likes")
    public ResponseEntity<List<Likes>> getLikesByUserPosts(@PathVariable int userId) {
        return likeService.getLikesByUserPosts(userId);
    }
    
    @GetMapping("/likes")
    public ResponseEntity<?> getAllLikes() {
        return likeService.getAllLikes();
    }
 
    @GetMapping("/likes/{likeID}")
    public ResponseEntity<?> getLikeById(@PathVariable int likeID) {
        return likeService.getLikeById(likeID);
    }
 
    @GetMapping("/likes/posts/{postID}")
    public ResponseEntity<?> getLikesByPostId(@PathVariable int postID) {
        return likeService.getLikesByPostId(postID);
    }
 
    @GetMapping("/likes/users/{userID}")
    public ResponseEntity<?> getLikesByUserId(@PathVariable int userID) {
        return likeService.getLikesByUserId(userID);
    }
 
    @PostMapping(path="/likes/addlike",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addLike(@RequestBody Likes like) {
        return likeService.addLike(like);
    }
 
    @DeleteMapping("/likes/{likeID}")
    public ResponseEntity<?> deleteLike(@PathVariable int likeID) {
        return likeService.deleteLike(likeID);
    }
 
    @DeleteMapping("/likes/post/{postId}")
    public ResponseEntity<String> deleteLikesByPostId(@PathVariable int postId) {
        return likeService.deleteLikesByPostId(postId);
    }
 
    @DeleteMapping("/likes/user/{userId}")
    public ResponseEntity<String> deleteLikesByUserId(@PathVariable int userId) {
        return likeService.deleteLikesByUserId(userId);
    }
    
}