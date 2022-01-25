package com.springboot.start_app_backend.models;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

import com.amazonaws.services.glue.model.Segment;
import com.amazonaws.services.s3.model.transform.Unmarshallers.SetBucketEncryptionUnmarshaller;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
	private String slogan;
	@Value("${experiences:}")
	@ElementCollection(targetClass = Experience.class)
	private Set<Experience> experiences = new HashSet<Experience>();
	@ElementCollection
	private Set<String> skills = new HashSet<String>();
	private String profileUrl;
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id")
	private User user;

	public UserProfile(String biography, String slogan, Set<Experience> experiences, String profileUrl, User user,
			Set<String> skills) {
		super();
		this.biography = biography;
		this.slogan = slogan;
		this.experiences = experiences;
		this.profileUrl = profileUrl;
		this.user = user;
		this.skills = skills;
	}

	public String getSlogan() {
		return slogan;
	}

	public void setSlogan(String slogan) {
		this.slogan = slogan;
	}

	public Set<Experience> getExperiences() {
		return experiences;
	}

	public void setExperiences(Set<Experience> experiences) {
		this.experiences = experiences;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	
	public void setUser(User user) {
		this.user = user;
	}

	public Long getId() {
		return id;
	}

	

}
