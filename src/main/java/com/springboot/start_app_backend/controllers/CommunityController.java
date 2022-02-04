package com.springboot.start_app_backend.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.start_app_backend.models.Community;
import com.springboot.start_app_backend.models.Post;
import com.springboot.start_app_backend.models.User;
import com.springboot.start_app_backend.repositories.CommunityRepository;
import com.springboot.start_app_backend.repositories.PostRepository;
import com.springboot.start_app_backend.repositories.UserRepository;
import com.springboot.start_app_backend.exceptions.ResourceNotFoundException;

@RestController
@RequestMapping("/api/v1/communities")
public class CommunityController {
	@Autowired
	private CommunityRepository communityRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	SimpMessagingTemplate template;

	@GetMapping
	public Page<Community> getAllCommunities(Pageable pageable) {
		return communityRepository.findAll(pageable);
	}

	@PostMapping("/{userId}/add")
	public Community createCommunity(@PathVariable(value = "userId") Long userId,
			@Valid @RequestBody Community community) {
		return userRepository.findById(userId).map(user -> {
			community.setOwner(user);
			Map<String, Object> header = new HashMap<>();
			String value = "create";
			header.put("eventType", value);
			Community newCommunity = communityRepository.save(community);
			this.template.convertAndSend("/topic/communities/realtime", newCommunity, header);
			this.template.convertAndSend("/topic/communities/realtime/" + newCommunity.getId(), newCommunity, header);
			return newCommunity;
		}).orElseThrow(() -> new ResourceNotFoundException("UserId " + userId + " not found"));
	}

	@PutMapping("/{communityId}/addUser")
	public Community addUserToCommunity(@PathVariable(value = "communityId") Long communityId,
			@Valid @RequestBody User user) {
		return communityRepository.findById(communityId).map(community -> {
			community.getSubscribers().add(user);
			Map<String, Object> header = new HashMap<>();
			String value = "update";
			header.put("eventType", value);
			Community newCommunity = communityRepository.save(community);
			this.template.convertAndSend("/topic/communities/realtime", newCommunity, header);
			this.template.convertAndSend("/topic/communities/realtime/" + community.getId(), newCommunity, header);
			return newCommunity;
		}).orElseThrow(() -> new ResourceNotFoundException("CommunityId " + communityId + " not found"));
	}

	@DeleteMapping("/{communityId}/deleteUser")
	public Community deleteUserFromCommunity(@PathVariable(value = "communityId") Long communityId,
			@Valid @RequestBody User user) {
		return communityRepository.findById(communityId).map(community -> {
			community.getSubscribers().remove(user);
			Map<String, Object> header = new HashMap<>();
			String value = "delete";
			header.put("eventType", value);
			Community newCommunity = communityRepository.save(community);
			this.template.convertAndSend("/topic/communities/realtime", newCommunity, header);
			this.template.convertAndSend("/topic/communities/realtime/" + community.getId(), newCommunity, header);
			return newCommunity;
		}).orElseThrow(() -> new ResourceNotFoundException("CommunityId " + communityId + " not found"));
   }

}
