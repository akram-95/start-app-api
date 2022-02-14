package com.springboot.start_app_backend.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.origin.SystemEnvironmentOrigin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
	public ResponseEntity<?> follow(@PathVariable long fromId, @PathVariable long toId, Pageable pageable) {
		Page<Followers> followers = followersRepository.findByFromIdAndToId(fromId, toId, pageable);
		if (followers.toList().size() == 0) {
			Optional<User> toUserOptional = userRepository.findById(toId);
			Optional<User> fromUserOptional = userRepository.findById(fromId);
			Followers followers2 = followersRepository
					.save(new Followers(fromUserOptional.get(), toUserOptional.get()));
			toUserOptional = userRepository.findById(toId);
			fromUserOptional = userRepository.findById(fromId);

			Map<String, Object> header = new HashMap<>();
			String value = "update";
			header.put("eventType", value);
			// this.template.convertAndSend("/topic/users/realtime", fromUserOptional.get(),
			// header);
			this.template.convertAndSend("/topic/users/realtime", toUserOptional.get(), header);
			return ResponseEntity.ok(toUserOptional.get());
		}
		return ResponseEntity.badRequest().body("you should have onely one unique relation " + fromId + " -> " + toId);

	}

	@DeleteMapping("/unfollow/{fromId}/{toId}")
	@Transactional
	public ResponseEntity<?> unfollow(@PathVariable long fromId, @PathVariable long toId, Pageable pageable) {
		List<Followers> followers = followersRepository.findByFromIdAndToId(fromId, toId, pageable).toList();
		System.out.println(followers.size() + " Size");
		if (!followers.isEmpty()) {
			followersRepository.deleteAll(followers);
			Optional<User> toUserOptional = userRepository.findById(toId);
			Optional<User> fromUserOptional = userRepository.findById(fromId);
			Map<String, Object> header = new HashMap<>();
			String value = "update";
			header.put("eventType", value);
			// this.template.convertAndSend("/topic/users/realtime", fromUserOptional.get(),
			// header);
			this.template.convertAndSend("/topic/users/realtime", toUserOptional.get(), header);
			return ResponseEntity.ok(toUserOptional.get());
		}
		return ResponseEntity.badRequest().body("Relation not found more");

	}

	@GetMapping("/followers/{id}")
	public ResponseEntity<?> followers(@PathVariable long id, Pageable pageable) {

		return ResponseEntity.ok(followersRepository.findByToId(id, pageable));
	}

	@GetMapping("/following/{id}")
	public ResponseEntity<?> following(@PathVariable long id, Pageable pageable) {

		return ResponseEntity.ok(followersRepository.findByFromId(id, pageable));
	}

}
