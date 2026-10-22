package com.example.travelplanner.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "travel_preferences")
public class TravelPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String travelStyle;
    private String preferredActivities;
    private Double budgetPerDay;
    private Integer numberOfTravelers;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    public TravelPreference() {
    }

    public Long getId() {
        return id;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}