package com.example.travelplanner.dto;

public class ActivityResponseDTO {

    private Long id;
    private String name;
    private String description;
    private String category;
    private Double estimatedCost;
    private Long destinationId;

    public ActivityResponseDTO(
            Long id,
            String name,
            String description,
            String category,
            Double estimatedCost,
            Long destinationId) {

        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.estimatedCost = estimatedCost;
        this.destinationId = destinationId;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public Double getEstimatedCost() {
        return estimatedCost;
    }

    public Long getDestinationId() {
        return destinationId;
    }
}