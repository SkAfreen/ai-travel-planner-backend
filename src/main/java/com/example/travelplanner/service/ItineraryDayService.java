package com.example.travelplanner.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.travelplanner.dto.ItineraryDayRequestDTO;
import com.example.travelplanner.dto.ItineraryDayResponseDTO;
import com.example.travelplanner.entity.Itinerary;
import com.example.travelplanner.entity.ItineraryDay;
import com.example.travelplanner.entity.Trip;
import com.example.travelplanner.entity.User;
import com.example.travelplanner.exception.ForbiddenException;
import com.example.travelplanner.exception.ResourceNotFoundException;
import com.example.travelplanner.repository.ItineraryDayRepository;
import com.example.travelplanner.repository.ItineraryRepository;
import com.example.travelplanner.repository.TripRepository;
import com.example.travelplanner.repository.UserRepository;

@Service
public class ItineraryDayService {

    private final ItineraryDayRepository itineraryDayRepository;
    private final ItineraryRepository itineraryRepository;
    private final TripRepository tripRepository;
    private final UserRepository userRepository;

    public ItineraryDayService(
            ItineraryDayRepository itineraryDayRepository,
            ItineraryRepository itineraryRepository,
            TripRepository tripRepository,
            UserRepository userRepository) {

        this.itineraryDayRepository = itineraryDayRepository;
        this.itineraryRepository = itineraryRepository;
        this.tripRepository = tripRepository;
        this.userRepository = userRepository;
    }

    public ItineraryDayResponseDTO createDay(
            Long tripId,
            String email,
            ItineraryDayRequestDTO request) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(()
                        -> new ResourceNotFoundException("User not found"));

        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(()
                        -> new ResourceNotFoundException("Trip not found"));

        if (!trip.getUser().getId().equals(user.getId())) {
            throw new ForbiddenException("You cannot access this itinerary");
        }

        Itinerary itinerary
                = itineraryRepository.findByTripId(tripId)
                        .orElseThrow(()
                                -> new RuntimeException(
                                "Itinerary not found"));

        ItineraryDay day = new ItineraryDay();

        day.setDayNumber(request.getDayNumber());
        day.setTitle(request.getTitle());
        day.setActivitiesDescription(
                request.getActivitiesDescription());
        day.setItinerary(itinerary);

        ItineraryDay savedDay
                = itineraryDayRepository.save(day);

        return new ItineraryDayResponseDTO(
                savedDay.getId(),
                savedDay.getDayNumber(),
                savedDay.getTitle(),
                savedDay.getActivitiesDescription(),
                savedDay.getItinerary().getId()
        );
    }

    public List<ItineraryDayResponseDTO> getDays(
            Long tripId,
            String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(()
                        -> new ResourceNotFoundException("User not found"));

        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(()
                        -> new ResourceNotFoundException("Trip not found"));

        if (!trip.getUser().getId().equals(user.getId())) {
            throw new ForbiddenException("You cannot access this itinerary");
        }

        Itinerary itinerary
                = itineraryRepository.findByTripId(tripId)
                        .orElseThrow(()
                                -> new ResourceNotFoundException("Itinerary not found"));

        return itineraryDayRepository
                .findByItineraryIdOrderByDayNumberAsc(itinerary.getId())
                .stream()
                .map(day -> new ItineraryDayResponseDTO(
                day.getId(),
                day.getDayNumber(),
                day.getTitle(),
                day.getActivitiesDescription(),
                day.getItinerary().getId()
        ))
                .toList();
    }

    public ItineraryDayResponseDTO updateDay(
            Long tripId,
            Long dayId,
            String email,
            ItineraryDayRequestDTO request) {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(()
                        -> new ResourceNotFoundException("User not found"));

        Trip trip = tripRepository
                .findById(tripId)
                .orElseThrow(()
                        -> new ResourceNotFoundException("Trip not found"));

        // Make sure this trip belongs to the logged-in user
        if (!trip.getUser().getId().equals(user.getId())) {
            throw new ForbiddenException(
                    "You cannot modify this itinerary");
        }

        Itinerary itinerary = itineraryRepository
                .findByTripId(tripId)
                .orElseThrow(()
                        -> new ResourceNotFoundException("Itinerary not found"));

        ItineraryDay day = itineraryDayRepository
                .findById(dayId)
                .orElseThrow(()
                        -> new ResourceNotFoundException("Itinerary day not found"));

        // Make sure the day actually belongs to this itinerary
        if (!day.getItinerary().getId().equals(itinerary.getId())) {
            throw new ForbiddenException(
                    "This day does not belong to this itinerary");
        }

        day.setDayNumber(request.getDayNumber());
        day.setTitle(request.getTitle());
        day.setActivitiesDescription(
                request.getActivitiesDescription());

        ItineraryDay savedDay
                = itineraryDayRepository.save(day);

        return new ItineraryDayResponseDTO(
                savedDay.getId(),
                savedDay.getDayNumber(),
                savedDay.getTitle(),
                savedDay.getActivitiesDescription(),
                savedDay.getItinerary().getId()
        );
    }

}
