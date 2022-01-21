package com.springboot.start_app_backend.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.origin.SystemEnvironmentOrigin;
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
import org.springframework.web.bind.annotation.RestController;

import com.springboot.start_app_backend.models.Post;
import com.springboot.start_app_backend.repositories.PostRepository;
import com.springboot.start_app_backend.repositories.UserRepository;
import com.springboot.start_app_backend.exceptions.ResourceNotFoundException;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {
	@Autowired
	private PostRepository postRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	SimpMessagingTemplate template;

	@GetMapping
	public Page<Post> getAllPosts(Pageable pageable) {
		return postRepository.findAll(pageable);
	}

	@PostMapping("/{userId}/add")
	public Post createPost(@PathVariable(value = "userId") Long userId, @Valid @RequestBody Post post) {
		return userRepository.findById(userId).map(user -> {
			post.setUser(user);
			Map<String, Object> header = new HashMap<>();
			String value = "create";
			header.put("eventType", value);
			
			Post newPost =  postRepository.save(post);
			this.template.convertAndSend("/topic/posts/realtime", newPost, header);
			return newPost;
		}).orElseThrow(() -> new ResourceNotFoundException("UserId " + userId + " not found"));
	}

	@PutMapping("/update/{postId}")
	public Post updatePost(@PathVariable Long postId, @Valid @RequestBody Post postRequest) {
		return postRepository.findById(postId).map(post -> {
			post.setTitle(postRequest.getTitle());
			post.setContent(postRequest.getContent());
			Map<String, Object> header = new HashMap<>();
			String value = "update";
			header.put("eventType", value);
			this.template.convertAndSend("/topic/posts/realtime", post, header);
			return postRepository.save(post);
		}).orElseThrow(() -> new ResourceNotFoundException("PostId " + postId + " not found"));
	}

	@DeleteMapping("/delete/{postId}")
	public ResponseEntity<?> deletePost(@PathVariable Long postId) {
		return postRepository.findById(postId).map(post -> {
			postRepository.delete(post);
			Map<String, Object> header = new HashMap<>();
			String value = "delete";
			
			header.put("eventType", value);
			this.template.convertAndSend("/topic/posts/realtime", post, header);
			return ResponseEntity.ok().build();
		}).orElseThrow(() -> new ResourceNotFoundException("PostId " + postId + " not found"));
	}

}
