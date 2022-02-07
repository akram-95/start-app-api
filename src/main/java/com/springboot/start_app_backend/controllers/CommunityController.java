package com.springboot.start_app_backend.controllers;

import java.util.*;

import javax.validation.Valid;

import org.hibernate.internal.ExceptionConverterImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
			this.template.convertAndSend("/topic/communities/" + newCommunity.getId() + "/realtime", newCommunity,
					header);
			return newCommunity;
		}).orElseThrow(() -> new ResourceNotFoundException("UserId " + userId + " not found"));
	}

	@PutMapping("/community/{communityId}/addUsertest")
	public ResponseEntity<?> addUserToCommunityTest(@PathVariable(value = "communityId") Long communityId,
			@Valid @RequestBody User user) {
		return ResponseEntity.ok(user);

	}

	@PutMapping("/{communityId}/addUserById")
	public Community addUserToCommunity(@PathVariable(value = "communityId") Long communityId,
			@RequestParam Long userId) {
		return communityRepository.findById(communityId).map(community -> {
			return userRepository.findById(userId).map(userFromMap -> {
				if (community.getOwner().getId() == userFromMap.getId()) {
					throw new ResourceNotFoundException(
							"User " + userId + " can't be owner and subscriber at the same time");
				}
				community.getSubscribers().add(userFromMap);
				Map<String, Object> header = new HashMap<>();
				String value = "update";
				header.put("eventType", value);
				Community newCommunity = communityRepository.save(community);
				userFromMap.getSubscirbedCommunities().add(newCommunity);
				userRepository.save(userFromMap);
				this.template.convertAndSend("/topic/communities/realtime", newCommunity, header);
				this.template.convertAndSend("/topic/communities/realtime/" + community.getId(), newCommunity, header);
				return newCommunity;
			}).orElseThrow(() -> new ResourceNotFoundException("UserId " + userId + " not found"));
		}).orElseThrow(() -> new ResourceNotFoundException("CommunityId " + communityId + " not found"));
	}

	@GetMapping("/users/{userId}")
	public Page<Community> getAllCommunitiesByUserId(@PathVariable(value = "userId") Long userId, Pageable pageable) {
		return communityRepository.findByOwnerId(userId, pageable);
	}

	@GetMapping("/subscribers/{userId}")
	public Page<Community> getAllCommunitiesBySubscriberId(@PathVariable(value = "userId") Long userId,
			Pageable pageable) {
		return communityRepository.findBysubscribersId(userId, pageable);
	}

	@PutMapping("/{communityId}/addUser")
	public Community addUserToCommunity(@PathVariable(value = "communityId") Long communityId,
			@Valid @RequestBody User user) {
		return communityRepository.findById(communityId).map(community -> {
			return userRepository.findById(user.getId()).map(userFromMap -> {
				if (community.getOwner().getId() == userFromMap.getId()) {
					throw new ResourceNotFoundException(
							"User " + user.getId() + " can't be owner and subscriber at the same time");
				}
				community.getSubscribers().add(userFromMap);
				Map<String, Object> header = new HashMap<>();
				String value = "update";
				header.put("eventType", value);
				Community newCommunity = communityRepository.save(community);
				userFromMap.getSubscirbedCommunities().add(newCommunity);
				userRepository.save(userFromMap);
				this.template.convertAndSend("/topic/communities/realtime", newCommunity, header);
				this.template.convertAndSend("/topic/communities/realtime/" + newCommunity.getId(), newCommunity,
						header);
				return newCommunity;
			}).orElseThrow(() -> new ResourceNotFoundException("UserId " + user.getId() + " not found"));
		}).orElseThrow(() -> new ResourceNotFoundException("CommunityId " + communityId + " not found"));
	}

	@PutMapping("/{communityId}/deleteUser")
	public Community deleteUserFromCommunity(@PathVariable(value = "communityId") Long communityId,
			@Valid @RequestBody User user) {
		return communityRepository.findById(communityId).map(community -> {
			return userRepository.findById(user.getId()).map(userFromMap -> {
				community.getSubscribers().remove(userFromMap);
				Map<String, Object> header = new HashMap<>();
				String value = "update";
				header.put("eventType", value);
				Community newCommunity = communityRepository.save(community);
				this.template.convertAndSend("/topic/communities/realtime", newCommunity, header);
				this.template.convertAndSend("/topic/communities/realtime/" + community.getId(), newCommunity, header);
				return newCommunity;
			}).orElseThrow(() -> new ResourceNotFoundException("UserId " + user.getId() + " not found"));
		}).orElseThrow(() -> new ResourceNotFoundException("CommunityId " + communityId + " not found"));
	}

	@DeleteMapping("/{communityId}/delete")
	public ResponseEntity<?> deleteCommunity(@PathVariable(value = "communityId") Long communityId) {
		return communityRepository.findById(communityId).map(community -> {
			communityRepository.delete(community);
			Map<String, Object> header = new HashMap<>();
			String value = "delete";
			header.put("eventType", value);
			this.template.convertAndSend("/topic/communities/realtime", community, header);
			this.template.convertAndSend("/topic/communities/realtime/" + community.getId(), community, header);
			return ResponseEntity.ok().build();

		}).orElseThrow(() -> new ResourceNotFoundException("CommunityId " + communityId + " not found"));
	}

	@PutMapping("/{communityId}/update")
	public ResponseEntity<?> updateCommunity(@PathVariable(value = "communityId") Long communityId,
			@Valid @RequestBody Community communityRequest) {
		return communityRepository.findById(communityId).map(community -> {
			Optional<Community> optionalComunityOptional = communityRepository.findByName(communityRequest.getName());
			if (optionalComunityOptional.isPresent()) {
				return ResponseEntity.badRequest().body("Community already exists with this name");
			}
			if (!(community.getName().equals(communityRequest.getName()))) {
				community.setName(communityRequest.getName());
			}
			if (!(community.getDescription().equals(communityRequest.getDescription()))) {
				community.setDescription(communityRequest.getDescription());
			}
			if (community.getIsPublic() != communityRequest.getIsPublic()) {
				community.setIsPublic(communityRequest.getIsPublic());
			}
			Community newCommunity = communityRepository.save(community);
			Map<String, Object> header = new HashMap<>();
			String value = "update";
			header.put("eventType", value);
			this.template.convertAndSend("/topic/communities/realtime", newCommunity, header);
			this.template.convertAndSend("/topic/communities/realtime/" + newCommunity.getId(), community, header);
			return ResponseEntity.ok(newCommunity);
		}).orElseThrow(() -> new ResourceNotFoundException("CommunityId " + communityId + " not found"));
	}

}