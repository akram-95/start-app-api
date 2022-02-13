package com.springboot.start_app_backend.models;

import javax.persistence.*;
import javax.persistence.GenerationType;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "followers")
public class Followers {

    public long getId() {
		return id;
	}

	public User getFrom() {
		from.setFollowers(null);
		from.setFollowing(null);
		return from;
	}

	public User getTo() {
		to.setFollowers(null);
		to.setFollowing(null);
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
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
	
	@JsonIgnoreProperties({"from.followers,from.following"})
    @ManyToOne
    @JoinColumn(name="from_user_fk")
    private User from;
    @JsonIgnoreProperties({"to.followers,to.following"})
    @ManyToOne
    @JoinColumn(name="to_user_fk")
    private User to;

    public Followers() {};

    public Followers(User from, User to) {
        this.from = from;
        this.to = to;
    }
}