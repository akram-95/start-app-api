package com.springboot.start_app_backend;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.origin.SystemEnvironmentOrigin;
import org.springframework.data.domain.*;

import com.amazonaws.services.alexaforbusiness.model.Sort;
import com.springboot.start_app_backend.controllers.CommunityController;
import com.springboot.start_app_backend.controllers.FollowersController;
import com.springboot.start_app_backend.controllers.UserController;
import com.springboot.start_app_backend.models.Community;

import com.springboot.start_app_backend.models.User;
import com.springboot.start_app_backend.repositories.CommunityRepository;

import com.springboot.start_app_backend.repositories.UserRepository;

import antlr.collections.*;
import java.util.*;

@SpringBootApplication
public class StartAppBackendApplication {
	@Autowired
	private CommunityRepository communityRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserController userController;
	@Autowired
	private CommunityController communityController;
	@Autowired
	private FollowersController followersController;


	public static void main(String[] args) {
		System.out.println("Aa a");
		SpringApplication.run(StartAppBackendApplication.class, args);

	}

	@PostConstruct
	public void initialize() {
		long from = 43;
		long to = 42;
		PageRequest pageRequest = PageRequest.of(0, 100);
		System.out.println(followersController.follow(from, to,pageRequest).getNumberOfElements());
	
	

		// do something here
	}

}
