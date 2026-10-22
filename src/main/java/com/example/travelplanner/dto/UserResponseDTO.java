package com.example.travelplanner.dto;

public class UserResponseDTO {

    private Long id;
    private String name;
    private String email;
    private String country;
    private String preferredCurrency;

    public UserResponseDTO() {
    }

    public UserResponseDTO(Long id, String name, String email,
                           String country, String preferredCurrency) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.country = country;
        this.preferredCurrency = preferredCurrency;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getCountry() {
        return country;
    }

    public String getPreferredCurrency() {
        return preferredCurrency;
    }
}