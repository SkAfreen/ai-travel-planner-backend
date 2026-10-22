package com.example.travelplanner.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.travelplanner.dto.AIGeneratedDayDTO;
import com.example.travelplanner.dto.AIGeneratedItineraryDTO;
import com.example.travelplanner.entity.Itinerary;
import com.example.travelplanner.entity.ItineraryDay;
import com.example.travelplanner.entity.Trip;
import com.example.travelplanner.repository.ItineraryDayRepository;
import com.example.travelplanner.repository.ItineraryRepository;

@Service
public class ItineraryPersistenceService {

    private final ItineraryRepository itineraryRepository;
    private final ItineraryDayRepository itineraryDayRepository;

    public ItineraryPersistenceService(
            ItineraryRepository itineraryRepository,
            ItineraryDayRepository itineraryDayRepository) {

        this.itineraryRepository = itineraryRepository;
        this.itineraryDayRepository = itineraryDayRepository;
    }

    @Transactional
    public void saveGeneratedItinerary(
            Trip trip,
            AIGeneratedItineraryDTO generated) {

        // Find existing itinerary or create a new one
        Itinerary itinerary = itineraryRepository
                .findByTripId(trip.getId())
                .orElseGet(Itinerary::new);

        itinerary.setTrip(trip);
        itinerary.setTitle(generated.getTitle());

        Itinerary savedItinerary =
                itineraryRepository.save(itinerary);

        // Remove previously generated days
        itineraryDayRepository.deleteByItineraryId(
                savedItinerary.getId()
        );

        // Ensure old days are deleted before inserting new ones
        itineraryDayRepository.flush();

        // Save newly generated days
        for (AIGeneratedDayDTO generatedDay : generated.getDays()) {

            ItineraryDay day = new ItineraryDay();

            day.setDayNumber(generatedDay.getDayNumber());
            day.setTitle(generatedDay.getTitle());
            day.setActivitiesDescription(
                    generatedDay.getActivitiesDescription()
            );

            day.setItinerary(savedItinerary);

            itineraryDayRepository.save(day);
        }

        // Detect database constraint errors before returning
        itineraryDayRepository.flush();
    }
}