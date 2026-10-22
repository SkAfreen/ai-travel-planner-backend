package com.example.travelplanner.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.travelplanner.dto.ItineraryDayRequestDTO;
import com.example.travelplanner.dto.ItineraryDayResponseDTO;
import com.example.travelplanner.service.ItineraryDayService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/trips/{tripId}/itinerary/days")
public class ItineraryDayController {

    private final ItineraryDayService itineraryDayService;

    public ItineraryDayController(
            ItineraryDayService itineraryDayService) {
        this.itineraryDayService = itineraryDayService;

    }

    @PostMapping
    public ItineraryDayResponseDTO createDay(
            @PathVariable Long tripId,
            Authentication authentication,
            @Valid @RequestBody ItineraryDayRequestDTO request) {

        String email = authentication.getName();

        return itineraryDayService.createDay(
                tripId,
                email,
                request
        );
    }

    @GetMapping
    public List<ItineraryDayResponseDTO> getDays(
            @PathVariable Long tripId,
            Authentication authentication) {

        String email = authentication.getName();

        return itineraryDayService.getDays(
                tripId,
                email
        );
    }

    @PutMapping("/{dayId}")
    public ItineraryDayResponseDTO updateDay(
            @PathVariable Long tripId,
            @PathVariable Long dayId,
            Authentication authentication,
            @Valid @RequestBody ItineraryDayRequestDTO request) {

        String email = authentication.getName();

        return itineraryDayService.updateDay(
                tripId,
                dayId,
                email,
                request
        );
    }
}
