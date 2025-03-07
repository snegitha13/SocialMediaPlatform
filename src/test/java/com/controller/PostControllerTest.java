package com.controller;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.model.Posts;
import com.service.PostService;

public class PostControllerTest {
 
    @Mock
    private PostService postService;
 
    @InjectMocks
    private PostController postController;
 
    private Posts post;
 
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        post = new Posts();
        post.setPostId(1);
        post.setText("This is a sample post");
    }
 
    @Test
    public void testCreatePost() {
        when(postService.createPost(any(Posts.class))).thenAnswer(invocation -> new ResponseEntity<>(post, HttpStatus.CREATED));
 
        ResponseEntity<?> response = postController.createPost(post);
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(post, response.getBody());
    }
 
    @Test
    public void testGetPostById() {
        when(postService.getPostById(1)).thenAnswer(invocation -> new ResponseEntity<>(post, HttpStatus.OK));
 
        ResponseEntity<?> response = postController.getPostById(1);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(post, response.getBody());
    }
 
    @Test
    public void testUpdatePost() {
        when(postService.updatePost(any(Integer.class), any(Posts.class))).thenAnswer(invocation -> new ResponseEntity<>(post, HttpStatus.OK));
 
        ResponseEntity<Posts> response = postController.updatePost(1, post);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(post, response.getBody());
    }
 
    @Test
    public void testDeletePost() {
        when(postService.deletePost(1)).thenAnswer(invocation -> new ResponseEntity<>(HttpStatus.OK));
 
        ResponseEntity<String> response = postController.deletePost(1);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}