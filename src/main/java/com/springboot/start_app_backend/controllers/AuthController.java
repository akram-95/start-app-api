package com.springboot.start_app_backend.controllers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.origin.SystemEnvironmentOrigin;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.start_app_backend.enums.ERole;
import com.springboot.start_app_backend.models.JwtResponse;
import com.springboot.start_app_backend.models.MessageResponse;
import com.springboot.start_app_backend.models.Role;
import com.springboot.start_app_backend.models.SigninRequest;
import com.springboot.start_app_backend.models.SignupRequest;
import com.springboot.start_app_backend.models.User;
import com.springboot.start_app_backend.models.UserDetailsImpl;
import com.springboot.start_app_backend.models.UserProfile;
import com.springboot.start_app_backend.repositories.RoleRepository;
import com.springboot.start_app_backend.repositories.TokenRepository;
import com.springboot.start_app_backend.repositories.UserRepository;
import com.springboot.start_app_backend.services.UserService;
import com.springboot.start_app_backend.utils.JwtUtils;
import com.springboot.start_app_backend.utils.Utils;

import ch.qos.logback.classic.pattern.DateConverter;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
	private static final String jwtTokenCookieName = "JWT-TOKEN";
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;
	@Autowired
	TokenRepository tokenRepository;

	@Autowired
	PasswordEncoder encoder;
	@Autowired
	private UserService userService;
	@Autowired
	SimpMessagingTemplate template;
	@Autowired
	UserController userProfileController;
	

	@Autowired
	JwtUtils jwtUtils;
	
	 
    // This annotation makes sure that the method needs to be executed after 
    // dependency injection is done to perform any initialization.
  
	
	@PostMapping("/signout")
	public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
		
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    if (auth != null){    
	        new SecurityContextLogoutHandler().logout(request, response, auth);
	    }
	    return "redirect:/login?logout"; //You can redirect wherever you want, but generally it's a good practice to show login screen again.
	}

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(HttpServletResponse httpServletResponse, @Valid @RequestBody SigninRequest loginRequest) {
		if (loginRequest.getUsername().isEmpty()) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Username is must'nt be empty!"));
		}
		if (loginRequest.getPassword().isEmpty()) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Password is must'nt be empty!"));
		}
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);
		
	 
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		   Optional<User> useOptional = userRepository.findByUsername(userDetails.getUsername());
		    if(useOptional.isEmpty()) {
		    	return  ResponseEntity
						.badRequest()
						.body(new MessageResponse("Error: user is not logged"));
		    }
		List<String> roles = userDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList());
	
		
		return ResponseEntity.ok(new JwtResponse(jwt, 
												 userDetails.getId(), 
												 userDetails.getUsername(), 
												 userDetails.getEmail(), 
												 roles));
	}


	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
		 System.out.println("aa");
		 System.out.println(signUpRequest.getRole());
		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Username is already taken!"));
		}
		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Email is already in use!"));
		}
		if(!Utils.isValid(signUpRequest.getPassword())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Password is not valid"));
		}
		

		// Create new user's account
		User user = new User(signUpRequest.getUsername(), 
							 signUpRequest.getEmail(),
							 encoder.encode(signUpRequest.getPassword()));

		Set<String> strRoles = signUpRequest.getRole();
		Set<Role> roles = new HashSet<>();

		if (strRoles == null) {
			Role userRole = roleRepository.findByName(ERole.ROLE_USER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(userRole);
		} else {
			strRoles.forEach(role -> {
				switch (role) {
				
				case "user":
					System.out.println(role);
					Role adminRole = roleRepository.findByName(ERole.ROLE_USER)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(adminRole);

					break;
				default:
					Role userRole = roleRepository.findByName(ERole.ROLE_USER)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(userRole);
				}
			});
		}
		
		user.setRoles(roles);
		UserProfile userProfile = new UserProfile("a",user);
		user.setUserProfile(userProfile);
		userProfile.setUser(user);
	  	User newUser = userRepository.save(user);
	  	Map<String, Object> header = new HashMap<>();
		header.put("eventType", "create");
		this.template.convertAndSend("/topic/user_profiles/realtime", user.getUserProfile(), header);
	    return ResponseEntity.ok(newUser);
	}
	@PostMapping("/forgot-password")
	public String forgotPassword(@RequestParam String email) {

		String response = userService.forgotPassword(email);

		if (!response.startsWith("Invalid")) {
			response = "http://localhost:8080/reset-password?token=" + response;
		}
		return response;
	}

	@PutMapping("/reset-password")
	public String resetPassword(@RequestParam String token,
			@RequestParam String password) {
		return userService.resetPassword(token, password);
	}
}
