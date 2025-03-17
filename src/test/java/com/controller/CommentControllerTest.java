package com.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.model.Comments;
import com.model.Posts;
import com.model.Users;
import com.service.CommentService;

class CommentControllerTest {
	
	@InjectMocks
    private CommentController commentsController;
 
    @Mock
    private CommentService commentsService;
 
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
 
    @Test
    public void testGetCommentsByPostId() {
        int postId = 1;
        Comments comment = new Comments();
        when(commentsService.getCommentsByPostId(postId)).thenReturn(new ResponseEntity<>(Arrays.asList(comment), HttpStatus.OK));
        ResponseEntity<List<Comments>> response = commentsController.getCommentsByPostId(postId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }
    @Test
    public void testCreateComment() {
        Comments comment = new Comments();
        comment.setComment_text("Test Comment");
        Posts post = new Posts();
        post.setPostId(1);
        Users user = new Users();
        user.setUserId(1);
        comment.setPostId(post);
        comment.setUser(user);
        Comments createdComment = new Comments();
        createdComment.setComment_text("Test Comment");
        createdComment.setPostId(post);
        createdComment.setUser(user);
        when(commentsService.createComment(anyInt(), anyInt(), anyString())).thenReturn(new ResponseEntity<>(createdComment, HttpStatus.CREATED));
 
        ResponseEntity<Comments> response = commentsController.createComment(comment);
 
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Test Comment", response.getBody().getComment_text());
        assertEquals(post, response.getBody().getPostId());
        assertEquals(user, response.getBody().getUser());
    }
    @Test
    public void testAddCommentToPost() {
        int postId = 1;
        Comments comment = new Comments();
        comment.setComment_text("Test Comment");
        Users user = new Users();
        user.setUserId(1);
        comment.setUser(user);
        Comments createdComment = new Comments();
        createdComment.setComment_text("Test Comment");
        createdComment.setPostId(new Posts());
        createdComment.getPostId().setPostId(postId);
        createdComment.setUser(user);
        when(commentsService.createComment(anyInt(), anyInt(), anyString())).thenReturn(new ResponseEntity<>(createdComment, HttpStatus.CREATED));
 
        ResponseEntity<Comments> response = commentsController.addCommentToPost(postId, comment);
 
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Test Comment", response.getBody().getComment_text());
        assertEquals(postId, response.getBody().getPostId().getPostId());
        assertEquals(user, response.getBody().getUser());
    }
    @Test
    public void testGetAllComments() {
        Comments comment = new Comments();
        when(commentsService.getAllComments()).thenReturn(new ResponseEntity<>(Arrays.asList(comment), HttpStatus.OK));
        ResponseEntity<List<Comments>> response = commentsController.getAllComments();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }
    @Test
    public void testGetCommentById() {
        int commentId = 1;
        Comments comment = new Comments();
        when(commentsService.getCommentById(commentId)).thenReturn(new ResponseEntity<>(comment, HttpStatus.OK));
        ResponseEntity<Comments> response = commentsController.getCommentById(commentId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(comment, response.getBody());
    }
    @Test
    public void testUpdateComment() {
        int commentId = 1;
        Comments comment = new Comments();
        comment.setComment_text("Updated Comment");
        Comments updatedComment = new Comments();
        updatedComment.setComment_text("Updated Comment");
        when(commentsService.updateComment(anyInt(), anyString())).thenReturn(new ResponseEntity<>(updatedComment, HttpStatus.ACCEPTED));
 
        ResponseEntity<Comments> response = commentsController.updateComment(commentId, comment);
 
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Updated Comment", response.getBody().getComment_text());
    }
 
    @Test
    public void testDeleteComment() {
        int commentId = 1;
        when(commentsService.deleteComment(commentId)).thenReturn(new ResponseEntity<>("Comment Deleted", HttpStatus.NO_CONTENT));
        ResponseEntity<String> response = commentsController.deleteComment(commentId);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals("Comment Deleted", response.getBody());
    }
    @Test
    public void testDeleteCommentFromPost() {
        int postId = 1;
        int commentId = 1;
        when(commentsService.deleteComment(commentId)).thenReturn(new ResponseEntity<>("Comment Deleted", HttpStatus.NO_CONTENT));
        ResponseEntity<String> response = commentsController.deleteCommentFromPost(postId, commentId);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals("Comment Deleted", response.getBody());
    }
}