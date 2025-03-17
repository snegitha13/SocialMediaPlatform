package com.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.dao.CommentsDAO;
import com.dao.PostDAO;
import com.dao.UsersDAO;
import com.globalException.CommentsException;
import com.model.Comments;
import com.model.Posts;
import com.model.Users;

@Service
public class CommentService {
	@Autowired
    CommentsDAO commentsDAO;
 
    @Autowired
    PostDAO postDAO;
 
    @Autowired
    private UsersDAO userDAO;
 
    public ResponseEntity<List<Comments>> getCommentsByPostId(int postId) {
        List<Comments> comments = commentsDAO.findByPostId_PostId(postId);
        return ResponseEntity.ok(comments);
    }
 
    public ResponseEntity<List<Comments>> getAllComments() {
        List<Comments> comments = commentsDAO.findAll();
        if (comments.isEmpty()) {
            throw new CommentsException("GETALLFAILS", "Comments list is empty");
        }
        return ResponseEntity.ok(comments);
    }
 
    public ResponseEntity<Comments> getCommentById(int commentId) {
        Comments comment = commentsDAO.findById(commentId).orElseThrow(() -> new CommentsException("GETFAILS", "Comment not found"));
        return ResponseEntity.ok(comment);
    }
 
    public ResponseEntity<Comments> createComment(int postId, int userId, String commentText) {
        Posts post = postDAO.findById(postId).orElseThrow(() -> new CommentsException("GETFAILS", "Post not found"));
        Users user = userDAO.findById(userId).orElseThrow(() -> new CommentsException("GETFAILS", "User not found"));
 
        if (post == null || user == null) {
            throw new CommentsException("INVALID_INPUT", "Post or User not found");
        }
 
        Comments comment = new Comments();
        comment.setPostId(post);
        comment.setUser(user);
        comment.setComment_text(commentText);
 
        // Convert java.util.Date to java.sql.Date
        java.sql.Date sqlDate = new java.sql.Date(new Date().getTime());
        comment.setTimestamp(sqlDate);
 
        commentsDAO.save(comment);
        return new ResponseEntity<>(comment, HttpStatus.CREATED);
    }
 
    public ResponseEntity<Comments> addCommentToPost(int postId, int userId, String commentText) {
        return createComment(postId, userId, commentText);
    }
 
    public ResponseEntity<Comments> updateComment(int commentId, String commentText) {
        return commentsDAO.findById(commentId)
                .map(comment -> {
                    comment.setComment_text(commentText);
                    commentsDAO.save(comment);
                    return new ResponseEntity<>(comment, HttpStatus.ACCEPTED);
                })
                .orElseThrow(() -> new CommentsException("UPDATEFAILS", "Comment not found"));
    }
 
    public ResponseEntity<String> deleteComment(int commentId) {
        if (commentsDAO.existsById(commentId)) {
            commentsDAO.deleteById(commentId);
            return new ResponseEntity<>("Comment Deleted", HttpStatus.NO_CONTENT);
        }
        throw new CommentsException("DELETEFAILS", "Comment not found");
    }
    
}
