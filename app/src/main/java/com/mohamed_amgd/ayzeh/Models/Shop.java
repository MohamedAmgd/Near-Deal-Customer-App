package com.mohamed_amgd.ayzeh.Models;

import java.io.Serializable;

public class Shop implements Serializable {
    private String id;
    private String name;
    private String imageUrl;
    private String description;
    private double locationLon;
    private double locationLat;
    private String distanceToUser;

    public Shop() {
    }

    public Shop(String id, String name, String imageUrl, String description, double locationLon, double locationLat, String distanceToUser) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.description = description;
        this.locationLon = locationLon;
        this.locationLat = locationLat;
        this.distanceToUser = distanceToUser;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getLocationLon() {
        return locationLon;
    }

    public void setLocationLon(double locationLon) {
        this.locationLon = locationLon;
    }

    public double getLocationLat() {
        return locationLat;
    }

    public void setLocationLat(double locationLat) {
        this.locationLat = locationLat;
    }

    public String getDistanceToUser() {
        return distanceToUser;
    }

    public void setDistanceToUser(String distanceToUser) {
        this.distanceToUser = distanceToUser;
    }
}
