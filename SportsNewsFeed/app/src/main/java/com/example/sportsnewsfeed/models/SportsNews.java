package com.example.sportsnewsfeed.models;

import java.io.Serializable;

public class SportsNews implements Serializable {
    private int id;
    private String title;
    private String description;
    private String imageResId; // resource drawable name
    private SportCategory category;
    private String author;
    private String publishDate;

    public SportsNews(int id, String title, String description, String imageResId, 
                      SportCategory category, String author, String publishDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.imageResId = imageResId;
        this.category = category;
        this.author = author;
        this.publishDate = publishDate;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }
}

