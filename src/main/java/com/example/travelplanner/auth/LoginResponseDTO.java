package com.example.travelplanner.auth;

import com.example.travelplanner.dto.UserResponseDTO;


public class LoginResponseDTO {

    private String token;
    private UserResponseDTO user;

    public LoginResponseDTO(String token, UserResponseDTO user) {
        this.token = token;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public UserResponseDTO getUser() {
        return user;
    }
}