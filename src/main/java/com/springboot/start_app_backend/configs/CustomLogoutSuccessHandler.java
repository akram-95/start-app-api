package com.springboot.start_app_backend.configs;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

import com.springboot.start_app_backend.models.UserDetailsImpl;
import com.springboot.start_app_backend.services.UserDetailsServiceImpl;
@Component
public class CustomLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {
 
    @Autowired
    private UserDetailsServiceImpl customerService;
 
    @Override
    public void onLogoutSuccess(HttpServletRequest request,
                HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
    	if(authentication == null) {
    		System.out.println("user alread out logged");
    		 super.onLogoutSuccess(request, response, authentication);
    	}else {
         
    	UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String username = userDetails.getUsername();
         
        UserDetails customer = customerService.loadUserByUsername(username);
        System.out.println(customer.getPassword());
         
        // process logics with customer
         
        super.onLogoutSuccess(request, response, authentication);
    	}
    }  
}
