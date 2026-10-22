package com.example.travelplanner.dto;

public class ItineraryDayResponseDTO {

    private Long id;
    private Integer dayNumber;
    private String title;
    private String activitiesDescription;
    private Long itineraryId;

    public ItineraryDayResponseDTO() {
    }

    public ItineraryDayResponseDTO(
            Long id,
            Integer dayNumber,
            String title,
            String activitiesDescription,
            Long itineraryId) {

        this.id = id;
        this.dayNumber = dayNumber;
        this.title = title;
        this.activitiesDescription = activitiesDescription;
        this.itineraryId = itineraryId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getDayNumber() {
        return dayNumber;
    }

    public void setDayNumber(Integer dayNumber) {
        this.dayNumber = dayNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getActivitiesDescription() {
        return activitiesDescription;
    }

    public void setActivitiesDescription(String activitiesDescription) {
        this.activitiesDescription = activitiesDescription;
    }

    public Long getItineraryId() {
        return itineraryId;
    }

    public void setItineraryId(Long itineraryId) {
        this.itineraryId = itineraryId;
    }
}
