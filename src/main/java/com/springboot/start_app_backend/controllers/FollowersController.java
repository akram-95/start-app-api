package com.springboot.start_app_backend.controllers;

import java.util.*;
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
import org.springframework.web.bind.annotation.*;

import com.springboot.start_app_backend.enums.RealTimeEventType;
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
            Followers savedFollowers = followersRepository
                    .save(new Followers(fromUserOptional.get(), toUserOptional.get()));
            toUserOptional.get().getFollowers().add(savedFollowers);
            fromUserOptional.get().getFollowing().add(savedFollowers);
            return ResponseEntity.ok("you follow this user successfully");
        }
        return ResponseEntity.ok().body("you follow already this user");

    }

    @GetMapping("/isFollowing/{fromId}/{toId}")
    public ResponseEntity<?> isFollowing(@PathVariable long fromId, @PathVariable long toId) {
        return ResponseEntity.ok(followersRepository.existsByFromIdAndToId(fromId, toId));
    }

    @DeleteMapping("/unfollow/{fromId}/{toId}")
    @Transactional
    public ResponseEntity<?> unfollow(@PathVariable long fromId, @PathVariable long toId, Pageable pageable) {
        List<Followers> followers = followersRepository.findByFromIdAndToId(fromId, toId, pageable).toList();
        if (!followers.isEmpty()) {
            followersRepository.deleteAll(followers);
            Optional<User> toUserOptional = userRepository.findById(toId);
            Optional<User> fromUserOptional = userRepository.findById(fromId);
            toUserOptional.get().getFollowers().removeAll(followers);
            fromUserOptional.get().getFollowing().removeAll(followers);
            return ResponseEntity.ok("you don't follow this user more");
        }
        return ResponseEntity.ok().body("you don't follow this user already ");

    }

    @GetMapping("/followers/{id}")
    public ResponseEntity<?> followers(@PathVariable long id, Pageable pageable) {
        return ResponseEntity.ok(followersRepository.findByToId(id, pageable));
    }

    @GetMapping("/following/{id}")
    public ResponseEntity<?> following(@PathVariable long id, Pageable pageable) {
        return ResponseEntity.ok(followersRepository.findByFromId(id, pageable));
    }

    @GetMapping("/FollowingByToIdAndFromIds/{id}")
    public ResponseEntity<?> getFollowingByToIdAndFromIds(@PathVariable(value = "id") Long id, @RequestParam(value = "ids", defaultValue = "") List<Long> userIds, Pageable pageable) {
        return ResponseEntity.ok(followersRepository.findByFromIdAndToIds(userIds, id, pageable));
    }

    @GetMapping("/FollowersByToIdAndFromIds/{id}")
    public ResponseEntity<?> getFollowersByToIdAndFromIds(@PathVariable(value = "id") Long id, @RequestParam(value = "ids", defaultValue = "") List<Long> userIds, Pageable pageable) {
        try {
            return ResponseEntity.ok(followersRepository.findByFromIdsAndToId(userIds, id, pageable));
        } catch (Exception e) {
            System.out.println("error" + e.getMessage());
            return ResponseEntity.badRequest().body(e.toString());
        }
    }
}
