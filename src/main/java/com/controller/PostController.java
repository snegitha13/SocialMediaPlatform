package com.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.model.Posts;
import com.service.LikeService;
import com.service.PostService;
 
@RestController
@RequestMapping("/api/post")
public class PostController {
@Autowired 
private PostService postService;
 
@Autowired
LikeService likeService;
 
 
@GetMapping("/{id}")
public ResponseEntity<Posts> getPostById(@PathVariable("id") int id){
	Posts post=postService.getPostById(id);
	if(!(post==null)) {
		return new ResponseEntity<>(post,HttpStatus.OK);
	}
	else {
		throw new RuntimeException("Validation failed");
	}
 
}
@DeleteMapping("/delete/{id}")
public ResponseEntity<?> deletePostById(@PathVariable int id){
	if(postService.deletePostById(id)) {
		return new ResponseEntity<String>("Deletion success",HttpStatus.OK);
	}
	else {
		throw new RuntimeException("Deletion is failed");
	}
}
@PostMapping
public ResponseEntity<?> createPost(@RequestBody Posts post){
	if(postService.createPost(post)) {
		return new ResponseEntity<String>("creation success",HttpStatus.OK);
	}
	else {
		throw new RuntimeException("Deletion is failed");
	}
}
@PutMapping("/update/{postId}")
public ResponseEntity<?> updatePost(@PathVariable Integer postId, @RequestBody Posts postdetails){
	if(postService.updatePost(postId, postdetails)) {
		return new ResponseEntity<String>("Updation success",HttpStatus.OK);
	}
	else {
		throw new RuntimeException("updation is failed");
	}
}
 
}
 
