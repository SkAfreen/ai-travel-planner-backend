package com.example.travelplanner.service;

import com.example.travelplanner.dto.AIGeneratedDayDTO;
import com.example.travelplanner.dto.AIGeneratedItineraryDTO;
import com.example.travelplanner.entity.Itinerary;
import com.example.travelplanner.entity.ItineraryDay;
import com.example.travelplanner.entity.Trip;
import com.example.travelplanner.entity.User;
import com.example.travelplanner.repository.ItineraryDayRepository;
import com.example.travelplanner.repository.ItineraryRepository;
import com.example.travelplanner.repository.TripRepository;
import com.example.travelplanner.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import javax.sql.DataSource;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
class ItineraryPersistenceServiceTest {

    @Autowired
    private ItineraryPersistenceService persistenceService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private ItineraryRepository itineraryRepository;

    @Autowired
    private ItineraryDayRepository itineraryDayRepository;

    @Autowired
    private DataSource dataSource;

    private void verifyTestDatabase() throws Exception {

        try (Connection connection = dataSource.getConnection()) {

            String databaseName = connection.getCatalog();

            assertEquals(
                    "travelplanner_test",
                    databaseName,
                    "TEST ABORTED: Wrong database!"
            );
        }
    }

    @org.junit.jupiter.api.Test
    void shouldConnectToTestDatabase() throws Exception {
        verifyTestDatabase();
        System.out.println("SUCCESS: Connected to travelplanner_test");
    }

    private AIGeneratedItineraryDTO createItinerary(
            String title,
            int numberOfDays) {

        AIGeneratedItineraryDTO itinerary
                = new AIGeneratedItineraryDTO();

        itinerary.setTitle(title);

        List<AIGeneratedDayDTO> days = new ArrayList<>();

        for (int i = 1; i <= numberOfDays; i++) {

            AIGeneratedDayDTO day = new AIGeneratedDayDTO();

            day.setDayNumber(i);
            day.setTitle("Day " + i);
            day.setActivitiesDescription(
                    "Activities for day " + i
            );

            days.add(day);
        }

        itinerary.setDays(days);

        return itinerary;
    }

    @Test
    void failedRegenerationShouldPreserveOriginalItinerary()
            throws Exception {

        // SAFETY: Verify database before inserting data
        verifyTestDatabase();

        // 1. Create temporary user
        User user = new User();

        user.setName("Rollback Test User");
        user.setEmail("rollback-test@example.com");
        user.setPassword("test-password");
        user.setCountry("India");
        user.setPreferredCurrency("INR");

        User savedUser = userRepository.save(user);

        // 2. Create temporary trip
        Trip trip = new Trip();

        trip.setDestination("Dubai");
        trip.setStartDate(LocalDate.of(2026, 12, 20));
        trip.setEndDate(LocalDate.of(2026, 12, 25));
        trip.setNumberOfTravelers(2);
        trip.setBudget(60000.0);
        trip.setUser(savedUser);

        Trip savedTrip = tripRepository.save(trip);

        // 3. Save original itinerary
        AIGeneratedItineraryDTO original
                = createItinerary("Original Dubai Itinerary", 6);

        persistenceService.saveGeneratedItinerary(
                savedTrip,
                original
        );

        // 4. Verify original data
        Itinerary savedItinerary = itineraryRepository
                .findByTripId(savedTrip.getId())
                .orElseThrow();

        List<ItineraryDay> originalDays
                = itineraryDayRepository
                        .findByItineraryIdOrderByDayNumberAsc(
                                savedItinerary.getId()
                        );

        assertEquals(6, originalDays.size());
        assertEquals(
                "Original Dubai Itinerary",
                savedItinerary.getTitle()
        );

        // 5. Create replacement itinerary
        AIGeneratedItineraryDTO replacement
                = createItinerary("New Dubai Itinerary", 6);

        // Deliberately exceed PostgreSQL's default VARCHAR(255)
        // limit to trigger a database error.
        replacement.getDays().get(3).setTitle(
                "X".repeat(500)
        );

        // 6. Attempt replacement - expect database failure
        assertThrows(
                RuntimeException.class,
                () -> persistenceService.saveGeneratedItinerary(
                        savedTrip,
                        replacement
                )
        );

        // 7. Reload itinerary after failed transaction
        Itinerary afterFailure = itineraryRepository
                .findByTripId(savedTrip.getId())
                .orElseThrow();

        List<ItineraryDay> daysAfterFailure
                = itineraryDayRepository
                        .findByItineraryIdOrderByDayNumberAsc(
                                afterFailure.getId()
                        );

        // 8. Verify rollback
        assertEquals(
                "Original Dubai Itinerary",
                afterFailure.getTitle()
        );

        assertEquals(6, daysAfterFailure.size());

        assertEquals(
                "Day 4",
                daysAfterFailure.get(3).getTitle()
        );
    }
}
