package com.example.sportsnewsfeed.models;

public enum SportCategory {
    FOOTBALL("Football"),
    BASKETBALL("Basketball"),
    CRICKET("Cricket"),
    ALL("All");

    private final String displayName;

    SportCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

