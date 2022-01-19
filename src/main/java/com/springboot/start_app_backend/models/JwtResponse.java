package com.springboot.start_app_backend.models;

import java.util.List;

public class JwtResponse {

    private String token;
    public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	private Long id;
    private String type = "Basic";
    private String username;
    private String email;
    public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getType() {
		return type;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}



	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	private List<String> roles;

    public JwtResponse(String jwt,Long id, String username, String email, List<String> roles) {
    	this.id = id;
    	this.token = jwt;
    	this.username = username;
        this.email = email;
        this.roles = roles;
    }
}