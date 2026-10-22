package com.example.travelplanner.dto;

import java.util.List;

public class AIGeneratedItineraryDTO {

    private String title;
    private List<AIGeneratedDayDTO> days;

    public AIGeneratedItineraryDTO() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<AIGeneratedDayDTO> getDays() {
        return days;
    }

    public void setDays(List<AIGeneratedDayDTO> days) {
        this.days = days;
    }
}