package com.example.travelplanner.dto;

import java.util.List;

public class ItineraryResponseDTO {

    private Long id;
    private String title;
    private String description;
    private Long tripId;
    private List<ItineraryDayResponseDTO> days;

    public ItineraryResponseDTO(
            Long id,
            String title,
            String description,
            Long tripId) {

        this.id = id;
        this.title = title;
        this.description = description;
        this.tripId = tripId;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Long getTripId() {
        return tripId;
    }

    public List<ItineraryDayResponseDTO> getDays() {
        return days;
    }

    public void setDays(List<ItineraryDayResponseDTO> days) {
        this.days = days;
    }
}
