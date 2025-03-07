package com.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.model.Likes;
import com.model.Posts;
import com.service.LikeService;
import com.service.PostService;
 
@RestController
@RequestMapping("/api")
public class PostController {
@Autowired 
private PostService postService;
 
@Autowired
LikeService likeService;

@PostMapping("/post")
public ResponseEntity<Posts> createPost(@RequestBody Posts post) {
    return postService.createPost(post);
}

@GetMapping("/post/{postId}")
public ResponseEntity<Optional<Posts>> getPostById(@PathVariable int postId) {
    return postService.getPostById(postId);
}

@PutMapping("/post/update/{postId}")
public ResponseEntity<Posts> updatePost(@PathVariable int postId, @RequestBody Posts postDetails) {
    return postService.updatePost(postId, postDetails);
}

@DeleteMapping("/post/delete/{postId}")
public ResponseEntity<String> deletePost(@PathVariable int postId) {
    return postService.deletePost(postId);
}


 
}
 
