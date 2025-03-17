package com.model;

import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
@Entity
public class Comments {
    @Id
    @GeneratedValue
    private int commentId;

    @JsonIgnore
    @JsonProperty
    @ManyToOne
    @JoinColumn(name = "postId", nullable = false)
    private Posts postId;

//    @JsonIgnore
//    @JsonProperty
    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private Users user;
    @NotBlank(message = "Content text is mandatory")
    private String Comment_text;
    private Date Timestamp;
    // Getters and setters
    public int getCommentId() {
        return commentId;
    }
    public void setCommentId(int commentId) {
       this. commentId = commentId;
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
        this.Comment_text = comment_text;
    }
    public Date getTimestamp() {
        return Timestamp;
    }
    public void setTimestamp(Date timestamp) {
        this.Timestamp = timestamp;
    }
    @Override
    public String toString() {
        return "Comments [CommentId=" + commentId + ", postId=" + postId + ", user=" + user + ", Comment_text="
                + Comment_text + ", Timestamp=" + Timestamp + "]";
    }
    public Comments() {}
}