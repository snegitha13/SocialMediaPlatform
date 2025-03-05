package com.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.model.Likes;

@Repository
public interface LikeDAO extends JpaRepository<Likes,Integer> {
	List<Likes> findByPost_PostId(int postId);
    List<Likes> findByPost_User_UserId(int userId);

}
