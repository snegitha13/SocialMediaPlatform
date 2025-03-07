package com.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
 
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
 
import com.dao.CommentsDAO;
import com.dao.PostDAO;
import com.dao.UsersDAO;
import com.globalException.CommentsException;
import com.model.Comments;
import com.model.Posts;
import com.model.Users;
 
class CommentServiceTest {
 
    @InjectMocks
    private CommentService commentsService;
 
    @Mock
    private CommentsDAO commentsDAO;
 
    @Mock
    private PostDAO postsDAO;
 
    @Mock
    private UsersDAO usersDAO;
 
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
 
    @Test
    public void testGetCommentsByPostId_Success() {
        int postId = 1;
        Comments comment = new Comments();
        when(commentsDAO.findByPostId_PostId(postId)).thenReturn(Arrays.asList(comment));
 
        ResponseEntity<List<Comments>> response = commentsService.getCommentsByPostId(postId);
 
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }
 
    @Test
    public void testGetCommentsByPostId_NoCommentsFound() {
        int postId = 1;
        when(commentsDAO.findByPostId_PostId(postId)).thenReturn(Arrays.asList());
 
        assertThrows(CommentsException.class, () -> {
            commentsService.getCommentsByPostId(postId);
        });
    }
 
    @Test
    public void testGetAllComments_Success() {
        Comments comment = new Comments();
        when(commentsDAO.findAll()).thenReturn(Arrays.asList(comment));
 
        ResponseEntity<List<Comments>> response = commentsService.getAllComments();
 
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }
 
    @Test
    public void testGetAllComments_NoCommentsFound() {
        when(commentsDAO.findAll()).thenReturn(Arrays.asList());
 
        assertThrows(CommentsException.class, () -> {
            commentsService.getAllComments();
        });
    }
 
    @Test
    public void testGetCommentById_Success() {
        int commentId = 1;
        Comments comment = new Comments();
        when(commentsDAO.findById(commentId)).thenReturn(Optional.of(comment));
 
        ResponseEntity<Comments> response = commentsService.getCommentById(commentId);
 
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(comment, response.getBody());
    }
 
    @Test
    public void testGetCommentById_CommentNotFound() {
        int commentId = 1;
        when(commentsDAO.findById(commentId)).thenReturn(Optional.empty());
 
        assertThrows(CommentsException.class, () -> {
            commentsService.getCommentById(commentId);
        });
    }
 
    @Test
    public void testCreateComment_Success() {
        int postId = 1;
        int userId = 1;
        String commentText = "Test Comment";
        Posts post = new Posts();
        Users user = new Users();
        when(postsDAO.findById(postId)).thenReturn(Optional.of(post));
        when(usersDAO.findById(userId)).thenReturn(Optional.of(user));
 
        ResponseEntity<String> response = commentsService.createComment(postId, userId, commentText);
 
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Comment Created", response.getBody());
    }
 
    @Test
    public void testCreateComment_PostNotFound() {
        int postId = 1;
        int userId = 1;
        String commentText = "Test Comment";
        when(postsDAO.findById(postId)).thenReturn(Optional.empty());
 
        assertThrows(CommentsException.class, () -> {
            commentsService.createComment(postId, userId, commentText);
        });
    }
 
    @Test
    public void testCreateComment_UserNotFound() {
        int postId = 1;
        int userId = 1;
        String commentText = "Test Comment";
        Posts post = new Posts();
        when(postsDAO.findById(postId)).thenReturn(Optional.of(post));
        when(usersDAO.findById(userId)).thenReturn(Optional.empty());
 
        assertThrows(CommentsException.class, () -> {
            commentsService.createComment(postId, userId, commentText);
        });
    }
 
    @Test
    public void testUpdateComment_Success() {
        int commentId = 1;
        String commentText = "Updated Comment";
        Comments comment = new Comments();
        when(commentsDAO.findById(commentId)).thenReturn(Optional.of(comment));
 
        ResponseEntity<String> response = commentsService.updateComment(commentId, commentText);
 
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals("Comment Updated", response.getBody());
    }
 
    @Test
    public void testUpdateComment_CommentNotFound() {
        int commentId = 1;
        String commentText = "Updated Comment";
        when(commentsDAO.findById(commentId)).thenReturn(Optional.empty());
 
        assertThrows(CommentsException.class, () -> {
            commentsService.updateComment(commentId, commentText);
        });
    }
 
    @Test
    public void testDeleteComment_Success() {
        int commentId = 1;
        when(commentsDAO.existsById(commentId)).thenReturn(true);
 
        ResponseEntity<String> response = commentsService.deleteComment(commentId);
 
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals("Comment Deleted", response.getBody());
    }
 
    @Test
    public void testDeleteComment_CommentNotFound() {
        int commentId = 1;
        when(commentsDAO.existsById(commentId)).thenReturn(false);
 
        assertThrows(CommentsException.class, () -> {
            commentsService.deleteComment(commentId);
        });
    }
}