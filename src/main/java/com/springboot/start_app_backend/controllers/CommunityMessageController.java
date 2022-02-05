package com.springboot.start_app_backend.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.start_app_backend.exceptions.ResourceNotFoundException;
import com.springboot.start_app_backend.models.CommunityMessage;
import com.springboot.start_app_backend.repositories.CommunityMessageRepository;
import com.springboot.start_app_backend.repositories.CommunityRepository;

import com.springboot.start_app_backend.repositories.UserRepository;

@RestController
@RequestMapping("/api/v1/communities_messages")
public class CommunityMessageController {
	@Autowired
	CommunityRepository communityRepository;
	@Autowired
	SimpMessagingTemplate template;
	@Autowired
	UserRepository userRepository;
	@Autowired
	CommunityMessageRepository communityMessageRepository;

	@GetMapping
	public Page<CommunityMessage> getAllCommunitiesMessages(Pageable pageable) {
		return communityMessageRepository.findAll(pageable);
	}

	@PostMapping("/communities/{communityId}/add")
	public CommunityMessage createCommunityMessage(@PathVariable(value = "communityId") Long communityId,
			@Valid @RequestBody CommunityMessage communityMessage) {
		return communityRepository.findById(communityId).map(community -> {
			communityMessage.setCommunity(community);
			CommunityMessage newCommunityMessage = communityMessageRepository.save(communityMessage);
			Map<String, Object> header = new HashMap<>();
			String value = "create";
			header.put("eventType", value);
			this.template.convertAndSend(
					"/topic/communities/" + community.getId() + "communities_messages" + "/realtime",
					newCommunityMessage, header);
			this.template.convertAndSend("/topic/communities/" + community.getId() + "communities_messages/"
					+ communityMessage.getId() + "/realtime", newCommunityMessage, header);
			return newCommunityMessage;
		}).orElseThrow(() -> new ResourceNotFoundException("CommunityId " + communityId + " not found"));
	}

}
