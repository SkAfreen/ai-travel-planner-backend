package com.example.travelplanner.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.travelplanner.entity.Itinerary;

public interface ItineraryRepository
        extends JpaRepository<Itinerary, Long> {

    Optional<Itinerary> findByTripId(Long tripId);
}