package com.springboot.start_app_backend.models;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.time.LocalDateTime;

@Entity
@Table(name = "users", uniqueConstraints = { @UniqueConstraint(columnNames = "username"),
		@UniqueConstraint(columnNames = "email") })
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotBlank
	@Size(max = 20)
	private String username;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}


	public LocalDateTime getTokenCreationDate() {
		return tokenCreationDate;
	}

	public void setTokenCreationDate(LocalDateTime tokenCreationDate) {
		this.tokenCreationDate = tokenCreationDate;
	}

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

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();
	@JsonManagedReference
	@OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
	private Token token2;
	private String token;
	
	@Column(columnDefinition = "TIMESTAMP")
	private LocalDateTime tokenCreationDate;
	@JsonManagedReference
	@OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private UserProfile userProfile;
	

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
