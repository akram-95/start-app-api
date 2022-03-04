package com.springboot.start_app_backend;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.internal.ExceptionConverterImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.start_app_backend.controllers.UserController;
import com.springboot.start_app_backend.models.Experience;
import com.springboot.start_app_backend.models.UserProfile;
import com.springboot.start_app_backend.repositories.UserProfileRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class test {
	@Autowired
	UserProfileRepository userProfileRepository;
	@Autowired
	private SimpMessagingTemplate kafkaTemplate;
	@GetMapping
	public String getAllUsers() {
		try {
			for(int i = 0 ; i < 10;i++) {
				
				kafkaTemplate.convertAndSend("Test", "A");
				System.out.println("Send " + i);
			}
		return "Hello";
		}catch(Exception e) {
			return e.getMessage();
		}
	}

}
