package com.example.travelplanner.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.travelplanner.dto.ActivityRequestDTO;
import com.example.travelplanner.dto.ActivityResponseDTO;
import com.example.travelplanner.entity.Activity;
import com.example.travelplanner.entity.Destination;
import com.example.travelplanner.exception.ResourceNotFoundException;
import com.example.travelplanner.repository.ActivityRepository;
import com.example.travelplanner.repository.DestinationRepository;

@Service
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final DestinationRepository destinationRepository;

    public ActivityService(
            ActivityRepository activityRepository,
            DestinationRepository destinationRepository) {

        this.activityRepository = activityRepository;
        this.destinationRepository = destinationRepository;
    }

    public ActivityResponseDTO createActivity(
            Long destinationId,
            ActivityRequestDTO request) {

        Destination destination =
                destinationRepository.findById(destinationId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Destination not found"));

        Activity activity = new Activity();

        activity.setName(request.getName());
        activity.setDescription(request.getDescription());
        activity.setCategory(request.getCategory());
        activity.setEstimatedCost(request.getEstimatedCost());
        activity.setDestination(destination);

        Activity savedActivity =
                activityRepository.save(activity);

        return new ActivityResponseDTO(
                savedActivity.getId(),
                savedActivity.getName(),
                savedActivity.getDescription(),
                savedActivity.getCategory(),
                savedActivity.getEstimatedCost(),
                savedActivity.getDestination().getId()
        );
    }

    public List<ActivityResponseDTO> getActivitiesByDestination(
            Long destinationId) {

        if (!destinationRepository.existsById(destinationId)) {
            throw new ResourceNotFoundException(
                    "Destination not found");
        }

        return activityRepository
                .findByDestinationId(destinationId)
                .stream()
                .map(activity -> new ActivityResponseDTO(
                        activity.getId(),
                        activity.getName(),
                        activity.getDescription(),
                        activity.getCategory(),
                        activity.getEstimatedCost(),
                        activity.getDestination().getId()
                ))
                .toList();
    }
}