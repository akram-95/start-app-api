package com.springboot.start_app_backend.models;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.*;

@Entity
@Table(name = "posts")
public class Post {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	public Post(String title, String content, Set<String> postImageUrls, Set<String> businessRooms,
			Set<String> personsTyp) {
		super();
		this.title = title;
		this.content = content;
		this.postImageUrls = postImageUrls;
		this.businessRooms = businessRooms;
		this.personsTyp = personsTyp;

	}

	private String title;

	public Set<String> getBusinessRooms() {
		return businessRooms;
	}

	public void setBusinessRooms(Set<String> businessRooms) {
		this.businessRooms = businessRooms;
	}

	public Set<String> getPersonsTyp() {
		return personsTyp;
	}

	public void setPersonsTyp(Set<String> personsTyp) {
		this.personsTyp = personsTyp;
	}

	private String content;
	private long creationDate;
	@ElementCollection
	private Set<String> postImageUrls = new HashSet<String>();
	@ElementCollection
	private Set<String> businessRooms = new HashSet<String>();
	@ElementCollection
	private Set<String> personsTyp = new HashSet<String>();
	@ManyToOne(optional = false)
	@JoinColumn(name = "user_id", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private User user;

	public Set<String> getPostImageUrls() {
		return postImageUrls;
	}

	public long getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(long creationDate) {
		this.creationDate = creationDate;
	}

	public void setPostImageUrls(Set<String> postImageUrls) {
		this.postImageUrls = postImageUrls;
	}

	public Long getId() {
		return id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Post() {

	}

	public Post(String title, String content, Set<String> postImageUrls) {
		this.title = title;
		this.content = content;
		this.postImageUrls = postImageUrls;
		this.creationDate = System.currentTimeMillis();
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}