package com.springboot.start_app_backend.services;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.fasterxml.jackson.databind.ser.std.StdArraySerializers.LongArraySerializer;
import com.springboot.start_app_backend.exceptions.ResourceNotFoundException;
import com.springboot.start_app_backend.models.CommunityMessage;
import com.springboot.start_app_backend.models.User;
import com.springboot.start_app_backend.repositories.CommunityMessageRepository;
import com.springboot.start_app_backend.repositories.CommunityRepository;
import com.springboot.start_app_backend.repositories.UserRepository;

@Service
@Transactional
public class CommunityMessageService {
	@Autowired
	CommunityRepository communityRepository;
	@Autowired
	SimpMessagingTemplate template;
	@Autowired
	UserRepository userRepository;
	@Autowired
	CommunityMessageRepository communityMessageRepository;

	public Page<CommunityMessage> getAllCommunitiesMessages(Long communityId, Pageable pageable) {
		return communityMessageRepository.findByCommunityId(communityId, pageable);
	}

	public CommunityMessage createCommunityMessage(Long communityId, long userId, CommunityMessage communityMessage) {
		return communityRepository.findById(communityId).map(community -> {
			return userRepository.findById(userId).map(user -> {

				communityMessage.setCommunity(community);
				communityMessage.setAuthor(user);

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

			}).orElseThrow(() -> new ResourceNotFoundException("userId " + userId + " not found"));
		}).orElseThrow(() -> new ResourceNotFoundException("CommunityId " + communityId + " not found"));
	}

	public CommunityMessage updateCommunityMessage(Long messageId,
			@Valid @RequestBody CommunityMessage communityMessage) {

		return communityMessageRepository.findById(messageId).map(message -> {
			message.setContent(communityMessage.getContent());
			CommunityMessage newCommunityMessage = communityMessageRepository.save(message);
			Map<String, Object> header = new HashMap<>();
			String value = "update";
			header.put("eventType", value);
			this.template.convertAndSend("/topic/communities/" + newCommunityMessage.getCommunity().getId()
					+ "communities_messages" + "/realtime", newCommunityMessage, header);
			this.template.convertAndSend("/topic/communities/" + newCommunityMessage.getCommunity().getId()
					+ "communities_messages/" + communityMessage.getId() + "/realtime", newCommunityMessage, header);
			return newCommunityMessage;
		}).orElseThrow(() -> new ResourceNotFoundException("messageId " + messageId + " not found"));

	}

	public CommunityMessage deleteCommunityMessage(Long messageId, CommunityMessage communityMessage) {

		return communityMessageRepository.findById(messageId).map(message -> {
			message.setContent(communityMessage.getContent());
			communityMessageRepository.delete(message);
			Map<String, Object> header = new HashMap<>();
			String value = "delete";
			header.put("eventType", value);
			this.template.convertAndSend(
					"/topic/communities/" + message.getCommunity().getId() + "communities_messages" + "/realtime",
					message, header);
			this.template.convertAndSend("/topic/communities/" + message.getCommunity().getId()
					+ "communities_messages/" + communityMessage.getId() + "/realtime", message, header);
			return message;
		}).orElseThrow(() -> new ResourceNotFoundException("messageId " + messageId + " not found"));

	}

}
