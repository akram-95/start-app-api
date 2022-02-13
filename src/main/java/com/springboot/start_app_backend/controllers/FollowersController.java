package com.springboot.start_app_backend.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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

	@PostMapping("/follow/{fromId}/{toId}")
	public String follow(@PathVariable long fromId, @PathVariable long toId) {
		Optional<User> fromUser = userRepository.findById(fromId);
		Optional<User> toUser = userRepository.findById(toId);
		if (fromUser.isPresent() && toUser.isPresent()) {
			Followers followers = new Followers(fromUser.get(), toUser.get());
			// userRepository.save(fromUser.get());
			followersRepository.save(followers);
			return "Follow Successful1";
		}
		return "not found";

	}

	@DeleteMapping("/unfollow/{fromId}/{toId}")
	public User unfollow(@PathVariable long fromId, @PathVariable long toId) {
		return userRepository.findById(fromId).map((fromUser) -> {
			return userRepository.findById(toId).map((toUser) -> {
				toUser.getFollowers().remove(fromUser);
				fromUser.getFollowing().remove(toUser);
				userRepository.save(fromUser);

				return fromUser;
			}).orElseThrow(() -> new ResourceNotFoundException("UserId " + toId + " not found"));
		}).orElseThrow(() -> new ResourceNotFoundException("UserId " + fromId + " not found"));
	}

}
