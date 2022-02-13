package com.springboot.start_app_backend.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.start_app_backend.exceptions.ResourceNotFoundException;
import com.springboot.start_app_backend.models.Followers;
import com.springboot.start_app_backend.models.User;
import com.springboot.start_app_backend.repositories.FollowersRepository;
import com.springboot.start_app_backend.repositories.UserRepository;

@RestController
@RequestMapping("/api/v1/followers")
public class FollowersController {
	@Autowired
	UserRepository userRepository;
	@Autowired
	FollowersRepository followersRepository;
	@Autowired
	SimpMessagingTemplate template;

	@PostMapping("/follow/{fromId}/{toId}")
	public User follow(@PathVariable long fromId, @PathVariable long toId) {
		return userRepository.findById(fromId).map((fromUser) -> {
			return userRepository.findById(toId).map((toUser) -> {

				Followers followers = new Followers(fromUser, toUser);
				Optional<Followers> fOptional = followersRepository.findByFromIdAndToId(fromId, toId);
				if (fOptional.isEmpty()) {

					followersRepository.save(followers);
				}
				Map<String, Object> header = new HashMap<>();
				header.put("eventType", "update");
				this.template.convertAndSend("/topic/users/realtime", fromUser, header);
				this.template.convertAndSend("/topic/users/realtime", toUser, header);
				return fromUser;
			}).orElseThrow(() -> new ResourceNotFoundException("UserId " + toId + " not found"));
		}).orElseThrow(() -> new ResourceNotFoundException("UserId " + fromId + " not found"));

	}

	@DeleteMapping("/unfollow/{fromId}/{toId}")
	public User unfollow(@PathVariable long fromId, @PathVariable long toId) {
		return userRepository.findById(fromId).map((fromUser) -> {
			return userRepository.findById(toId).map((toUser) -> {
				Optional<Followers> fOptional = followersRepository.findByFromIdAndToId(fromId, toId);
				if (fOptional.isPresent()) {
					followersRepository.deleteById(fOptional.get().getId());
				}

				return fromUser;
			}).orElseThrow(() -> new ResourceNotFoundException("UserId " + toId + " not found"));
		}).orElseThrow(() -> new ResourceNotFoundException("UserId " + fromId + " not found"));
	}

}
