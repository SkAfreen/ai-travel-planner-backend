package com.example.travelplanner.service;

import com.example.travelplanner.dto.AIGeneratedDayDTO;
import com.example.travelplanner.dto.AIGeneratedItineraryDTO;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;

@Component
public class AIItineraryValidator {

    public void validate(
            AIGeneratedItineraryDTO generated,
            long expectedDays) {

        if (generated == null) {
            throw new IllegalArgumentException(
                    "AI generated an empty itinerary");
        }

        if (generated.getTitle() == null
                || generated.getTitle().isBlank()) {

            throw new IllegalArgumentException(
                    "AI itinerary title cannot be empty");
        }

        if (expectedDays < 1 || expectedDays > Integer.MAX_VALUE) {
            throw new IllegalArgumentException(
                    "Invalid expected itinerary duration");
        }

        if (generated.getDays() == null
                || generated.getDays().size() != expectedDays) {

            throw new IllegalArgumentException(
                    "AI itinerary must contain exactly "
                    + expectedDays + " days");
        }

        Set<Integer> dayNumbers = new HashSet<>();

        for (AIGeneratedDayDTO day : generated.getDays()) {

            if (day == null) {
                throw new IllegalArgumentException(
                        "AI itinerary contains an empty day");
            }

            if (day.getDayNumber() == null
                    || day.getDayNumber() < 1
                    || day.getDayNumber() > expectedDays) {

                throw new IllegalArgumentException(
                        "AI generated an invalid day number");
            }

            if (!dayNumbers.add(day.getDayNumber())) {
                throw new IllegalArgumentException(
                        "AI generated duplicate day numbers");
            }

            if (day.getTitle() == null
                    || day.getTitle().isBlank()) {

                throw new IllegalArgumentException(
                        "AI generated a day without a title");
            }

            if (day.getActivitiesDescription() == null
                    || day.getActivitiesDescription().isBlank()) {

                throw new IllegalArgumentException(
                        "AI generated a day without activities");
            }
        }

        for (int i = 1; i <= expectedDays; i++) {
            if (!dayNumbers.contains(i)) {
                throw new IllegalArgumentException(
                        "AI itinerary is missing day " + i);
            }
        }
    }
}