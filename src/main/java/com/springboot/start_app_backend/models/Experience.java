package com.springboot.start_app_backend.models;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "experiences")
public class Experience {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String company;
	private String position;
	private String description;
	@Column(name = "valueFrom")
	private String from;

	public boolean isCurrentlyIn() {
		return currentlyIn;
	}

	public void setCurrentlyIn(boolean currentlyIn) {
		this.currentlyIn = currentlyIn;
	}

	public Set<String> getJobTypes() {
		return jobTypes;
	}

	public void setJobTypes(Set<String> jobTypes) {
		this.jobTypes = jobTypes;
	}

	@Column(name = "valueTo")
	private String to;
	@Column(name = "currently_in")
	private boolean currentlyIn;
	@Column(name = "job_types")
	@ElementCollection
	private Set<String> jobTypes = new HashSet<>();

	public Experience() {

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public Experience(String company, String position, String description, String from, String to, boolean currentyIn,
			Set<String> jobTypes) {
		super();
		this.company = company;
		this.position = position;
		this.description = description;
		this.from = from;
		this.to = to;
		this.currentlyIn = currentyIn;
		this.jobTypes = jobTypes;
	}

}
