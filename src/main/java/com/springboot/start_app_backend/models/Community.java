package com.springboot.start_app_backend.models;

import java.util.*;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ser.std.StdArraySerializers.BooleanArraySerializer;

@Entity
@Table(name = "communities")
public class Community {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Size(max = 120)
	@Column(unique = true)
	private String name;
	@Size(max = 350)
	private String description;
	private Boolean isPublic = true;
	private String imageUrl;

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	// @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	@JsonIgnoreProperties(value = { "followers", "following" })
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "user_id", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private User owner;
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	@ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
	@JoinTable(name = "communities_users", joinColumns = { @JoinColumn(name = "community_id") }, inverseJoinColumns = {
			@JoinColumn(name = "user_id") })
	Set<User> subscribers = new HashSet<User>();

	public Community() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public Boolean getIsPublic() {
		return isPublic;
	}

	public void setIsPublic(Boolean isPublic) {
		this.isPublic = isPublic;
	}

	public Set<User> getSubscribers() {
		return subscribers;
	}

	public void setSubscribers(Set<User> subscribers) {
		this.subscribers = subscribers;
	}

}