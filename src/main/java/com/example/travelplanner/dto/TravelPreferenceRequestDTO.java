package com.example.travelplanner.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public class TravelPreferenceRequestDTO {

    @NotBlank(message = "Travel style is required")
    private String travelStyle;

    @NotBlank(message = "Preferred activities are required")
    private String preferredActivities;

    @NotNull(message = "Budget per day is required")
    @PositiveOrZero(message = "Budget per day cannot be negative")
    private Double budgetPerDay;

    @NotNull(message = "Number of travelers is required")
    @Positive(message = "Number of travelers must be greater than zero")
    private Integer numberOfTravelers;

    public TravelPreferenceRequestDTO() {
    }

    public String getTravelStyle() {
        return travelStyle;
    }

    public void setTravelStyle(String travelStyle) {
        this.travelStyle = travelStyle;
    }

    public String getPreferredActivities() {
        return preferredActivities;
    }

    public void setPreferredActivities(String preferredActivities) {
        this.preferredActivities = preferredActivities;
    }

    public Double getBudgetPerDay() {
        return budgetPerDay;
    }

    public void setBudgetPerDay(Double budgetPerDay) {
        this.budgetPerDay = budgetPerDay;
    }

    public Integer getNumberOfTravelers() {
        return numberOfTravelers;
    }

    public void setNumberOfTravelers(Integer numberOfTravelers) {
        this.numberOfTravelers = numberOfTravelers;
    }
}