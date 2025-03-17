package com.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
public class Users {
    @Id
    @GeneratedValue
    private int userId;
    
    @NotBlank(message="Username is mandatory")
    private String userName;
    
    @Size(min=8,message="Password must be atleast 8 characters long")
    private String password;
    
    @NotBlank(message="Email should be valid")
    private String email;

    private byte[] profilePicture;

    @JsonIgnore
    @JsonProperty
    @OneToMany(mappedBy = "user")
    private List<Posts> posts;
    
    @JsonIgnore
    @JsonProperty
    @OneToMany(mappedBy = "user")
    private List<Comments> comments;

    @JsonIgnore
    @JsonProperty
    @OneToMany(mappedBy = "user")
    private List<Likes> likes;

    @JsonIgnore
    @JsonProperty
    @OneToMany(mappedBy = "userID1")
    private List<Friends> friends1=new ArrayList<>();

    @JsonIgnore
    @JsonProperty
    @OneToMany(mappedBy = "userID2")
    private List<Friends> friends2=new ArrayList<>();

    @JsonIgnore
    @JsonProperty
    @OneToMany(mappedBy = "sender")
    private List<Messages> sentMessages;

    @JsonIgnore
    @JsonProperty
    @OneToMany(mappedBy = "receiver")
    private List<Messages> receivedMessages;

    @JsonIgnore
    @JsonProperty
    @OneToMany(mappedBy = "user")
    private List<Notifications> notifications;

    @JsonIgnore
    @JsonProperty
    @OneToMany(mappedBy = "admin")
    private List<Groups> groups;
    
    @JsonIgnore
    @JsonProperty
    public List<Users> getFriends() {
        return Stream.concat(friends1.stream().map(Friends::getUserID2), friends2.stream()
        		.map(Friends::getUserID1)).collect(Collectors.toList());
    }
    
    @JsonIgnore
    @JsonProperty
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER,cascade=CascadeType.ALL)
    private List<Role> roles;
    
    
    public Users() {}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public byte[] getProfilePicture() {
		return profilePicture;
	}

	public void setProfilePicture(byte[] profilePicture) {
		this.profilePicture = profilePicture;
	}

	public List<Posts> getPosts() {
		return posts;
	}

	public void setPosts(List<Posts> posts) {
		this.posts = posts;
	}

	public List<Comments> getComments() {
		return comments;
	}

	public void setComments(List<Comments> comments) {
		this.comments = comments;
	}

	public List<Likes> getLikes() {
		return likes;
	}

	public void setLikes(List<Likes> likes) {
		this.likes = likes;
	}


	public List<Friends> getFriends1() {
		return friends1;
	}

	public void setFriends1(List<Friends> friends1) {
		this.friends1 = friends1;
	}

	public List<Friends> getFriends2() {
		return friends2;
	}

	public void setFriends2(List<Friends> friends2) {
		this.friends2 = friends2;
	}

	public List<Messages> getSentMessages() {
		return sentMessages;
	}

	public void setSentMessages(List<Messages> sentMessages) {
		this.sentMessages = sentMessages;
	}

	public List<Messages> getReceivedMessages() {
		return receivedMessages;
	}

	public void setReceivedMessages(List<Messages> receivedMessages) {
		this.receivedMessages = receivedMessages;
	}

	public List<Notifications> getNotifications() {
		return notifications;
	}

	public void setNotifications(List<Notifications> notifications) {
		this.notifications = notifications;
	}

	public List<Groups> getGroups() {
		return groups;
	}

	public void setGroups(List<Groups> groups) {
		this.groups = groups;
	}
}
