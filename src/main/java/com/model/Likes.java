package com.model;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
 
@Entity
public class Likes {
	@Id
	@GeneratedValue
	private int likeID;
	
//	@JsonIgnore
//	@JsonProperty
	@ManyToOne
    @JoinColumn(name = "postID")
	private Posts post;
	
	@JsonIgnore
	@JsonProperty
	@ManyToOne
    @JoinColumn(name = "userID")
	private Users user;
	@Column(name = "timestamp")
	private Timestamp timestamp;
	public Likes() {
		// TODO Auto-generated constructor stub
	}
	public int getLikeID() {
		return likeID;
	}
	public void setLikeID(int likeID) {
		this.likeID = likeID;
	}
	public Posts getPostID() {
		return post;
	}
	public void setPostID(Posts postID) {
		this.post = postID;
	}
	public Users getUserID() {
		return user;
	}
	public void setUserID(Users userID) {
		this.user = userID;
	}
	public Timestamp getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Timestamp sqlDate) {
		this.timestamp = sqlDate;
	}
	@Override
	public String toString() {
		return "likes [likeID=" + likeID + ", postID=" + post + ", userID=" + user + ", timestamp=" + timestamp
				+ "]";
	}
}