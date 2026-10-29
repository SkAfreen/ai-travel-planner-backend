package com.example.travelplanner.service;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.travelplanner.dto.AIGeneratedItineraryDTO;
import com.example.travelplanner.entity.Activity;
import com.example.travelplanner.entity.Destination;
import com.example.travelplanner.entity.TravelPreference;
import com.example.travelplanner.entity.Trip;
import com.example.travelplanner.entity.User;
import com.example.travelplanner.exception.ForbiddenException;
import com.example.travelplanner.exception.ResourceNotFoundException;
import com.example.travelplanner.repository.ActivityRepository;
import com.example.travelplanner.repository.DestinationRepository;
import com.example.travelplanner.repository.ItineraryDayRepository;
import com.example.travelplanner.repository.ItineraryRepository;
import com.example.travelplanner.repository.TravelPreferenceRepository;
import com.example.travelplanner.repository.TripRepository;
import com.example.travelplanner.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openai.client.OpenAIClient;
import com.openai.models.responses.Response;
import com.openai.models.responses.ResponseCreateParams;
import com.example.travelplanner.exception.AIServiceException;

@Service
public class AIItineraryService {

    private final OpenAIClient openAIClient;
    private final TripRepository tripRepository;
    private final UserRepository userRepository;
    private final DestinationRepository destinationRepository;
    private final ActivityRepository activityRepository;
    private final TravelPreferenceRepository travelPreferenceRepository;
    private final ObjectMapper objectMapper;
    private final ItineraryRepository itineraryRepository;
    private final ItineraryDayRepository itineraryDayRepository;
    private final AIItineraryValidator aiItineraryValidator;
    private final ItineraryPersistenceService itineraryPersistenceService;

    public AIItineraryService(
            OpenAIClient openAIClient,
            TripRepository tripRepository,
            UserRepository userRepository,
            DestinationRepository destinationRepository,
            ActivityRepository activityRepository,
            TravelPreferenceRepository travelPreferenceRepository,
            ItineraryRepository itineraryRepository,
            ItineraryDayRepository itineraryDayRepository,
            ObjectMapper objectMapper,
            AIItineraryValidator aiItineraryValidator,
            ItineraryPersistenceService itineraryPersistenceService) {

        this.openAIClient = openAIClient;
        this.tripRepository = tripRepository;
        this.userRepository = userRepository;
        this.destinationRepository = destinationRepository;
        this.activityRepository = activityRepository;
        this.travelPreferenceRepository = travelPreferenceRepository;
        this.objectMapper = objectMapper;
        this.itineraryRepository = itineraryRepository;
        this.itineraryDayRepository = itineraryDayRepository;
        this.aiItineraryValidator = aiItineraryValidator;
        this.itineraryPersistenceService = itineraryPersistenceService;
    }

    public AIGeneratedItineraryDTO generateItinerary(Long tripId, String email) {

        // 1. Find authenticated user
        User user = userRepository.findByEmail(email)
                .orElseThrow(()
                        -> new ResourceNotFoundException("User not found"));

        // 2. Find trip
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(()
                        -> new ResourceNotFoundException("Trip not found"));

        // 3. Verify trip ownership
        if (!trip.getUser().getId().equals(user.getId())) {
            throw new ForbiddenException(
                    "You cannot generate an itinerary for this trip");
        }

        long numberOfDays = ChronoUnit.DAYS.between(
                trip.getStartDate(),
                trip.getEndDate()
        ) + 1;

// Prevent excessively long AI itinerary requests
        if (numberOfDays < 1 || numberOfDays > 30) {
            throw new IllegalArgumentException(
                    "AI itinerary generation supports trips between 1 and 30 days"
            );
        }

// 4. Find user's travel preferences
        TravelPreference preference = travelPreferenceRepository
                .findByUserId(user.getId())
                .orElse(null);

        String preferenceDetails;

        if (preference != null) {

            preferenceDetails = """
                    Travel style: %s
                    Preferred activities: %s
                    Preferred budget per day: %s
                    Preferred number of travelers: %s
                    """.formatted(
                    preference.getTravelStyle(),
                    preference.getPreferredActivities(),
                    preference.getBudgetPerDay(),
                    preference.getNumberOfTravelers()
            );

        } else {

            preferenceDetails
                    = "No additional travel preferences provided.";
        }

        // 5. Get destination directly from the trip
        String destinationName = trip.getDestination();

        if (destinationName == null || destinationName.isBlank()) {
            throw new IllegalArgumentException(
                    "Trip destination cannot be empty"
            );
        }

        // 6. Find optional destination details from the catalogue
        List<Activity> activities = destinationRepository
                .findByName(destinationName)
                .map(destination
                        -> activityRepository.findByDestinationId(
                                destination.getId()
                        )
                )
                .orElseGet(List::of);

        // 7. Convert available activities into readable text
        String availableActivities = activities.stream()
                .map(activity
                        -> "Activity: " + activity.getName()
                        + "\nCategory: " + activity.getCategory()
                        + "\nDescription: " + activity.getDescription()
                        + "\nEstimated Cost: " + activity.getEstimatedCost()
                )
                .collect(Collectors.joining("\n\n"));

        // 8. Build AI prompt
        String prompt = """
                You are an expert travel planner.

                Create a detailed day-by-day travel itinerary using
                the following trip information.

                Destination: %s
                Start date: %s
                End date: %s
                Number of travelers: %s
                Total budget: %s
                Number of itinerary days: %s

                TRAVELER PREFERENCES:

                %s

                AVAILABLE ACTIVITIES:

                %s

                IMPORTANT RULES:

                - Personalize the itinerary according to the
                  traveler's preferences when preferences are provided.
                - Respect the traveler's preferred travel style.
                - Prioritize activities that match the traveler's
                  preferred activities.
                - Consider both the total trip budget and preferred
                  daily budget.
                - The activities listed above come from the travel
                  planner's available activity catalogue.
                - Use the exact activity names provided above.
                - Include all available activities somewhere in
                  the itinerary.
                - Organize the activities naturally across the trip.
                - Do not call them database activities, objects,
                  placeholders, or priority activities.
                - You may suggest additional activities when useful.
                - Create a plan for every day of the trip.
                - Include morning, afternoon and evening plans.
                - Keep the itinerary realistic.
                - Keep the itinerary within the total budget.
                - Include useful travel suggestions.
                - Keep the response structured and easy to read.
                  - Return ONLY valid JSON.
                - Do not use Markdown.
                - Do not use ```json code blocks.
                - Do not include any text before or after the JSON.
                - You MUST generate exactly %s days, numbered consecutively from 1 to %s.
                - Day 1 corresponds to the start date and the final day corresponds to the end date.
                - Use exactly this structure:
                {
                "title": "Itinerary title",
                "days": [
                 {
                "dayNumber": 1,
                "title": "Day title",
                "activitiesDescription": "Detailed morning, afternoon and evening plan"
                }
                ]
                }
                """.formatted(
                trip.getDestination(),
                trip.getStartDate(),
                trip.getEndDate(),
                trip.getNumberOfTravelers(),
                trip.getBudget(),
                numberOfDays,
                preferenceDetails,
                availableActivities,
                numberOfDays,
                numberOfDays
        );

        // 9. Prepare OpenAI request
        ResponseCreateParams params
                = ResponseCreateParams.builder()
                        .model("gpt-5.4-mini")
                        .input(prompt)
                        .build();

// 10. Call OpenAI
        Response response;

        try {

            response = openAIClient.responses().create(params);

        } catch (com.openai.errors.OpenAIException e) {

            throw new AIServiceException(
                    "OpenAI API request failed", e
            );
        }

// 11. Extract generated text
        String jsonResponse;

        try {

            jsonResponse = response.output().stream()
                    .flatMap(item -> item.message().stream())
                    .flatMap(message -> message.content().stream())
                    .flatMap(content -> content.outputText().stream())
                    .map(outputText -> outputText.text())
                    .collect(Collectors.joining("\n"));

        } catch (RuntimeException e) {

            throw new AIServiceException(
                    "Failed to extract OpenAI response", e
            );
        }

// 12. Parse AI response
        AIGeneratedItineraryDTO generated;

        try {

            generated = objectMapper.readValue(
                    jsonResponse,
                    AIGeneratedItineraryDTO.class
            );

        } catch (JsonProcessingException e) {

            throw new AIServiceException(
                    "Failed to parse AI itinerary response", e
            );
        }

// 13. Validate AI-generated itinerary
        try {

            aiItineraryValidator.validate(
                    generated,
                    numberOfDays
            );

        } catch (IllegalArgumentException e) {

            throw new AIServiceException(
                    "AI generated an invalid itinerary", e
            );
        }

        // Save only after the AI response has passed validation.
        itineraryPersistenceService.saveGeneratedItinerary(trip, generated);

        return generated;
    }

}
