package com.example.sportsnewsfeed.models;

import java.io.Serializable;

public class FeaturedMatch implements Serializable {
    private int id;
    private String team1;
    private String team2;
    private String matchTime;
    private String imageResId; // resource drawable name
    private SportCategory category;

    public FeaturedMatch(int id, String team1, String team2, String matchTime, 
                         String imageResId, SportCategory category) {
        this.id = id;
        this.team1 = team1;
        this.team2 = team2;
        this.matchTime = matchTime;
        this.imageResId = imageResId;
        this.category = category;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTeam1() {
        return team1;
    }

    public void setTeam1(String team1) {
        this.team1 = team1;
    }

    public String getTeam2() {
        return team2;
    }

    public void setTeam2(String team2) {
        this.team2 = team2;
    }

    public String getMatchTime() {
        return matchTime;
    }

    public void setMatchTime(String matchTime) {
        this.matchTime = matchTime;
    }

    public String getImageResId() {
        return imageResId;
    }

    public void setImageResId(String imageResId) {
        this.imageResId = imageResId;
    }

    public SportCategory getCategory() {
        return category;
    }

    public void setCategory(SportCategory category) {
        this.category = category;
    }
}

