package com.example.travelplanner.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.travelplanner.dto.ItineraryRequestDTO;
import com.example.travelplanner.dto.ItineraryResponseDTO;
import com.example.travelplanner.service.ItineraryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/trips/{tripId}/itinerary")
public class ItineraryController {

    private final ItineraryService itineraryService;

    public ItineraryController(
            ItineraryService itineraryService) {
        this.itineraryService = itineraryService;
    }

    @PostMapping
    public ItineraryResponseDTO createItinerary(
            @PathVariable Long tripId,
            Authentication authentication,
            @Valid @RequestBody ItineraryRequestDTO request) {

        String email = authentication.getName();

        return itineraryService.createItinerary(
                tripId,
                email,
                request
        );
    }

    @GetMapping
    public ItineraryResponseDTO getItinerary(
        @PathVariable Long tripId,
        Authentication authentication) {

    String email = authentication.getName();

    return itineraryService.getItinerary(
            tripId,
            email
    );
}

}