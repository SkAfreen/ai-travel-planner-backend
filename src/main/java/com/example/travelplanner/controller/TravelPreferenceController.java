package com.example.travelplanner.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.travelplanner.dto.TravelPreferenceRequestDTO;
import com.example.travelplanner.dto.TravelPreferenceResponseDTO;
import com.example.travelplanner.service.TravelPreferenceService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/travel-preferences")
public class TravelPreferenceController {

    private final TravelPreferenceService travelPreferenceService;

    public TravelPreferenceController(
            TravelPreferenceService travelPreferenceService) {
        this.travelPreferenceService = travelPreferenceService;
    }

   @PostMapping
public TravelPreferenceResponseDTO createPreference(
        Authentication authentication,
        @Valid @RequestBody TravelPreferenceRequestDTO request) {

    String email = authentication.getName();

    return travelPreferenceService.createPreference(email, request);
}

@GetMapping
public TravelPreferenceResponseDTO getPreference(
        Authentication authentication) {

    String email = authentication.getName();

    return travelPreferenceService.getPreference(email);
}

@PutMapping
public TravelPreferenceResponseDTO updatePreference(
        Authentication authentication,
        @Valid @RequestBody TravelPreferenceRequestDTO request) {

    String email = authentication.getName();

    return travelPreferenceService.updatePreference(email, request);
}

    @DeleteMapping
    public void deletePreference(Authentication authentication) {

    String email = authentication.getName();

    travelPreferenceService.deletePreference(email);
}

}
