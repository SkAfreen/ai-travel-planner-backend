package com.example.travelplanner.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.travelplanner.dto.DestinationRequestDTO;
import com.example.travelplanner.dto.DestinationResponseDTO;
import com.example.travelplanner.entity.Destination;
import com.example.travelplanner.repository.DestinationRepository;

@Service
public class DestinationService {

    private final DestinationRepository destinationRepository;

    public DestinationService(DestinationRepository destinationRepository) {
        this.destinationRepository = destinationRepository;
    }

    public DestinationResponseDTO createDestination(
            DestinationRequestDTO request) {

        Destination destination = new Destination();

        destination.setName(request.getName());
        destination.setCountry(request.getCountry());
        destination.setDescription(request.getDescription());

        Destination savedDestination =
                destinationRepository.save(destination);

        return new DestinationResponseDTO(
                savedDestination.getId(),
                savedDestination.getName(),
                savedDestination.getCountry(),
                savedDestination.getDescription()
        );
    }

    public List<DestinationResponseDTO> getAllDestinations() {

        return destinationRepository.findAll()
                .stream()
                .map(destination -> new DestinationResponseDTO(
                        destination.getId(),
                        destination.getName(),
                        destination.getCountry(),
                        destination.getDescription()
                ))
                .toList();
    }
}