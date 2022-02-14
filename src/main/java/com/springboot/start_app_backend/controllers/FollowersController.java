package com.springboot.start_app_backend.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.transaction.annotation.Transactional;
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
	@Transactional
	public Page<Followers> follow(@PathVariable long fromId, @PathVariable long toId, Pageable pageable)
			throws ResourceNotFoundException {
		Page<Followers> followers = followersRepository.findByFromIdAndToId(fromId, toId, pageable);
		if (followers.toList().size() == 0) {
			Optional<User> toUserOptional = userRepository.findById(toId);
			Optional<User> fromUserOptional = userRepository.findById(fromId);
			followersRepository.save(new Followers(fromUserOptional.get(), toUserOptional.get()));
			toUserOptional = userRepository.findById(toId);
			fromUserOptional = userRepository.findById(fromId);

			Map<String, Object> header = new HashMap<>();
			String value = "update";
			header.put("eventType", value);
			this.template.convertAndSend("/topic/users/realtime", fromUserOptional.get(), header);
			this.template.convertAndSend("/topic/users/realtime", toUserOptional.get(), header);
			return followers;
		}
		throw new ResourceNotFoundException("you should have onely one unique relation " + fromId + " -> " + toId);

	}

	@DeleteMapping("/unfollow/{fromId}/{toId}")
	@Transactional
	public Page<Followers> unfollow(@PathVariable long fromId, @PathVariable long toId, Pageable pageable) {
		Page<Followers> followers = followersRepository.findByFromIdAndToId(fromId, toId, pageable);
		if (!followers.toList().isEmpty()) {
			followersRepository.deleteAll(followers);
			Optional<User> toUserOptional = userRepository.findById(toId);
			Optional<User> fromUserOptional = userRepository.findById(fromId);
			Map<String, Object> header = new HashMap<>();
			String value = "update";
			header.put("eventType", value);
			this.template.convertAndSend("/topic/users/realtime", fromUserOptional.get(), header);
			this.template.convertAndSend("/topic/users/realtime", toUserOptional.get(), header);
			return followers;
		}
		throw new ResourceNotFoundException("Relation not found more");

	}

}
