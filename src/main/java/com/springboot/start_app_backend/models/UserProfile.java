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

import org.springframework.util.StringUtils;

import com.amazonaws.services.s3.model.transform.Unmarshallers.SetBucketEncryptionUnmarshaller;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;

import net.bytebuddy.utility.privilege.SetAccessibleAction;
import java.util.*;

@Entity
@Table(name = "user_profiles")
public class UserProfile {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String biography;
	private Set<String> skills;
	private String profileUrl;
	@JsonBackReference
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "id_profile_id")
	private User user;
	public String getBiography() {
		return biography;
	}

	public void setBiography(String biography) {
		this.biography = biography;
	}

	public Set<String> getSkills() {
		return skills;
	}

	public void setSkills(Set<String> skills) {
		this.skills = skills;
	}

	public String getProfileUrl() {
		return profileUrl;
	}

	public void setProfileUrl(String profileUrl) {
		this.profileUrl = profileUrl;
	}

	public UserProfile() {

	}

	public String getBio() {
		return biography;
	}

	public void setBio(String bio) {
		this.biography = bio;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public UserProfile(String bio, User user) {
		super();
		this.biography = bio;
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
