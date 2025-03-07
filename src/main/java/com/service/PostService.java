package com.service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.dao.LikeDAO;
import com.dao.PostDAO;
import com.globalException.ResourceNotFoundException;
import com.model.Likes;
import com.model.Posts;

 
@Service
public class PostService {

    @Autowired
    private PostDAO postDAO;

    @Autowired
    private LikeDAO likeDAO;

    public ResponseEntity<Posts> createPost(Posts post) {
        post.setTimestamp(new Timestamp(System.currentTimeMillis()));
        Posts createdPost = postDAO.save(post);
        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }

    public ResponseEntity<Optional<Posts>> getPostById(int postId) {
        Optional<Posts> post = postDAO.findById(postId);
        return post.map(value -> new ResponseEntity<>(Optional.of(value), HttpStatus.OK))
                   .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    public ResponseEntity<Posts> updatePost(int postId, Posts postDetails) {
        Posts post = postDAO.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found for this id :: " + postId));
        post.setText(postDetails.getText());
        post.setTimestamp(Timestamp.from(Instant.now()));
        Posts updatedPost = postDAO.save(post);
        return new ResponseEntity<>(updatedPost, HttpStatus.OK);
    }

    public ResponseEntity<String> deletePost(int postId) {
        Posts post = postDAO.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found for this id :: " + postId));
        postDAO.delete(post);
        return new ResponseEntity<>("Post deleted",HttpStatus.NO_CONTENT);
    }

   
}
 