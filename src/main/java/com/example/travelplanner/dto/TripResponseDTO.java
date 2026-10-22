package com.example.travelplanner.dto;

import java.time.LocalDate;

public class TripResponseDTO {

    private Long id;
    private String destination;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer numberOfTravelers;
    private Double budget;

    public TripResponseDTO(
            Long id,
            String destination,
            LocalDate startDate,
            LocalDate endDate,
            Integer numberOfTravelers,
            Double budget) {

        this.id = id;
        this.destination = destination;
        this.startDate = startDate;
        this.endDate = endDate;
        this.numberOfTravelers = numberOfTravelers;
        this.budget = budget;
    }

    public Long getId() {
        return id;
    }

    public String getDestination() {
        return destination;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public Integer getNumberOfTravelers() {
        return numberOfTravelers;
    }

    public Double getBudget() {
        return budget;
    }
}