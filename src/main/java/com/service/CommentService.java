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
        return ResponseEntity.ok(comments);
    }

    public ResponseEntity<Comments> getCommentById(int commentId) {
        Comments comment = commentsDAO.findById(commentId).orElse(null);
        if (comment != null) {
            return ResponseEntity.ok(comment);
        }
        return ResponseEntity.notFound().build();
    }
   
//    public ResponseEntity<String> createComment(int postId, int userId, String commentText) {
//        Posts post = postDAO.findById(postId).orElse(null);
//        Users user = userDAO.findById(userId).orElse(null);
//        if (post != null && user != null) {
//            Comments comment = new Comments();
//            comment.setPostId(post);
//            comment.setUser(user);
//            comment.setComment_text(commentText);
//            comment.setTimestamp((java.sql.Date) new Date());
//            commentsDAO.save(comment);
//            return new ResponseEntity<>("Comment Created", HttpStatus.CREATED);
//        }
//        return new ResponseEntity<>("Post or User Not Found", HttpStatus.BAD_REQUEST);
//    }
    
    public ResponseEntity<String> createComment(int postId, int userId, String commentText) {
        Posts post = postDAO.findById(postId).orElse(null);
        Users user = userDAO.findById(userId).orElse(null);
        if (post != null && user != null) {
            Comments comment = new Comments();
            comment.setPostId(post);
            comment.setUser(user);
            comment.setComment_text(commentText);

            // Convert java.util.Date to java.sql.Date
            java.sql.Date sqlDate = new java.sql.Date(new Date().getTime());
            comment.setTimestamp(sqlDate);

            commentsDAO.save(comment);
            return new ResponseEntity<>("Comment Created", HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Post or User Not Found", HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<String> addCommentToPost(int postId, int userId, String commentText) {
        return createComment(postId, userId, commentText);
    }

    public ResponseEntity<String> updateComment(int commentId, String commentText) {
        return commentsDAO.findById(commentId)
                .map(comment -> {
                    comment.setComment_text(commentText);
                    commentsDAO.save(comment);
                    return new ResponseEntity<>("Comment Updated", HttpStatus.ACCEPTED);
                })
                .orElse(new ResponseEntity<>("Comment Not Found", HttpStatus.NOT_FOUND));
    }

    public ResponseEntity<String> deleteComment(int commentId) {
        if (commentsDAO.existsById(commentId)) {
            commentsDAO.deleteById(commentId);
            return new ResponseEntity<>("Comment Deleted", HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>("Comment Not Found", HttpStatus.NOT_FOUND);
    }
}
