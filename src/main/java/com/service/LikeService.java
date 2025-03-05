package com.service;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.dao.LikeDAO;
import com.dao.PostDAO;
import com.dao.UsersDAO;
import com.model.Likes;
import com.model.Posts;
import com.model.Users;

@Service
public class LikeService {
	@Autowired
	private LikeDAO likesDAO;

	@Autowired
	private PostDAO postsDAO;

	@Autowired
	private UsersDAO usersDAO;

	public ResponseEntity<Likes> addLike(int postId, int userId) {
        Posts post = postsDAO.findById(postId).orElse(null);
        Users user = usersDAO.findById(userId).orElse(null);
        if (post != null && user != null) {
            Likes like = new Likes();
            like.setPostID(post);
            like.setUserID(user);
            like.setTimestamp(new Timestamp(System.currentTimeMillis()));
            Likes savedLike = likesDAO.save(like);
            return new ResponseEntity<>(savedLike, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<List<Likes>> getLikesByPost(int postId) {
        List<Likes> likes = likesDAO.findByPost_PostId(postId);
        return new ResponseEntity<>(likes, HttpStatus.OK);
    }

    public ResponseEntity<String> removeLike(int likeId) {
        Likes like = likesDAO.findById(likeId).orElse(null);
        if (like != null) {
            likesDAO.delete(like);
            return new ResponseEntity<>("Like removed successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Like not found", HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<List<Likes>> getLikesByUserPosts(int userId) {
        List<Likes> likes = likesDAO.findByPost_User_UserId(userId);
        return new ResponseEntity<>(likes, HttpStatus.OK);
    }

}
