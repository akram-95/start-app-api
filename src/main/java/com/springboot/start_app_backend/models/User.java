package com.springboot.start_app_backend.models;

import java.util.*;


import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.boot.origin.SystemEnvironmentOrigin;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;



import java.time.LocalDateTime;

@Entity
@Table(name = "users", uniqueConstraints = { @UniqueConstraint(columnNames = "username"),
		@UniqueConstraint(columnNames = "email") })
public class User {
	public List<Followers> getFollowers() {
		return followers;
	}

	public void setFollowers(List<Followers> followers) {
		this.followers = followers;
	}

	public List<Followers> getFollowing() {
		return following;
	}

	public void setFollowing(List<Followers> following) {
		this.following = following;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	public Set<Community> getSubscirbedCommunities() {
		return subscirbedCommunities;
	}

	public void setSubscirbedCommunities(Set<Community> subscirbedCommunities) {
		this.subscirbedCommunities = subscirbedCommunities;
	}

	@NotBlank
	@Size(max = 20)
	private String username;
	private long creation_date;

	@NotBlank
	@Size(max = 50)
	@Email
	private String email;

	public UserProfile getUserProfile() {
		return userProfile;
	}

	public void setUserProfile(UserProfile userProfile) {
		this.userProfile = userProfile;
	}

	@NotBlank
	@Size(max = 120)
	private String password;
	@Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
	private boolean isEnabled = false;
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
	private UserProfile userProfile;
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
	private Set<Post> posts = new HashSet<>();
	@JsonIgnore
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "owner", cascade = CascadeType.ALL)
	private Set<Community> communities = new HashSet<>();
	@ManyToMany(mappedBy = "subscribers", fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST,
			CascadeType.REMOVE })
	@JsonIgnore
	private Set<Community> subscirbedCommunities = new HashSet<Community>();
	@OneToMany(mappedBy="to")
    private List<Followers> followers;

    @OneToMany(mappedBy="from")
    private List<Followers> following;

	public long getCreation_date() {
		return creation_date;
	}

	public void setCreation_date(long creation_date) {
		this.creation_date = creation_date;
	}

	public boolean isEnabled() {
		return isEnabled;
	}

	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	public User() {

	}

	public User(String username, String email, String password) {
		this.username = username;
		this.email = email;
		this.password = password;
		this.creation_date = System.currentTimeMillis();

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
}
