package com.springboot.start_app_backend.utils;

import org.springframework.security.web.AuthenticationEntryPoint;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.origin.SystemEnvironmentOrigin;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {
	

	private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);


	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		logger.error("Unauthorized error: {}", authException.getMessage());
		logger.error("Value ", authException.toString());
	       response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		   response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		   System.out.println(authException.toString() + "awa");
		   final Map<String, Object> body = new HashMap<>();
		   if(authException.toString().contains("Bad credentials")) {  
		        body.put("code", HttpServletResponse.SC_UNAUTHORIZED);
		        body.put("payload", "Bad Credentials , user not found");
		   }else if(authException.getMessage().contains("Full authentication")) {
			   body.put("code", HttpServletResponse.SC_UNAUTHORIZED);
		       body.put("payload", "Full authentication , Full authentication is required to access this resource");
		   }
	        final ObjectMapper mapper = new ObjectMapper();
	        mapper.writeValue(response .getOutputStream(), body);
	}

}