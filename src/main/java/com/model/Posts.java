package com.model;

import java.sql.Timestamp;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
 
@Entity
public class Posts {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int postId;
	

	@ManyToOne
	@JoinColumn(name = "userId", nullable = false)
	private Users user;
	
    @NotBlank(message = "Text is mandatory")
	private String text;
	private Timestamp timestamp;
	
	@JsonIgnore
	@JsonProperty
	@OneToMany(mappedBy = "post")
	private List<Likes> likes;
	
	@JsonIgnore
	@JsonProperty
	@OneToMany(mappedBy = "postId")
    private List<Comments> comments;
	
	
	public List<Comments> getComments() {
		return comments;
	}
	public void setComments(List<Comments> comments) {
		this.comments = comments;
	}
	public void setLikes(List<Likes> likes) {
		this.likes = likes;
	}
	public List<Likes> getLikes() {
		return likes;
	    }
	public Posts() {}
	
	public int getPostId() {
		return postId;
	}

	public void setPostId(int postId) {
		this.postId = postId;
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public String toString() {
		return "Posts [postId=" + postId + ", text=" + text + ", timestamp=" + timestamp + "]";
	}
}
