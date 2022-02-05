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
import com.springboot.start_app_backend.repositories.CommunityRepository;

import antlr.collections.List;

@SpringBootApplication
public class StartAppBackendApplication {
	@Autowired
	private CommunityRepository communityRepository;
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
		//System.out.println(communityController.createCommunity(userId, community));

		System.out.println(com.toList().get(0).getIsPublic());

		// do something here
	}

}
