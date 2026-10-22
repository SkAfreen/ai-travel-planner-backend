package com.example.travelplanner.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.travelplanner.entity.TravelPreference;

public interface TravelPreferenceRepository
        extends JpaRepository<TravelPreference, Long> {

    Optional<TravelPreference> findByUserId(Long userId);
}