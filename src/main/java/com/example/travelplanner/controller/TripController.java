package com.example.travelplanner.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.travelplanner.dto.TripRequestDTO;
import com.example.travelplanner.dto.TripResponseDTO;
import com.example.travelplanner.service.TripService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/trips")
public class TripController {

    private final TripService tripService;

    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    @PostMapping
    public TripResponseDTO createTrip(
            Authentication authentication,
            @Valid @RequestBody TripRequestDTO request) {

        String email = authentication.getName();

        return tripService.createTrip(email, request);
    }

    @GetMapping
    public List<TripResponseDTO> getMyTrips(
            Authentication authentication) {

        String email = authentication.getName();

        return tripService.getMyTrips(email);
    }

    @PutMapping("/{tripId}")
    public TripResponseDTO updateTrip(
        @PathVariable Long tripId,
        Authentication authentication,
        @Valid @RequestBody TripRequestDTO request) {

    String email = authentication.getName();

    return tripService.updateTrip(
            tripId,
            email,
            request
    );
}

    @DeleteMapping("/{tripId}")
    public void deleteTrip(
        @PathVariable Long tripId,
        Authentication authentication) {

    String email = authentication.getName();

    tripService.deleteTrip(tripId, email);
}

}