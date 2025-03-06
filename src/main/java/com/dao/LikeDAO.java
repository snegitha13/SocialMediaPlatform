package com.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.model.Likes;

@Repository
public interface LikeDAO extends JpaRepository<Likes,Integer> {
	List<Likes> findByPost_PostId(int postId);
    List<Likes> findByPost_User_UserId(int userId);
    List<Likes> findByUserUserId(int userId);
    
    @Query("SELECT l FROM Likes l WHERE l.post.id = :postId")
	List<Likes> findByPostId(@Param("postId") int postId);
	@Query("SELECT l FROM Likes l WHERE l.user.id = :userId")
	List<Likes> findByUserId(@Param("userId") int userId);
	@Modifying
	@Transactional
	@Query("DELETE FROM Likes l WHERE l.post.id = :postId")
	void deleteByPost_PostId(@Param("postId") int postId);
 
	@Modifying
	@Transactional
	@Query("DELETE FROM Likes l WHERE l.user.id = :userId")
	void deleteByUser_UserId(@Param("userId") int userId);
 
	@Query("SELECT COUNT(l) > 0 FROM Likes l WHERE l.post.id = :postId")
	boolean existsByPost_PostId(@Param("postId") int postId);
 
	@Query("SELECT COUNT(l) > 0 FROM Likes l WHERE l.user.id = :userId")
	boolean existsByUser_UserId(@Param("userId") int userId);


}
