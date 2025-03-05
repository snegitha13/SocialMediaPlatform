package com.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.model.Friends;

@Repository
public interface FriendDAO extends JpaRepository<Friends,Integer>{

	@Query("SELECT f FROM Friends f WHERE (f.userID1.userId = :userId OR f.userID2.userId = :userId) AND f.status = 'pending'")
    List<Friends> findPendingFriendRequests(@Param("userId") int userId);

    @Query("SELECT f FROM Friends f WHERE f.userID1.userId = :userId OR f.userID2.userId = :userId")
    List<Friends> findFriendsByUserId(@Param("userId") int userId);
}
