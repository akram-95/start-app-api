package com.springboot.start_app_backend.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.origin.SystemEnvironmentOrigin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.services.comprehend.model.Entity;
import com.springboot.start_app_backend.exceptions.ResourceNotFoundException;
import com.springboot.start_app_backend.models.MessageResponse;
import com.springboot.start_app_backend.models.Post;
import com.springboot.start_app_backend.models.User;
import com.springboot.start_app_backend.models.UserDetailsImpl;
import com.springboot.start_app_backend.models.UserProfile;
import com.springboot.start_app_backend.repositories.UserProfileRepository;
import com.springboot.start_app_backend.repositories.UserRepository;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
	int limit = 100;
	@Autowired
	UserProfileRepository userProfileRepository;
	@Autowired
	SimpMessagingTemplate template;
	@Autowired
	UserRepository userRepository;

	@GetMapping("fromIndex/{fromindex}")
	public List<User> getAllStudentsfromindex(@PathVariable int fromindex) {
		Pageable pageLimit = PageRequest.of(fromindex, limit, Sort.by(Sort.Direction.ASC, "id"));
		Page<User> list = userRepository.findAll(pageLimit);
		return list.toList();
	}

	@GetMapping
	public Page<User> getAllUsers(Pageable pageable) {
		return userRepository.findAll(pageable);
	}

	@PostMapping("/{userId}/create_profile")
	public ResponseEntity<?> createProfile(@PathVariable("userId") long userId,
			@Valid @RequestBody UserProfile userProfile) {
		return userRepository.findById(userId).map(user -> {
			if (user.getUserProfile() != null) {
				userProfileRepository.delete(user.getUserProfile());
				return ResponseEntity.badRequest().body("Profile already exist for this user");
			}
			user.setUserProfile(userProfile);
			userProfile.setUser(user);
			System.out.println("user avant " + user.getUserProfile());
			Map<String, Object> header = new HashMap<>();
			String value = "create";
			header.put("eventType", value);
			this.template.convertAndSend("/topic/users/realtime", user, header);
			return ResponseEntity.ok(userRepository.save(user));
		}).orElseThrow(() -> new ResourceNotFoundException("UserId " + userId + " not found"));
	}

	@GetMapping("/findone/{id}")
	public User findStudentById(@PathVariable("id") long idt) {
		Optional<User> st = userRepository.findById(idt);

		if (st.isEmpty()) {
			return null;
		}
		return st.get();
	}

	@PutMapping("/update/{id}")
	public User updateEmployee(@PathVariable long id, @RequestBody User requestUser) {
		return userRepository.findById(id).map(user -> {
			user.getUserProfile().setBiography(requestUser.getUserProfile().getBiography());
			User newUser = userRepository.save(user);
			Map<String, Object> header = new HashMap<>();
			header.put("eventType", "update");
			this.template.convertAndSend("/topic/users/realtime", newUser, header);
			this.template.convertAndSend("/topic/users/" + newUser.getId() + "/realtime", newUser, header);
			return newUser;
		}).orElseThrow(() -> new ResourceNotFoundException("UserId " + id + " not found"));
	}

	public User updateUserProfile(long employeeId, User user) {
		try {
			Optional<User> usOptional = userRepository.findById(employeeId);
			if (usOptional.isEmpty()) {
				return null;
			} else {
				usOptional.get().getUserProfile().setBiography(null);
				return userRepository.save(usOptional.get());

			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	@GetMapping("/currentuser")
	public ResponseEntity<?> currentUserNameSimple() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			UserDetailsImpl userDetailsImpl = (UserDetailsImpl) auth.getPrincipal();
			Optional<User> useOptional = userRepository.findByUsername(userDetailsImpl.getUsername());
			if (useOptional.isEmpty()) {
				return ResponseEntity.badRequest().body(new MessageResponse("Error: user is not logged"));
			}
			return ResponseEntity.ok().body(useOptional.get());
		}
		return ResponseEntity.badRequest().body(new MessageResponse("Error: user is not logged"));
	}

	@PostMapping("/deletecurrentuser")
	public ResponseEntity<?> deleteUser() {
		ResponseEntity<?> rEntity = currentUserNameSimple();
		if (rEntity.getStatusCodeValue() != 200) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Currentuser is not logged"));
		}
		Authentication authentication = (Authentication) rEntity.getBody();
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		userRepository.deleteById(userDetails.getId());
		return ResponseEntity.ok(userDetails);
	}

	@PostMapping("/delete/{id}")
	public ResponseEntity<?> deleteUserById(@PathVariable("id") long id) {
		Optional<User> userOptional = userRepository.findById(id);
		if (userOptional.isEmpty()) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: User with " + id + " is not found"));
		}
		User user = userOptional.get();

		UserDetailsImpl userDetailsImpl = UserDetailsImpl.build(user);
		userRepository.deleteById(userOptional.get().getId());
		Map<String, Object> header = new HashMap<>();
		header.put("eventType", "delete");
		this.template.convertAndSend("/topic/users/realtime", user, header);
		return ResponseEntity.ok(userDetailsImpl);
	}

}
