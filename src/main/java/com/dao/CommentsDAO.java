package com.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.model.Comments;

@Repository
public interface CommentsDAO extends JpaRepository<Comments,Integer>{
	List<Comments> findByPostId_PostId(int postId);
}
