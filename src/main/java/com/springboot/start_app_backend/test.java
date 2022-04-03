package com.springboot.start_app_backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class test {

    @Autowired
    private SimpMessagingTemplate kafkaTemplate;

    @GetMapping
    public String getAllUsers() {
        try {

            return "Hello";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

}
