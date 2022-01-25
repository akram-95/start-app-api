package com.springboot.start_app_backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.start_app_backend.controllers.UserController;
import com.springboot.start_app_backend.models.UserProfile;
import com.springboot.start_app_backend.repositories.UserProfileRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class test {
	@Autowired
	UserProfileRepository userProfileRepository;
	@GetMapping
	public String getAllUsers() {
		UserProfile  user= new UserProfile("aaaa12", null, null, null, null, null);
		userProfileRepository.save(user);
		return "Hello";
	}

}
