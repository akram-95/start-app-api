package com.springboot.start_app_backend.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.origin.SystemEnvironmentOrigin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.start_app_backend.exceptions.ResourceNotFoundException;
import com.springboot.start_app_backend.models.CommunityMessage;
import com.springboot.start_app_backend.models.User;
import com.springboot.start_app_backend.repositories.CommunityMessageRepository;
import com.springboot.start_app_backend.repositories.CommunityRepository;

import com.springboot.start_app_backend.repositories.UserRepository;
import com.springboot.start_app_backend.services.CommunityMessageService;

@RestController
@RequestMapping("/api/v1/communities/communities_messages")
@Transactional
public class CommunityMessageController {
	@Autowired
	CommunityRepository communityRepository;
	@Autowired
	SimpMessagingTemplate template;
	@Autowired
	UserRepository userRepository;
	@Autowired
	CommunityMessageRepository communityMessageRepository;
	@Autowired
	CommunityMessageService communityMessageService;

	@GetMapping("/{communityId}")
	public Page<CommunityMessage> getAllCommunitiesMessages(@PathVariable(value = "communityId") Long communityId,
			Pageable pageable) {
		return communityMessageService.getAllCommunitiesMessages(communityId, pageable);
	}

	@PostMapping("/{userId}/{communityId}/add")
	public CommunityMessage createCommunityMessage(@PathVariable(value = "userId") Long userId,
			@PathVariable(value = "communityId") Long communityId,
			@Valid @RequestBody CommunityMessage communityMessage) {
		return communityMessageService.createCommunityMessage(communityId, userId, communityMessage);
	}

	@PutMapping("/{messageId}/update")
	public CommunityMessage updateCommunityMessage(@PathVariable(value = "messageId") Long messageId,
			@Valid @RequestBody CommunityMessage communityMessage) {
		return communityMessageService.updateCommunityMessage(messageId, communityMessage);

	}

	@DeleteMapping("/{messageId}/delete")
	public CommunityMessage deleteCommunityMessage(@PathVariable(value = "messageId") Long messageId,
			@Valid @RequestBody CommunityMessage communityMessage) {
		return communityMessageService.deleteCommunityMessage(messageId, communityMessage);

	}

}
