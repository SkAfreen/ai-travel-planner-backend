package com.example.travelplanner.service;

import com.example.travelplanner.dto.AIGeneratedDayDTO;
import com.example.travelplanner.dto.AIGeneratedItineraryDTO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AIItineraryValidatorTest {

    private AIItineraryValidator validator;

    @BeforeEach
    void setUp() {
        validator = new AIItineraryValidator();
    }

    // Helper: Create a valid itinerary
    private AIGeneratedItineraryDTO createValidItinerary(int numberOfDays) {

        AIGeneratedItineraryDTO itinerary =
                new AIGeneratedItineraryDTO();

        itinerary.setTitle("Dubai Travel Adventure");

        List<AIGeneratedDayDTO> days = new ArrayList<>();

        for (int i = 1; i <= numberOfDays; i++) {

            AIGeneratedDayDTO day = new AIGeneratedDayDTO();

            day.setDayNumber(i);
            day.setTitle("Day " + i);
            day.setActivitiesDescription(
                    "Morning sightseeing, afternoon activities, evening dinner."
            );

            days.add(day);
        }

        itinerary.setDays(days);

        return itinerary;
    }

    @Test
    void validSixDayItineraryShouldPass() {

        AIGeneratedItineraryDTO itinerary =
                createValidItinerary(6);

        assertDoesNotThrow(() ->
                validator.validate(itinerary, 6)
        );
    }

    @Test
    void incompleteItineraryShouldFail() {

        AIGeneratedItineraryDTO itinerary =
                createValidItinerary(4);

        assertThrows(
                IllegalArgumentException.class,
                () -> validator.validate(itinerary, 6)
        );
    }

    @Test
    void duplicateDayNumbersShouldFail() {

        AIGeneratedItineraryDTO itinerary =
                createValidItinerary(6);

        itinerary.getDays().get(1).setDayNumber(1);

        assertThrows(
                IllegalArgumentException.class,
                () -> validator.validate(itinerary, 6)
        );
    }

    @Test
    void missingDayTitleShouldFail() {

        AIGeneratedItineraryDTO itinerary =
                createValidItinerary(6);

        itinerary.getDays().get(2).setTitle("");

        assertThrows(
                IllegalArgumentException.class,
                () -> validator.validate(itinerary, 6)
        );
    }

    @Test
    void emptyActivitiesShouldFail() {

        AIGeneratedItineraryDTO itinerary =
                createValidItinerary(6);

        itinerary.getDays().get(3).setActivitiesDescription("");

        assertThrows(
                IllegalArgumentException.class,
                () -> validator.validate(itinerary, 6)
        );
    }

    @Test
    void missingItineraryTitleShouldFail() {

        AIGeneratedItineraryDTO itinerary =
                createValidItinerary(6);

        itinerary.setTitle("");

        assertThrows(
                IllegalArgumentException.class,
                () -> validator.validate(itinerary, 6)
        );
    }
}