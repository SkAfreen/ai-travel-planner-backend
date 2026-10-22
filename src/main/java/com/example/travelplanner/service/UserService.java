package com.example.travelplanner.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.travelplanner.dto.UserRequestDTO;
import com.example.travelplanner.dto.UserResponseDTO;
import com.example.travelplanner.entity.User;
import com.example.travelplanner.exception.ConflictException;
import com.example.travelplanner.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponseDTO createUser(UserRequestDTO request) {

        // Check whether email is already registered
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ConflictException(
                    "Email is already registered");
        }

        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(
                passwordEncoder.encode(request.getPassword()));
        user.setCountry(request.getCountry());
        user.setPreferredCurrency(request.getPreferredCurrency());

        User savedUser = userRepository.save(user);

        return new UserResponseDTO(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getCountry(),
                savedUser.getPreferredCurrency()
        );
    }

    public List<UserResponseDTO> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(user -> new UserResponseDTO(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getCountry(),
                        user.getPreferredCurrency()
                ))
                .toList();
    }
}