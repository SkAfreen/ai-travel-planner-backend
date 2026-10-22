package com.example.travelplanner.dto;

import jakarta.validation.constraints.NotBlank;

public class ItineraryRequestDTO {

    @NotBlank(message = "Itinerary title is required")
    private String title;

    @NotBlank(message = "Itinerary description is required")
    private String description;

    public ItineraryRequestDTO() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}