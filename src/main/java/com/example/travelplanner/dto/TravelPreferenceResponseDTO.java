package com.example.travelplanner.dto;

public class TravelPreferenceResponseDTO {

    private Long id;
    private String travelStyle;
    private String preferredActivities;
    private Double budgetPerDay;
    private Integer numberOfTravelers;

    public TravelPreferenceResponseDTO(
            Long id,
            String travelStyle,
            String preferredActivities,
            Double budgetPerDay,
            Integer numberOfTravelers) {

        this.id = id;
        this.travelStyle = travelStyle;
        this.preferredActivities = preferredActivities;
        this.budgetPerDay = budgetPerDay;
        this.numberOfTravelers = numberOfTravelers;
    }

    public Long getId() {
        return id;
    }

    public String getTravelStyle() {
        return travelStyle;
    }

    public String getPreferredActivities() {
        return preferredActivities;
    }

    public Double getBudgetPerDay() {
        return budgetPerDay;
    }

    public Integer getNumberOfTravelers() {
        return numberOfTravelers;
    }
}