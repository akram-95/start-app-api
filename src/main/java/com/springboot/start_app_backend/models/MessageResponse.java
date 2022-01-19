package com.springboot.start_app_backend.models;

public class MessageResponse {
	String message;

	public MessageResponse(String message) {
		super();
		System.out.println("Message " + message);
		
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
