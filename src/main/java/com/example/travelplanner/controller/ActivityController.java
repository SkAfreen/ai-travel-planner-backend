package com.example.travelplanner.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.travelplanner.dto.ActivityRequestDTO;
import com.example.travelplanner.dto.ActivityResponseDTO;
import com.example.travelplanner.service.ActivityService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/destinations/{destinationId}/activities")
public class ActivityController {

    private final ActivityService activityService;

    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @PostMapping
    public ActivityResponseDTO createActivity(
            @PathVariable Long destinationId,
            @Valid @RequestBody ActivityRequestDTO request) {

        return activityService.createActivity(
                destinationId,
                request
        );
    }

    @GetMapping
    public List<ActivityResponseDTO> getActivities(
            @PathVariable Long destinationId) {

        return activityService.getActivitiesByDestination(
                destinationId
        );
    }
}