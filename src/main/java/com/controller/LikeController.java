package com.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.model.Likes;
import com.service.LikeService;

@RestController
@RequestMapping("/api/posts")
public class LikeController {

    @Autowired
    private LikeService likeService;

    @PostMapping("/{postId}/likes/add/{userId}")
    public ResponseEntity<Likes> addLike(@PathVariable int postId, @PathVariable int userId) {
        return likeService.addLike(postId, userId);
    }

    @GetMapping("/{postId}/likes")
    public ResponseEntity<List<Likes>> getLikesByPost(@PathVariable int postId) {
        return likeService.getLikesByPost(postId);
    }

    @DeleteMapping("/{postId}/likes/remove/{likeId}")
    public ResponseEntity<String> removeLike(@PathVariable int likeId) {
        return likeService.removeLike(likeId);
    }

    @GetMapping("/users/{userId}/posts/likes")
    public ResponseEntity<List<Likes>> getLikesByUserPosts(@PathVariable int userId) {
        return likeService.getLikesByUserPosts(userId);
    }
}