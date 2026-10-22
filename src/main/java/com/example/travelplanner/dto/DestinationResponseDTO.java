package com.example.travelplanner.dto;

public class DestinationResponseDTO {

    private Long id;
    private String name;
    private String country;
    private String description;

    public DestinationResponseDTO(
            Long id,
            String name,
            String country,
            String description) {

        this.id = id;
        this.name = name;
        this.country = country;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public String getDescription() {
        return description;
    }
}