package com.springboot.start_app_backend.controllers;

import java.util.*;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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

import com.springboot.start_app_backend.enums.RealTimeEventType;
import com.springboot.start_app_backend.exceptions.ResourceNotFoundException;

import com.springboot.start_app_backend.models.MessageResponse;
import com.springboot.start_app_backend.models.User;
import com.springboot.start_app_backend.models.UserDetailsImpl;
import com.springboot.start_app_backend.repositories.UserRepository;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {


    @Autowired
    SimpMessagingTemplate template;
    @Autowired
    UserRepository userRepository;


    @GetMapping
    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @PostMapping("/{userId}/create_profile")
    public ResponseEntity<?> createProfile(@PathVariable("userId") long userId,
                                           @Valid @RequestBody User requestedUser) {
        return userRepository.findById(userId).map(user -> {
            user.setBiography(requestedUser.getBiography());
            user.setSlogan(requestedUser.getSlogan());
            user.setProfileUrl(requestedUser.getProfileUrl());
            user.setSkills(requestedUser.getSkills());
            user.setExperiences(requestedUser.getExperiences());
            Map<String, Object> header = new HashMap<>();
            String value = RealTimeEventType.CREATE.name();
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
            user.setBiography(requestUser.getBiography());
            user.setExperiences(requestUser.getExperiences());
            user.setSkills(requestUser.getSkills());
            user.setProfileUrl(requestUser.getProfileUrl());
            user.setSlogan(requestUser.getSlogan());
            User newUser = userRepository.save(user);
            Map<String, Object> header = new HashMap<>();
            header.put("eventType", RealTimeEventType.UPDATE.name());
            this.template.convertAndSend("/topic/users/realtime", newUser, header);
            this.template.convertAndSend("/topic/users/" + newUser.getId() + "/realtime", newUser, header);
            return newUser;
        }).orElseThrow(() -> new ResourceNotFoundException("UserId " + id + " not found"));
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
        header.put("eventType", RealTimeEventType.DELETE.name());
        this.template.convertAndSend("/topic/users/realtime", user, header);
        return ResponseEntity.ok(userDetailsImpl);
    }

    @GetMapping("/following/{id}")
    public ResponseEntity<?> following(@PathVariable long id, Pageable pageable) {
        return ResponseEntity.ok(userRepository.findByFromId(id, pageable));
    }

    @GetMapping("/followers/{id}")
    public ResponseEntity<?> followers(@PathVariable long id, Pageable pageable) {
        return ResponseEntity.ok(userRepository.findByToId(id, pageable));
    }

}
