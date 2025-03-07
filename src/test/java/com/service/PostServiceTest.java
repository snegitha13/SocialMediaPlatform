package com.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.dao.LikeDAO;
import com.dao.PostDAO;
import com.globalException.ResourceNotFoundException;
import com.model.Posts;

class PostServiceTest {

	@InjectMocks
    private PostService postService;

    @Mock
    private PostDAO postDAO;

    @Mock
    private LikeDAO likeDAO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreatePost() {
        Posts post = new Posts();
        post.setText("This is a new post");
        post.setTimestamp(new Timestamp(System.currentTimeMillis()));

        when(postDAO.save(any(Posts.class))).thenReturn(post);

        ResponseEntity<Posts> response = postService.createPost(post);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(post, response.getBody());
    }

    @Test
    public void testGetPostById() {
        Posts post = new Posts();
        post.setPostId(1);
        post.setText("This is a new post");

        when(postDAO.findById(1)).thenReturn(Optional.of(post));

        ResponseEntity<Optional<Posts>> response = postService.getPostById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isPresent());
        assertEquals(post, response.getBody().get());
    }

    @Test
    public void testGetPostById_NotFound() {
        when(postDAO.findById(1)).thenReturn(Optional.empty());

        ResponseEntity<Optional<Posts>> response = postService.getPostById(1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertFalse(response.hasBody());
    }
    @Test
    public void testUpdatePost() {
        Posts post = new Posts();
        post.setPostId(1);
        post.setText("This is a new post");

        Posts updatedPost = new Posts();
        updatedPost.setText("Updated post text");

        when(postDAO.findById(1)).thenReturn(Optional.of(post));
        when(postDAO.save(any(Posts.class))).thenReturn(updatedPost);

        ResponseEntity<Posts> response = postService.updatePost(1, updatedPost);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedPost, response.getBody());
    }

    @Test
    public void testUpdatePost_NotFound() {
        Posts updatedPost = new Posts();
        updatedPost.setText("Updated post text");

        when(postDAO.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            postService.updatePost(1, updatedPost);
        });
    }

    @Test
    public void testDeletePost() {
        Posts post = new Posts();
        post.setPostId(1);
        post.setText("This is a new post");

        when(postDAO.findById(1)).thenReturn(Optional.of(post));
        doNothing().when(postDAO).delete(post);

        ResponseEntity<String> response = postService.deletePost(1);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void testDeletePost_NotFound() {
        when(postDAO.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            postService.deletePost(1);
        });
    }
}
