package com.springboot.start_app_backend.models;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;

@Entity
@Table(name = "user_profiles")
public class UserProfile {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String bio;
	@JsonBackReference
	@OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	@JoinColumn(name = "id_profile_id")
    private User user;
	public UserProfile() {
		
	}
	public String getBio() {
		return bio;
	}
	public void setBio(String bio) {
		this.bio = bio;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public UserProfile(String bio, User user) {
		super();
		this.bio = bio;
		this.user = user;
	}
	public Long getId() {
		return id;
	}
	public long getUserId() {
		return getUser().getId();
	}
	public String getUserName() {
		return getUser().getUsername();
	}
	public String getEmail() {
		return getUser().getEmail();
	}

}
