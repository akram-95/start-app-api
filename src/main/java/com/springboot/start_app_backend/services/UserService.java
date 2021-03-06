package com.springboot.start_app_backend.services;

import java.time.*;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.springboot.start_app_backend.models.User;
import com.springboot.start_app_backend.repositories.UserRepository;
@Service
public class UserService {

	private static final long EXPIRE_TOKEN_AFTER_MINUTES = 30;

	@Autowired
	private UserRepository userRepository;
	@Autowired
	PasswordEncoder encoder;

	public String forgotPassword(String email) {

		Optional<User> userOptional =userRepository.findByEmail(email);

		if (!userOptional.isPresent()) {
			return "Invalid email id.";
		}

		User user = userOptional.get();
		

		user = userRepository.save(user);

		return user.getEmail();
	}

	public String resetPassword(String token, String password) {

		Optional<User> userOptional = null;
				

		if (!userOptional.isPresent()) {
			return "Invalid token.";
		}

		User user = userOptional.get();

		user.setPassword(encoder.encode(password));
		

		userRepository.save(user);

		return "Your password successfully updated.";
	}

	/**
	 * Generate unique token. You may add multiple parameters to create a strong
	 * token.
	 * 
	 * @return unique token
	 */
	private String generateToken() {
		StringBuilder token = new StringBuilder();

		return token.append(UUID.randomUUID().toString())
				.append(UUID.randomUUID().toString()).toString();
	}

	/**
	 * Check whether the created token expired or not.
	 * 
	 * @param tokenCreationDate
	 * @return true or false
	 */
	private boolean isTokenExpired(final LocalDateTime tokenCreationDate) {

		LocalDateTime now = LocalDateTime.now();
		Duration diff = Duration.between(tokenCreationDate, now);

		return diff.toMinutes() >= EXPIRE_TOKEN_AFTER_MINUTES;
	}
}
