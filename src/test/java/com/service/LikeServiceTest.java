package com.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
 
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
 
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
 
import com.dao.LikeDAO;
import com.dao.PostDAO;
import com.dao.UsersDAO;
import com.globalException.LikesNotFoundException;
import com.globalException.ResourceNotFoundException;
import com.model.Likes;
import com.model.Posts;
import com.model.Users;
 
class LikeServiceTest {
	@InjectMocks
    private LikeService likeService;
 
    @Mock
    private LikeDAO likesDAO;
 
    @Mock
    private PostDAO postDAO;
 
    @Mock
    private UsersDAO usersDAO;
 
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
 
    @Test
    void testAddLike() {
        int postId = 1;
        int userId = 1;
        Posts post = new Posts();
        Users user = new Users();
        Likes like = new Likes();
        like.setPostID(post);
        like.setUserID(user);
 
        when(postDAO.findById(postId)).thenReturn(Optional.of(post));
        when(usersDAO.findById(userId)).thenReturn(Optional.of(user));
        when(likesDAO.save(any(Likes.class))).thenReturn(like);
 
        ResponseEntity<Likes> response = likeService.addLike(postId, userId);
 
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(post, response.getBody().getPostID());
        assertEquals(user, response.getBody().getUserID());
    }
 
    @Test
    void testAddLike_PostOrUserNotFound() {
        int postId = 1;
        int userId = 1;
 
        when(postDAO.findById(postId)).thenReturn(Optional.empty());
        when(usersDAO.findById(userId)).thenReturn(Optional.empty());
 
        ResponseEntity<Likes> response = likeService.addLike(postId, userId);
 
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
 
    @Test
    void testGetLikesByPost() {
        int postId = 1;
        Likes like = new Likes();
        List<Likes> likesList = Arrays.asList(like);
 
        when(likesDAO.findByPost_PostId(postId)).thenReturn(likesList);
 
        ResponseEntity<List<Likes>> response = likeService.getLikesByPost(postId);
 
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }
 
    @Test
    void testRemoveLike() {
        int likeId = 1;
        Likes like = new Likes();
 
        when(likesDAO.findById(likeId)).thenReturn(Optional.of(like));
        doNothing().when(likesDAO).delete(like);
 
        ResponseEntity<String> response = likeService.removeLike(likeId);
 
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Like removed successfully", response.getBody());
    }
 
    @Test
    void testRemoveLike_NotFound() {
        int likeId = 1;
 
        when(likesDAO.findById(likeId)).thenReturn(Optional.empty());
 
        ResponseEntity<String> response = likeService.removeLike(likeId);
 
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Like not found", response.getBody());
    }
 
    @Test
    void testGetLikesByUserPosts() {
        int userId = 1;
        Likes like = new Likes();
        List<Likes> likesList = Arrays.asList(like);
 
        when(likesDAO.findByPost_User_UserId(userId)).thenReturn(likesList);
 
        ResponseEntity<List<Likes>> response = likeService.getLikesByUserPosts(userId);
 
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }
 
    @Test
    void testGetAllLikes() {
        Likes like = new Likes();
        List<Likes> likesList = Arrays.asList(like);
 
        when(likesDAO.findAll()).thenReturn(likesList);
 
        ResponseEntity<?> response = likeService.getAllLikes();
 
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, ((List<Likes>) response.getBody()).size());
    }
 
    @Test
    void testGetAllLikes_NotFound() {
        when(likesDAO.findAll()).thenReturn(Collections.emptyList());
 
        LikesNotFoundException exception = assertThrows(LikesNotFoundException.class, () -> {
            likeService.getAllLikes();
        });
 
        assertEquals("No likes found", exception.getMessage());
    }
    @Test

    void testGetLikeById() {

        int likeID = 1;

        Likes like = new Likes();
 
        when(likesDAO.findById(likeID)).thenReturn(Optional.of(like));
 
        ResponseEntity<?> response = likeService.getLikeById(likeID);
 
        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertEquals(like, response.getBody());

    }
 
    @Test

    void testGetLikeById_NotFound() {

        int likeID = 1;
 
        when(likesDAO.findById(likeID)).thenReturn(Optional.empty());
 
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {

            likeService.getLikeById(likeID);

        });
 
        assertEquals("Like not found with ID: " + likeID, exception.getMessage());

    }
 
    @Test

    void testGetLikesByPostId() {

        int postID = 1;

        Likes like = new Likes();

        List<Likes> likesList = Arrays.asList(like);
 
        when(likesDAO.findByPostId(postID)).thenReturn(likesList);
 
        ResponseEntity<?> response = likeService.getLikesByPostId(postID);
 
        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertEquals(1, ((List<Likes>) response.getBody()).size());

    }
 
    @Test

    void testGetLikesByPostId_InvalidPostId() {

        int postID = -1;
 
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {

            likeService.getLikesByPostId(postID);

        });
 
        assertEquals("Invalid post ID", exception.getMessage());

    }
 
    @Test

    void testGetLikesByPostId_NotFound() {

        int postID = 1;
 
        when(likesDAO.findByPostId(postID)).thenReturn(Collections.emptyList());
 
        LikesNotFoundException exception = assertThrows(LikesNotFoundException.class, () -> {

            likeService.getLikesByPostId(postID);

        });
 
        assertEquals("No likes found for post ID: " + postID, exception.getMessage());

    }
 
    @Test

    void testGetLikesByUserId() {

        int userID = 1;

        Likes like = new Likes();

        List<Likes> likesList = Arrays.asList(like);
 
        when(likesDAO.findByUserId(userID)).thenReturn(likesList);
 
        ResponseEntity<?> response = likeService.getLikesByUserId(userID);
 
        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertEquals(1, ((List<Likes>) response.getBody()).size());

    }
 
    @Test

    void testGetLikesByUserId_InvalidUserId() {

        int userID = -1;
 
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {

            likeService.getLikesByUserId(userID);

        });
 
        assertEquals("Invalid user ID", exception.getMessage());

    }
 
    @Test

    void testGetLikesByUserId_NotFound() {

        int userID = 1;
 
        when(likesDAO.findByUserId(userID)).thenReturn(Collections.emptyList());
 
        LikesNotFoundException exception = assertThrows(LikesNotFoundException.class, () -> {

            likeService.getLikesByUserId(userID);

        });
 
        assertEquals("No likes found for user ID: " + userID, exception.getMessage());

    }
 
    @Test

    void testAddLike_Overloaded() {

        Likes like = new Likes();
 
        ResponseEntity<?> response = likeService.addLike(like);
 
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        assertEquals("Like added", response.getBody());

    }
 
    @Test

    void testAddLike_Overloaded_NullLike() {

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {

            likeService.addLike(null);

        });
 
        assertEquals("Like cannot be null", exception.getMessage());

    }
 
    @Test

    void testDeleteLike() {

        int likeID = 1;
 
        when(likesDAO.existsById(likeID)).thenReturn(true);

        doNothing().when(likesDAO).deleteById(likeID);
 
        ResponseEntity<?> response = likeService.deleteLike(likeID);
 
        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertEquals("Like deleted", response.getBody());

    }

    @Test

    void testDeleteLike_InvalidLikeId() {

        int likeID = -1;
 
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {

            likeService.deleteLike(likeID);

        });
 
        assertEquals("Invalid like ID", exception.getMessage());

    }
 
    @Test

    void testDeleteLike_NotFound() {

        int likeID = 1;
 
        when(likesDAO.existsById(likeID)).thenReturn(false);
 
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {

            likeService.deleteLike(likeID);

        });
 
        assertEquals("Like not found with ID: " + likeID, exception.getMessage());

    }
 
    @Test

    void testDeleteLikesByPostId() {

        int postId = 1;
 
        when(likesDAO.existsByPost_PostId(postId)).thenReturn(true);

        doNothing().when(likesDAO).deleteByPost_PostId(postId);
 
        ResponseEntity<?> response = likeService.deleteLikesByPostId(postId);
 
        assertEquals(HttpStatus.OK, response.getStatusCode());

    }
 
    @Test

    void testDeleteLikesByPostId_InvalidPostId() {

        int postId = -1;
 
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {

            likeService.deleteLikesByPostId(postId);

        });
 
        assertEquals("Invalid post ID", exception.getMessage());

    }
 
    @Test

    void testDeleteLikesByPostId_NotFound() {

        int postId = 1;
 
        when(likesDAO.existsByPost_PostId(postId)).thenReturn(false);
 
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {

            likeService.deleteLikesByPostId(postId);

        });
 
        assertEquals("Post not found with ID: " + postId, exception.getMessage());

    }
 
    @Test

    void testDeleteLikesByUserId() {

        int userID = 1;
 
        when(likesDAO.existsByUser_UserId(userID)).thenReturn(true);

        doNothing().when(likesDAO).deleteByUser_UserId(userID);
 
        ResponseEntity<String> response = likeService.deleteLikesByUserId(userID);
 
        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertEquals("Likes by user deleted", response.getBody());

    }
 
    @Test

    void testDeleteLikesByUserId_InvalidUserId() {

        int userID = -1;
 
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {

            likeService.deleteLikesByUserId(userID);

        });
 
        assertEquals("Invalid user ID", exception.getMessage());

    }
 
    @Test

    void testDeleteLikesByUserId_NotFound() {

        int userID = 1;
 
        when(likesDAO.existsByUser_UserId(userID)).thenReturn(false);
 
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {

            likeService.deleteLikesByUserId(userID);

        });
 
        assertEquals("User not found with ID: " + userID, exception.getMessage());

    }

}
 
    