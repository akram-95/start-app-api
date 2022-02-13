package com.springboot.start_app_backend.models;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "followers")

public class Followers {

	public long getId() {
		return id;
	}

	/*
	 * public UserProfile getUserProfileTo() { return to.getUserProfile(); }
	 * 
	 * public UserProfile getUserProfileFrom() { return from.getUserProfile(); }
	 * 
	 * public String getUserNameTo() { return to.getUsername(); }
	 * 
	 * public long getCreation_dateTo() { return to.getCreation_date(); }
	 * 
	 * public Set<Role> getRolesTo() { return to.getRoles(); }
	 * 
	 * public String getUserNameFrom() { return from.getUsername(); }
	 * 
	 * public long getCreation_dateFrom() { return from.getCreation_date(); }
	 * 
	 * public Set<Role> getRolesFrom() { return from.getRoles(); }
	 */
	@JsonIgnoreProperties(value = { "followers", "following" })
	public User getFrom() {
		return from;
	}

	@JsonIgnoreProperties(value = { "followers", "following" })
	public User getTo() {
		return to;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setFrom(User from) {
		this.from = from;
	}

	public void setTo(User to) {
		this.to = to;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@ManyToOne
	@JoinColumn(name = "from_user_fk")
	private User from;

	@ManyToOne
	@JoinColumn(name = "to_user_fk")
	private User to;

	public Followers() {
	};

	public Followers(User from, User to) {
		this.from = from;
		this.to = to;
	}

}