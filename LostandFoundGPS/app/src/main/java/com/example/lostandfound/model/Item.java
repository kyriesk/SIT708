package com.example.lostandfound.model;

import java.io.Serializable;

public class Item implements Serializable {
    private int id;
    private String title; // Item name
    private String description;
    private int categoryId;
    private String categoryName;
    private String status; // LOST or FOUND
    private String datePosted;
    private String imagePath;
    private String createdAt;
    private String phone;
    private String location;
    private double latitude;
    private double longitude;

    public Item() {
    }

    public Item(String title, String description, int categoryId, String categoryName, 
                String status, String datePosted, String imagePath, String phone, String location,
                double latitude, double longitude) {
        this.title = title;
        this.description = description;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.status = status;
        this.datePosted = datePosted;
        this.imagePath = imagePath;
        this.phone = phone;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
    }

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

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDatePosted() {
        return datePosted;
    }

    public void setDatePosted(String datePosted) {
        this.datePosted = datePosted;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
