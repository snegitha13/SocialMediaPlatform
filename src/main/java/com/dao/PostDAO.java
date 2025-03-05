package com.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.model.Posts;

@Repository
public interface PostDAO extends JpaRepository<Posts,Integer> {
	
}
