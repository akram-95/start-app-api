package com.springboot.start_app_backend;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.origin.SystemEnvironmentOrigin;
import org.springframework.data.domain.*;

import com.amazonaws.services.alexaforbusiness.model.Sort;
import com.springboot.start_app_backend.controllers.CommunityController;
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
	private CommunityController communityController;

	public static void main(String[] args) {
		System.out.println("Aa a");
		SpringApplication.run(StartAppBackendApplication.class, args);

	}

	@PostConstruct
	public void initialize() {
		System.out.println("Aa a11");
		Pageable pageable = PageRequest.of(0, 20);
		Page<Community> com =  communityController.getAllCommunities(pageable);
		Community community = new Community();
		community.setName("Test");
		long userId = 42;
		long communityId = 11;
		Optional<User> user = userRepository.findById(userId);
		
		System.out.println(communityController.addUserToCommunity(communityId, user.get()));

		System.out.println(com.toList().get(0).getIsPublic());

		// do something here
	}

}
