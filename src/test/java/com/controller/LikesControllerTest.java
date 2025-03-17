package com.controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
 
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
 
import com.model.Likes;
import com.model.Posts;
import com.model.Users;
import com.service.LikeService;

import java.util.Arrays;
import java.util.List;
 
public class LikesControllerTest {
 
    @Mock
    private LikeService likesService;
 
    @InjectMocks
    private LikeController likesController;
 
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    
    public void testGetLikesByPost() {
 
        Likes like1 = new Likes();
 
        Likes like2 = new Likes();
 
        List<Likes> likes = Arrays.asList(like1, like2);
        when(likesService.getLikesByPost(eq(1))).thenReturn(new ResponseEntity<>(likes, HttpStatus.OK));
        ResponseEntity<List<Likes>> response = likesController.getLikesByPost(1);
 
        assertEquals(HttpStatus.OK, response.getStatusCode());
 
        assertEquals(2, response.getBody().size());
 
    }
    @Test
 
    public void testRemoveLike() {
 
        when(likesService.removeLike(eq(1))).thenReturn(new ResponseEntity<>("Like removed", HttpStatus.OK));
        ResponseEntity<String> response = likesController.removeLike(1);
 
        assertEquals(HttpStatus.OK, response.getStatusCode());
 
        assertEquals("Like removed", response.getBody());
 
    }
    @Test
 
    public void testGetLikesByUserPosts() {
 
        Likes like1 = new Likes();
 
        Likes like2 = new Likes();
 
        List<Likes> likes = Arrays.asList(like1, like2);
        when(likesService.getLikesByUserPosts(eq(1))).thenReturn(new ResponseEntity<>(likes, HttpStatus.OK));
        ResponseEntity<List<Likes>> response = likesController.getLikesByUserPosts(1);
 
        assertEquals(HttpStatus.OK, response.getStatusCode());
 
        assertEquals(2, response.getBody().size());
 
    }
 
    @Test
 
    public void testGetAllLikes() {
 
    	Users user = new Users();
 
        user.setUserId(1);

 
 
        Posts post = new Posts();
 
        post.setPostId(1);
 
    	Likes like1 = new Likes();
 
        like1.setLikeID(1);
 
        like1.setUserID(user);
 
        like1.setPostID(post);
 
        Users user1 = new Users();
 
        user.setUserId(1);

 
 
        Posts post1 = new Posts();
 
        post.setPostId(1);
 
        Likes like2 = new Likes();
 
        like2.setLikeID(2);
 
        like2.setUserID(user1);
 
        like2.setPostID(post1);
 
        List<Likes> likesList = Arrays.asList(like1, like2);
 
        doReturn(new ResponseEntity<>(likesList, HttpStatus.OK)).when(likesService).getAllLikes();
 
        ResponseEntity<?> response = likesController.getAllLikes();
 
        assertNotNull(response);
 
        assertEquals(HttpStatus.OK, response.getStatusCode());
 
        assertFalse(((List<Likes>) response.getBody()).isEmpty());
 
        assertEquals(1, ((List<Likes>) response.getBody()).get(0).getLikeID());
 
        assertEquals(2, ((List<Likes>) response.getBody()).get(1).getLikeID());
 
    }
 
    @Test
 
    public void testGetLikeById() {
 
    	Users user = new Users();
 
        user.setUserId(1);

 
 
        Posts post = new Posts();
 
        post.setPostId(1);
 
    	Likes like = new Likes();
 
        like.setLikeID(1);
 
        like.setUserID(user);
 
        like.setPostID(post);
 
        doReturn(new ResponseEntity<>(like, HttpStatus.FOUND)).when(likesService).getLikeById(1);
 
        ResponseEntity<?> response = likesController.getLikeById(1);
 
        assertNotNull(response);
 
        assertEquals(HttpStatus.FOUND, response.getStatusCode());
 
        assertNotNull(response.getBody());
 
        assertEquals(1, ((Likes) response.getBody()).getLikeID());
 
    }
 
    @Test
 
    public void testGetLikesByPostId() {
 
    	Users user = new Users();
 
        user.setUserId(1);

 
 
        Posts post = new Posts();
 
        post.setPostId(1);
 
    	Likes like1 = new Likes();
 
        like1.setLikeID(1);
 
        like1.setUserID(user);
 
        like1.setPostID(post);
 
        List<Likes> likesList = Arrays.asList(like1);
 
        doReturn(new ResponseEntity<>(likesList, HttpStatus.OK)).when(likesService).getLikesByPostId(1);
 
        ResponseEntity<Likes> response = (ResponseEntity<Likes>) likesController.getLikesByPostId(1);
 
        assertNotNull(response);
 
        assertEquals(HttpStatus.OK, response.getStatusCode());
 
        assertFalse(((List<Likes>) response.getBody()).isEmpty());
 
        assertEquals(1, ((List<Likes>) response.getBody()).get(0).getLikeID());
 
    }
 
    @Test
 
    public void testGetLikesByUserId() {
 
    	Users user = new Users();
 
        user.setUserId(1);

 
 
        Posts post = new Posts();
 
        post.setPostId(1);
 
    	Likes like1 = new Likes();
 
        like1.setLikeID(1);
 
        like1.setUserID(user);
 
        like1.setPostID(post);
 
        List<Likes> likesList = Arrays.asList(like1);
 
        doReturn(new ResponseEntity<>(likesList, HttpStatus.OK)).when(likesService).getLikesByUserId(1);
 
        ResponseEntity<?> response = likesController.getLikesByUserId(1);
 
        assertNotNull(response);
 
        assertEquals(HttpStatus.OK, response.getStatusCode());
 
        assertFalse(((List<Likes>) response.getBody()).isEmpty());
 
        assertEquals(1, ((List<Likes>) response.getBody()).get(0).getLikeID());
 
    }
 
    @Test
 
    public void testAddLike() {
 
        Users user = new Users();
 
        user.setUserId(1);

 
 
        Posts post = new Posts();
 
        post.setPostId(1);

 
 
        Likes like = new Likes();
 
        like.setLikeID(1);
 
        like.setUserID(user);
 
        like.setPostID(post);
 
        doReturn(new ResponseEntity<>(like, HttpStatus.CREATED)).when(likesService).addLike(any(Likes.class));
 
        ResponseEntity<?> response = likesController.addLike(like);
 
        assertNotNull(response);
 
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
 
        assertNotNull(response.getBody());
 
        assertEquals(1, ((Likes) response.getBody()).getLikeID());
 
    }
 
    @Test
 
    public void testDeleteLike() {
 
        doReturn(new ResponseEntity<>("Like deleted successfully", HttpStatus.OK)).when(likesService).deleteLike(1);
 
        ResponseEntity<?> response = likesController.deleteLike(1);
 
        assertNotNull(response);
 
        assertEquals(HttpStatus.OK, response.getStatusCode());
 
        assertEquals("Like deleted successfully", response.getBody());
 
    }
 
    @Test
 
    public void testDeleteLikesByPostId() {
 
        doReturn(new ResponseEntity<>("Likes deleted successfully", HttpStatus.OK)).when(likesService).deleteLikesByPostId(1);
 
        ResponseEntity<?> response = likesController.deleteLikesByPostId(1);
 
        assertNotNull(response);
 
        assertEquals(HttpStatus.OK, response.getStatusCode());
 
        assertEquals("Likes deleted successfully", response.getBody());
 
    }
 
    @Test
 
    public void testDeleteLikesByUserId() {
 
        doReturn(new ResponseEntity<>("Likes deleted successfully", HttpStatus.OK)).when(likesService).deleteLikesByUserId(1);
 
        ResponseEntity<String> response = likesController.deleteLikesByUserId(1);
 
        assertNotNull(response);
 
        assertEquals(HttpStatus.OK, response.getStatusCode());
 
        assertEquals("Likes deleted successfully", response.getBody());
 
    }
 
}