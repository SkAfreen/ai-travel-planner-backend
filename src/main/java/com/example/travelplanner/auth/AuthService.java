package com.example.travelplanner.auth;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.travelplanner.dto.UserResponseDTO;
import com.example.travelplanner.entity.User;
import com.example.travelplanner.repository.UserRepository;
import com.example.travelplanner.exception.InvalidCredentialsException;
import com.example.travelplanner.exception.ForbiddenException;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public LoginResponseDTO login(LoginRequestDTO request) {

        User user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(()
                        -> new InvalidCredentialsException("Invalid email or password"));

        // Step 1: Verify password
        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword())) {

            throw new InvalidCredentialsException(
                    "Invalid email or password"
            );
        }

// Step 2: Check whether the email is verified
        if (!user.isEmailVerified()) {
            throw new ForbiddenException(
                    "Please verify your email address before logging in."
            );
        }

// Step 3: Generate JWT only for verified users
        String token = jwtService.generateToken(user.getEmail());

        UserResponseDTO userResponse = new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getCountry(),
                user.getPreferredCurrency()
        );

        return new LoginResponseDTO(token, userResponse);
    }
}
