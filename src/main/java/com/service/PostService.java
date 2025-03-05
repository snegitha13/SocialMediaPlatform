package com.service;

import java.util.Optional;
 
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Service;

import com.dao.LikeDAO;
import com.dao.PostDAO;

import com.model.Posts;
 
 
@Service
public class PostService {

@Autowired 
PostDAO postsDao;
 
@Autowired 
LikeDAO likeDao;
 
public Posts getPostById(int id){

	Posts posts=postsDao.findById(id).get();

	return posts;

}

public boolean deletePostById(int id){

	Posts posts=postsDao.findById(id).get();

	postsDao.delete(posts);

	return true;

}

public boolean createPost(Posts post){

	postsDao.save(post);

	return true;

}

public boolean updatePost( Integer id,  Posts postdetails) {	

	Optional<Posts> post=postsDao.findById(id);

	if(!(post==null)){

		Posts pst=post.get();

				pst.setPostId(postdetails.getPostId());

				pst.setUser(postdetails.getUser());

				pst.setText(postdetails.getText());

				pst.setTimestamp(postdetails.getTimestamp());

				postsDao.save(pst);

	}

		return true;

}

}
 