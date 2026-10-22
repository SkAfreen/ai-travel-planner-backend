package com.example.travelplanner.service;

import org.springframework.stereotype.Service;

import com.example.travelplanner.dto.TravelPreferenceRequestDTO;
import com.example.travelplanner.dto.TravelPreferenceResponseDTO;
import com.example.travelplanner.entity.TravelPreference;
import com.example.travelplanner.entity.User;
import com.example.travelplanner.exception.ConflictException;
import com.example.travelplanner.exception.ResourceNotFoundException;
import com.example.travelplanner.repository.TravelPreferenceRepository;
import com.example.travelplanner.repository.UserRepository;

@Service
public class TravelPreferenceService {

    private final TravelPreferenceRepository travelPreferenceRepository;
    private final UserRepository userRepository;

    public TravelPreferenceService(
            TravelPreferenceRepository travelPreferenceRepository,
            UserRepository userRepository) {

        this.travelPreferenceRepository = travelPreferenceRepository;
        this.userRepository = userRepository;
    }

    public TravelPreferenceResponseDTO createPreference(
            String email,
            TravelPreferenceRequestDTO request) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(()
                        -> new ResourceNotFoundException("User not found"));

        if (travelPreferenceRepository
                .findByUserId(user.getId())
                .isPresent()) {

            throw new ConflictException(
                    "Travel preferences already exist for this user");
        }

        TravelPreference preference = new TravelPreference();

        preference.setTravelStyle(request.getTravelStyle());
        preference.setPreferredActivities(
                request.getPreferredActivities());
        preference.setBudgetPerDay(request.getBudgetPerDay());
        preference.setNumberOfTravelers(
                request.getNumberOfTravelers());
        preference.setUser(user);

        TravelPreference savedPreference
                = travelPreferenceRepository.save(preference);

        return new TravelPreferenceResponseDTO(
                savedPreference.getId(),
                savedPreference.getTravelStyle(),
                savedPreference.getPreferredActivities(),
                savedPreference.getBudgetPerDay(),
                savedPreference.getNumberOfTravelers()
        );

    }

    public TravelPreferenceResponseDTO getPreference(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(()
                        -> new ResourceNotFoundException("User not found"));

        TravelPreference preference
                = travelPreferenceRepository.findByUserId(user.getId())
                        .orElseThrow(()
                                -> new ResourceNotFoundException(
                                "Travel preferences not found"));

        return new TravelPreferenceResponseDTO(
                preference.getId(),
                preference.getTravelStyle(),
                preference.getPreferredActivities(),
                preference.getBudgetPerDay(),
                preference.getNumberOfTravelers()
        );
    }

    public TravelPreferenceResponseDTO updatePreference(
            String email,
            TravelPreferenceRequestDTO request) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(()
                        -> new ResourceNotFoundException("User not found"));

        TravelPreference preference
                = travelPreferenceRepository.findByUserId(user.getId())
                        .orElseThrow(()
                                -> new ResourceNotFoundException(
                                "Travel preferences not found"));

        preference.setTravelStyle(request.getTravelStyle());
        preference.setPreferredActivities(
                request.getPreferredActivities());
        preference.setBudgetPerDay(request.getBudgetPerDay());
        preference.setNumberOfTravelers(
                request.getNumberOfTravelers());

        TravelPreference updatedPreference
                = travelPreferenceRepository.save(preference);

        return new TravelPreferenceResponseDTO(
                updatedPreference.getId(),
                updatedPreference.getTravelStyle(),
                updatedPreference.getPreferredActivities(),
                updatedPreference.getBudgetPerDay(),
                updatedPreference.getNumberOfTravelers()
        );
    }

    public void deletePreference(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(()
                        -> new ResourceNotFoundException("User not found"));
        TravelPreference preference
                = travelPreferenceRepository.findByUserId(user.getId())
                        .orElseThrow(()
                                -> new ResourceNotFoundException(
                                "Travel preferences not found"));

        travelPreferenceRepository.delete(preference);
    }
}
