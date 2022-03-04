package com.springboot.start_app_backend;

import javax.annotation.PostConstruct;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.origin.SystemEnvironmentOrigin;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.*;

import org.springframework.messaging.converter.SimpleMessageConverter;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.amazonaws.services.alexaforbusiness.model.Sort;
import com.springboot.start_app_backend.controllers.AuthController;
import com.springboot.start_app_backend.controllers.CommunityController;
import com.springboot.start_app_backend.controllers.CommunityMessageController;
import com.springboot.start_app_backend.controllers.FollowersController;
import com.springboot.start_app_backend.controllers.JobTitlesController;
import com.springboot.start_app_backend.controllers.UserController;
import com.springboot.start_app_backend.models.Community;
import com.springboot.start_app_backend.models.CommunityMessage;
import com.springboot.start_app_backend.models.JobTitles;
import com.springboot.start_app_backend.models.SigninRequest;
import com.springboot.start_app_backend.models.SignupRequest;
import com.springboot.start_app_backend.models.User;
import com.springboot.start_app_backend.repositories.CommunityRepository;
import com.springboot.start_app_backend.repositories.JobTitlesRepository;
import com.springboot.start_app_backend.repositories.UserRepository;

import antlr.collections.*;
import java.util.*;

@SpringBootApplication
@Configuration
public class StartAppBackendApplication {
	@Autowired
	private CommunityRepository communityRepository;
	@Autowired
	private AuthController authController;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserController userController;
	@Autowired
	private CommunityController communityController;
	@Autowired
	private JobTitlesRepository jobTitlesRepository;
	@Autowired
	private JobTitlesController jobTitlesController;
	@Autowired
	private CommunityMessageController communityMessageController;
	@Autowired
	private SimpMessagingTemplate kafkaTemplate;

	public static void main(String[] args) {
		System.out.println("Aa a");
		SpringApplication.run(StartAppBackendApplication.class, args);

	}

	@PostConstruct
	public void initialize() {
	
		long from = 42;
		long to = 62;
		PageRequest pageRequest = PageRequest.of(0, 100);
		Set<String> set = new HashSet<String>();
		CommunityMessage communityMessage = new CommunityMessage();
		communityMessage.setContent("Hello world");
		JobTitles jobTitles = new JobTitles("marketing");
		for(int i = 0 ; i < 10;i++) {
			
			kafkaTemplate.convertAndSend("Test", "A");
			System.out.println("Send " + i);
		}
		
	

		// do something here
	}

}
