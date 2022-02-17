package com.springboot.start_app_backend.models;

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
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.springboot.start_app_backend.enums.CommunityMessageType;

@Entity
@Table(name = "communities_messages")
public class CommunityMessage {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private long timeStamp;
	private String content;
	private CommunityMessageType communityMessageType;

	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private User author;
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "community_id", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
	private Community community;

	public CommunityMessage(String content, User author, Community community) {
		super();
		this.timeStamp = System.currentTimeMillis();
		this.content = content;
		this.author = author;
		this.community = community;
	}

	public CommunityMessage() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@JsonIgnoreProperties(value = { "followers,following" })
	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	public Community getCommunity() {
		return community;
	}

	public void setCommunity(Community community) {
		this.community = community;
	}

	public CommunityMessageType getCommunityMessageType() {
		return communityMessageType;
	}

	public void setCommunityMessageType(CommunityMessageType communityMessageType) {
		this.communityMessageType = communityMessageType;
	}

}
