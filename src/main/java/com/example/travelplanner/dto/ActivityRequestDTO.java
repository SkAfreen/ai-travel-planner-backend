package com.example.travelplanner.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public class ActivityRequestDTO {

    @NotBlank(message = "Activity name is required")
    private String name;

    @NotBlank(message = "Activity description is required")
    private String description;

    @NotBlank(message = "Activity category is required")
    private String category;

    @NotNull(message = "Estimated cost is required")
    @PositiveOrZero(message = "Estimated cost cannot be negative")
    private Double estimatedCost;

    public ActivityRequestDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getEstimatedCost() {
        return estimatedCost;
    }

    public void setEstimatedCost(Double estimatedCost) {
        this.estimatedCost = estimatedCost;
    }
}