package com.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
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
    private LikeDAO likeDAO;

    @Mock
    private PostDAO postDAO;

    @Mock
    private UsersDAO usersDAO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddLike() {
        Posts post = new Posts();
        Users user = new Users();
        Likes like = new Likes();
        like.setPostID(post);
        like.setUserID(user);
        like.setTimestamp(new Timestamp(System.currentTimeMillis()));

        when(postDAO.findById(1)).thenReturn(Optional.of(post));
        when(usersDAO.findById(1)).thenReturn(Optional.of(user));
        when(likeDAO.save(any(Likes.class))).thenReturn(like);

        ResponseEntity<Likes> response = likeService.addLike(1, 1);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(like, response.getBody());
    }

    @Test
    public void testGetLikesByPost() {
        Likes like1 = new Likes();
        Likes like2 = new Likes();
        List<Likes> likes = Arrays.asList(like1, like2);

        when(likeDAO.findByPost_PostId(1)).thenReturn(likes);

        ResponseEntity<List<Likes>> response = likeService.getLikesByPost(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    public void testRemoveLike() {
        Likes like = new Likes();
        when(likeDAO.findById(1)).thenReturn(Optional.of(like));

        ResponseEntity<String> response = likeService.removeLike(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Like removed successfully", response.getBody());

        verify(likeDAO).delete(like);
    }

    @Test
    public void testGetLikesByUserPosts() {
        Likes like1 = new Likes();
        Likes like2 = new Likes();
        List<Likes> likes = Arrays.asList(like1, like2);

        when(likeDAO.findByPost_User_UserId(1)).thenReturn(likes);

        ResponseEntity<List<Likes>> response = likeService.getLikesByUserPosts(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    public void testGetAllLikes() {
        Likes like1 = new Likes();
        Likes like2 = new Likes();
        List<Likes> likes = Arrays.asList(like1, like2);

        when(likeDAO.findAll()).thenReturn(likes);

        ResponseEntity<?> response = likeService.getAllLikes();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, ((List<Likes>) response.getBody()).size());
    }

    @Test
    public void testGetLikeById() {
        Likes like = new Likes();
        when(likeDAO.findById(1)).thenReturn(Optional.of(like));

        ResponseEntity<?> response = likeService.getLikeById(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(like, response.getBody());
    }

    @Test
    public void testGetLikesByPostId() {
        Likes like1 = new Likes();
        Likes like2 = new Likes();
        List<Likes> likes = Arrays.asList(like1, like2);

        when(likeDAO.findByPostId(1)).thenReturn(likes);

        ResponseEntity<?> response = likeService.getLikesByPostId(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, ((List<Likes>) response.getBody()).size());
    }

    @Test
    public void testGetLikesByUserId() {
        Likes like1 = new Likes();
        Likes like2 = new Likes();
        List<Likes> likes = Arrays.asList(like1, like2);

        when(likeDAO.findByUserId(1)).thenReturn(likes);

        ResponseEntity<?> response = likeService.getLikesByUserId(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, ((List<Likes>) response.getBody()).size());
    }

    @Test
    public void testAddLikeWithRequestBody() {
        Likes like = new Likes();
        when(likeDAO.save(any(Likes.class))).thenReturn(like);

        ResponseEntity<?> response = likeService.addLike(like);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Like added", response.getBody());
    }

    @Test
    public void testDeleteLike() {
        when(likeDAO.existsById(1)).thenReturn(true);

        ResponseEntity<?> response = likeService.deleteLike(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Like deleted", response.getBody());

        verify(likeDAO).deleteById(1);
    }

    @Test
    public void testDeleteLikesByPostId() {
        when(likeDAO.existsByPost_PostId(1)).thenReturn(true);

        ResponseEntity<String> response = likeService.deleteLikesByPostId(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Likes for post deleted", response.getBody());

        verify(likeDAO).deleteByPost_PostId(1);
    }

    @Test
    public void testDeleteLikesByUserId() {
        when(likeDAO.existsByUser_UserId(1)).thenReturn(true);

        ResponseEntity<String> response = likeService.deleteLikesByUserId(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Likes by user deleted", response.getBody());

        verify(likeDAO).deleteByUser_UserId(1);
    }

    @Test
    public void testGetAllLikes_EmptyList() {
        when(likeDAO.findAll()).thenReturn(Arrays.asList());

        assertThrows(LikesNotFoundException.class, () -> {
            likeService.getAllLikes();
        });
    }

    @Test
    public void testGetLikeById_NotFound() {
        when(likeDAO.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            likeService.getLikeById(1);
        });
    }

    @Test
    public void testGetLikesByPostId_NotFound() {
        when(likeDAO.findByPostId(1)).thenReturn(Arrays.asList());

        assertThrows(LikesNotFoundException.class, () -> {
            likeService.getLikesByPostId(1);
        });
    }

    @Test
    public void testGetLikesByUserId_NotFound() {
        when(likeDAO.findByUserId(1)).thenReturn(Arrays.asList());

        assertThrows(LikesNotFoundException.class, () -> {
            likeService.getLikesByUserId(1);
        });
    }

    @Test
    public void testDeleteLike_NotFound() {
        when(likeDAO.existsById(1)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> {
            likeService.deleteLike(1);
        });
    }

    @Test
    public void testDeleteLikesByPostId_NotFound() {
        when(likeDAO.existsByPost_PostId(1)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> {
            likeService.deleteLikesByPostId(1);
        });
    }

    @Test
    public void testDeleteLikesByUserId_NotFound() {
        when(likeDAO.existsByUser_UserId(1)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> {
            likeService.deleteLikesByUserId(1);
        });
    }
	
}
