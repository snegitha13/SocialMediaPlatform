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

import com.model.Comments;
import com.service.CommentService;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class CommentController {
	
	@Autowired
	 private CommentService service;

	
	@GetMapping("/posts/{postId}/comments")
    public ResponseEntity<List<Comments>> getCommentsByPostId(@PathVariable int postId) {
        return service.getCommentsByPostId(postId);
    }
	
	@PostMapping("/comments")
    public ResponseEntity<String> createComment(@RequestBody Comments commentDetails) {
        return service.createComment(commentDetails.getPostId().getPostId(), commentDetails.getUser().getUserId(), commentDetails.getComment_text());
    }
	
	@PostMapping("/posts/{postId}/comments")
	 public ResponseEntity<String> addCommentToPost(@PathVariable int postId, @RequestBody Comments commentDetails) {
	        return service.createComment(postId, commentDetails.getUser().getUserId(), commentDetails.getComment_text());
	  }

    @GetMapping("/comments")
    public ResponseEntity<List<Comments>> getAllComments() {
        return service.getAllComments();
    }

    @GetMapping("/comments/{commentId}")
    public ResponseEntity<Comments> getCommentById(@PathVariable int commentId) {
        return service.getCommentById(commentId);
    }

    @PutMapping("/comments/{commentId}")
    public ResponseEntity<String> updateComment(@PathVariable int commentId, @RequestBody Comments commentDetails) {
        return service.updateComment(commentId, commentDetails.getComment_text());
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable int commentId) {
        return service.deleteComment(commentId);
    }

    @DeleteMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<String> deleteCommentFromPost(@PathVariable int postId, @PathVariable int commentId) {
        return service.deleteComment(commentId);
    }
}
