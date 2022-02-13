package com.springboot.start_app_backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ser.std.StdArraySerializers.LongArraySerializer;
import com.springboot.start_app_backend.exceptions.ResourceNotFoundException;
import com.springboot.start_app_backend.models.Followers;
import com.springboot.start_app_backend.models.User;
import com.springboot.start_app_backend.repositories.FollowersRepository;
import com.springboot.start_app_backend.repositories.UserRepository;

import java.util.*;

@RestController
@RequestMapping("/api/v1/followers")
public class FollowersControllers {
	@Autowired
	UserRepository userRepository;
	@Autowired
	FollowersRepository followersRepository;

	@PostMapping("/follow/{fromId}/{toId}")
	public User follow(@PathVariable long fromId, @PathVariable long toId) {
		return userRepository.findById(fromId).map((fromUser) -> {
			return userRepository.findById(toId).map((toUser) -> {
				Followers followers = new Followers(fromUser, toUser);
				fromUser.getFollowing().add(followers);
				userRepository.save(fromUser);

				return fromUser;
			}).orElseThrow(() -> new ResourceNotFoundException("UserId " + toId + " not found"));
		}).orElseThrow(() -> new ResourceNotFoundException("UserId " + fromId + " not found"));
	}

	@DeleteMapping("/follow/{fromId}/{toId}")
	public User unfollow(@PathVariable long fromId, @PathVariable long toId) {
		return userRepository.findById(fromId).map((fromUser) -> {
			return userRepository.findById(toId).map((toUser) -> {
				Optional<Followers> fOptional = followersRepository.findFollowingsByFromIdAndToId(fromId, toId);
				if (fOptional.isPresent()) {
					followersRepository.delete(fOptional.get());
					return toUser;
				}

				throw new ResourceNotFoundException("UserId " + toId + " not found");
			}).orElseThrow(() -> new ResourceNotFoundException("UserId " + toId + " not found"));
		}).orElseThrow(() -> new ResourceNotFoundException("UserId " + fromId + " not found"));
	}

}
