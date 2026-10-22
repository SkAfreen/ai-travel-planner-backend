package com.example.travelplanner.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.travelplanner.dto.TripRequestDTO;
import com.example.travelplanner.dto.TripResponseDTO;
import com.example.travelplanner.entity.Trip;
import com.example.travelplanner.entity.User;
import com.example.travelplanner.exception.ForbiddenException;
import com.example.travelplanner.exception.ResourceNotFoundException;
import com.example.travelplanner.repository.TripRepository;
import com.example.travelplanner.repository.UserRepository;

@Service
public class TripService {

    private final TripRepository tripRepository;
    private final UserRepository userRepository;

    public TripService(
            TripRepository tripRepository,
            UserRepository userRepository) {

        this.tripRepository = tripRepository;
        this.userRepository = userRepository;
    }

    public TripResponseDTO createTrip(
            String email,
            TripRequestDTO request) {

        User user = userRepository.findByEmail(email)
        .orElseThrow(() ->
                new ResourceNotFoundException("User not found"));

        Trip trip = new Trip();

        trip.setDestination(request.getDestination());
        trip.setStartDate(request.getStartDate());
        trip.setEndDate(request.getEndDate());
        trip.setNumberOfTravelers(
                request.getNumberOfTravelers());
        trip.setBudget(request.getBudget());
        trip.setUser(user);

        Trip savedTrip = tripRepository.save(trip);

        return new TripResponseDTO(
                savedTrip.getId(),
                savedTrip.getDestination(),
                savedTrip.getStartDate(),
                savedTrip.getEndDate(),
                savedTrip.getNumberOfTravelers(),
                savedTrip.getBudget()
        );
    }

    public List<TripResponseDTO> getMyTrips(String email) {

        User user = userRepository.findByEmail(email)
        .orElseThrow(() ->
                new ResourceNotFoundException("User not found"));

        return tripRepository.findByUserId(user.getId())
                .stream()
                .map(trip -> new TripResponseDTO(
                        trip.getId(),
                        trip.getDestination(),
                        trip.getStartDate(),
                        trip.getEndDate(),
                        trip.getNumberOfTravelers(),
                        trip.getBudget()
                ))
                .toList();
    }

    public TripResponseDTO updateTrip(
        Long tripId,
        String email,
        TripRequestDTO request) {

    User user = userRepository.findByEmail(email)
        .orElseThrow(() ->
                new ResourceNotFoundException("User not found"));

    Trip trip = tripRepository.findById(tripId)
        .orElseThrow(() ->
                new ResourceNotFoundException("Trip not found"));

    if (!trip.getUser().getId().equals(user.getId())) {
    throw new ForbiddenException(
            "You cannot modify this trip");
}

    trip.setDestination(request.getDestination());
    trip.setStartDate(request.getStartDate());
    trip.setEndDate(request.getEndDate());
    trip.setNumberOfTravelers(
            request.getNumberOfTravelers());
    trip.setBudget(request.getBudget());

    Trip updatedTrip = tripRepository.save(trip);

    return new TripResponseDTO(
            updatedTrip.getId(),
            updatedTrip.getDestination(),
            updatedTrip.getStartDate(),
            updatedTrip.getEndDate(),
            updatedTrip.getNumberOfTravelers(),
            updatedTrip.getBudget()
    );
}

    public void deleteTrip(Long tripId, String email) {

    User user = userRepository.findByEmail(email)
        .orElseThrow(() ->
                new ResourceNotFoundException("User not found"));
    Trip trip = tripRepository.findById(tripId)
        .orElseThrow(() ->
                new ResourceNotFoundException("Trip not found"));

    if (!trip.getUser().getId().equals(user.getId())) {
    throw new ForbiddenException(
            "You cannot delete this trip");
}

    tripRepository.delete(trip);
}

}