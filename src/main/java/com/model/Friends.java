package com.model;



import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

 
@Entity
public class Friends {
	@Id
	@GeneratedValue
	private int friendshipId;
	
	@JsonIgnore
	@JsonProperty
	@ManyToOne 
	@JoinColumn(name = "userID1")
	private Users userID1;
	
//	@JsonIgnore
//	@JsonProperty
	@ManyToOne
	@JoinColumn(name = "userID2")
	private Users userID2;
	@Enumerated(EnumType.STRING)
	
	private Status status;
	public enum Status {
		pending,accepted;
		}
	public Friends() {
	}
	public int getFriendshipId() {
		return friendshipId;
	}
	public void setFriendshipId(int friendshipId) {
		this.friendshipId = friendshipId;
	}
	public Users getUserID1() {
		return userID1;
	}
	public void setUserID1(Users userID1) {
		this.userID1 = userID1;
	}
	public Users getUserID2() {
		return userID2;
	}
	public void setUserID2(Users userID2) {
		this.userID2 = userID2;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "friends [friendshipId=" + friendshipId + ", userID1=" + userID1 + ", userID2=" + userID2 + ", status="
				+ status + "]";
	}

 
}