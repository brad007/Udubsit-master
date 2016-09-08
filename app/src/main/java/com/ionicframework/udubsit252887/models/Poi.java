package com.ionicframework.udubsit252887.models;

/**
 * Created by CoLab on 2016/05/13.
 */
public class Poi {
    private String description;
    private String longitude;
    private String latitude;
    private String name;

    public Poi() {
    }

    public Poi(String description, String longitude, String latitude, String name) {

        this.description = description;
        this.longitude = longitude;
        this.latitude = latitude;
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getName() {
        return name;
    }
}
