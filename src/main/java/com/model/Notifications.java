package com.model;

import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;

@Entity

public class Notifications {
 
	@Id
 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
 
	private int NotificationId;
 
	@ManyToOne
 
	@JoinColumn(name="UserId",nullable=false)
 
	private Users user;
 
	private String Content_Text;
 
	private Date TimeStamp;
	private boolean isRead;
	public int getNotificationId() {
		return NotificationId;
	}
	public void setNotificationId(int notificationId) {
		NotificationId = notificationId;
	}
	public Users getUser() {
		return user;
	}
	public void setUser(Users user) {
		this.user = user;
	}
	public String getContent_Text() {
		return Content_Text;
	}
	public void setContent_Text(String content_Text) {
		Content_Text = content_Text;
	}
	public Date getTimeStamp() {
		return TimeStamp;
	}
	public void setTimeStamp(Date timeStamp) {
		TimeStamp = timeStamp;
	}
	public boolean isRead() {
		return isRead;
	}
	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}
	@Override
	public String toString() {
		return "Notifications [NotificationId=" + NotificationId + ", user=" + user + ", Content_Text=" + Content_Text
				+ ", TimeStamp=" + TimeStamp + "]";
	}
	public Notifications() {
	} 
}