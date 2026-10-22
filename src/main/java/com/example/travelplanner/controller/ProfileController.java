package com.example.travelplanner.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProfileController {

    @GetMapping("/api/profile")
    public String getProfile(Authentication authentication) {

        return "Hello " + authentication.getName()
                + ", you are authenticated!";
    }
}