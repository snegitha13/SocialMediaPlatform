package com.model;

import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
 
@Entity
public class Comments {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int CommentId;
	
	//@JsonIgnore
	@ManyToOne
	@JoinColumn(name="postId",nullable=false)
	//@JsonBackReference
	private Posts postId;
	
	//@JsonIgnore
	@ManyToOne
	@JoinColumn(name="userId",nullable=false)
	private Users user;
	
	@NotBlank(message = "Comment text is mandatory")
	private String Comment_text;
	private Date Timestamp;
	public int getCommentId() {
		return CommentId;
	}
	public void setCommentId(int commentId) {
		CommentId = commentId;
	}
	public Posts getPostId() {
		return postId;
	}
	public void setPostId(Posts postId) {
		this.postId = postId;
	}
	public Users getUser() {
		return user;
	}
	public void setUser(Users user) {
		this.user = user;
	}
	public String getComment_text() {
		return Comment_text;
	}
	public void setComment_text(String comment_text) {
		Comment_text = comment_text;
	}
	public Date getTimestamp() {
		return Timestamp;
	}
	public void setTimestamp(Date timestamp) {
		Timestamp = timestamp;
	}
	@Override
	public String toString() {
		return "Comments [CommentId=" + CommentId + ", postId=" + postId + ", user=" + user + ", Comment_text="
				+ Comment_text + ", Timestamp=" + Timestamp + "]";
	}
	public Comments() {
	}

 
}