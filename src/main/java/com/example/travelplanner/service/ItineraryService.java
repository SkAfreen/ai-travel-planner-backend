package com.example.travelplanner.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.travelplanner.dto.ItineraryDayResponseDTO;
import com.example.travelplanner.dto.ItineraryRequestDTO;
import com.example.travelplanner.dto.ItineraryResponseDTO;
import com.example.travelplanner.entity.Itinerary;
import com.example.travelplanner.entity.ItineraryDay;
import com.example.travelplanner.entity.Trip;
import com.example.travelplanner.entity.User;
import com.example.travelplanner.exception.ConflictException;
import com.example.travelplanner.exception.ForbiddenException;
import com.example.travelplanner.exception.ResourceNotFoundException;
import com.example.travelplanner.repository.ItineraryDayRepository;
import com.example.travelplanner.repository.ItineraryRepository;
import com.example.travelplanner.repository.TripRepository;
import com.example.travelplanner.repository.UserRepository;

@Service
public class ItineraryService {

    private final ItineraryRepository itineraryRepository;
    private final TripRepository tripRepository;
    private final UserRepository userRepository;
    private final ItineraryDayRepository itineraryDayRepository;

    public ItineraryService(
            ItineraryRepository itineraryRepository,
            TripRepository tripRepository,
            UserRepository userRepository,
            ItineraryDayRepository itineraryDayRepository) {

        this.itineraryRepository = itineraryRepository;
        this.tripRepository = tripRepository;
        this.userRepository = userRepository;
        this.itineraryDayRepository = itineraryDayRepository;
    }

    public ItineraryResponseDTO createItinerary(
        Long tripId,
        String email,
        ItineraryRequestDTO request) {

    User user = userRepository.findByEmail(email)
            .orElseThrow(() ->
                    new ResourceNotFoundException("User not found"));

    Trip trip = tripRepository.findById(tripId)
            .orElseThrow(() ->
                    new ResourceNotFoundException("Trip not found"));

    if (!trip.getUser().getId().equals(user.getId())) {
        throw new ForbiddenException(
                "You cannot create an itinerary for this trip");
    }

    if (itineraryRepository.findByTripId(tripId).isPresent()) {
        throw new ConflictException(
                "Itinerary already exists for this trip");
    }

    Itinerary itinerary = new Itinerary();

    itinerary.setTitle(request.getTitle());
    itinerary.setDescription(request.getDescription());
    itinerary.setTrip(trip);

    Itinerary savedItinerary =
            itineraryRepository.save(itinerary);

    return new ItineraryResponseDTO(
            savedItinerary.getId(),
            savedItinerary.getTitle(),
            savedItinerary.getDescription(),
            savedItinerary.getTrip().getId()
    );
}
    
    public ItineraryResponseDTO getItinerary(
            Long tripId,
            String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(()
                        -> new ResourceNotFoundException("User not found"));

        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(()
                        -> new ResourceNotFoundException("Trip not found"));

        if (!trip.getUser().getId().equals(user.getId())) {
            throw new ForbiddenException(
                    "You cannot access this itinerary");
        }

        Itinerary itinerary = itineraryRepository.findByTripId(tripId)
                .orElseThrow(()
                        -> new ResourceNotFoundException("Itinerary not found"));

        List<ItineraryDay> itineraryDays
                = itineraryDayRepository.findByItineraryIdOrderByDayNumberAsc(itinerary.getId());

        List<ItineraryDayResponseDTO> dayDTOs = itineraryDays.stream()
                .map(day -> new ItineraryDayResponseDTO(
                day.getId(),
                day.getDayNumber(),
                day.getTitle(),
                day.getActivitiesDescription(),
                itinerary.getId()
        ))
                .toList();

        ItineraryResponseDTO response = new ItineraryResponseDTO(
                itinerary.getId(),
                itinerary.getTitle(),
                itinerary.getDescription(),
                itinerary.getTrip().getId()
        );

        response.setDays(dayDTOs);

        return response;

    }

}
