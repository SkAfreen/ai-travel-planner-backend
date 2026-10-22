package com.example.travelplanner.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.travelplanner.dto.AIGeneratedItineraryDTO;
import com.example.travelplanner.service.AIItineraryService;

@RestController
@RequestMapping("/api/ai")
public class AIItineraryController {

    private final AIItineraryService aiItineraryService;

    public AIItineraryController(AIItineraryService aiItineraryService) {
        this.aiItineraryService = aiItineraryService;
    }

    @PostMapping("/trips/{tripId}/generate")
    public AIGeneratedItineraryDTO generateItinerary(
            @PathVariable Long tripId,
            Authentication authentication) {

        String email = authentication.getName();

        return aiItineraryService.generateItinerary(tripId, email);
    }
}