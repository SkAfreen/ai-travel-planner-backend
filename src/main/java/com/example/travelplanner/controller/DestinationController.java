package com.example.travelplanner.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.travelplanner.dto.DestinationRequestDTO;
import com.example.travelplanner.dto.DestinationResponseDTO;
import com.example.travelplanner.service.DestinationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/destinations")
public class DestinationController {

    private final DestinationService destinationService;

    public DestinationController(
            DestinationService destinationService) {
        this.destinationService = destinationService;
    }

    @PostMapping
    public DestinationResponseDTO createDestination(
            @Valid @RequestBody DestinationRequestDTO request) {

        return destinationService.createDestination(request);
    }

    @GetMapping
    public List<DestinationResponseDTO> getAllDestinations() {

        return destinationService.getAllDestinations();
    }
}