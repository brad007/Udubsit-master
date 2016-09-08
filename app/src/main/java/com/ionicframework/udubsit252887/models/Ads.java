package com.ionicframework.udubsit252887.models;

/**
 * Created by CoLab on 2016/05/23.
 */
public class Ads {
    private String advertImageUrl;
    private String advertDescription;
    private double advertCost;
    private long advertDue;
    private String advertOwner;
    private String advertCategory;
    private double longitude;
    private double latitude;
    private String advertId;
    private String advertGroupId;

    public Ads() {
    }

    public Ads(String advertImageUrl, String advertDescription, double advertCost, long advertDue, String advertOwner, String advertCategory, double longitude, double latitude, String advertId, String advertGroupId) {

        this.advertImageUrl = advertImageUrl;
        this.advertDescription = advertDescription;
        this.advertCost = advertCost;
        this.advertDue = advertDue;
        this.advertOwner = advertOwner;
        this.advertCategory = advertCategory;
        this.longitude = longitude;
        this.latitude = latitude;
        this.advertId = advertId;
        this.advertGroupId = advertGroupId;
    }

    public String getAdvertImageUrl() {
        return advertImageUrl;
    }

    public void setAdvertImageUrl(String advertImageUrl) {
        this.advertImageUrl = advertImageUrl;
    }

    public String getAdvertDescription() {
        return advertDescription;
    }

    public void setAdvertDescription(String advertDescription) {
        this.advertDescription = advertDescription;
    }

    public double getAdvertCost() {
        return advertCost;
    }

    public void setAdvertCost(double advertCost) {
        this.advertCost = advertCost;
    }

    public long getAdvertDue() {
        return advertDue;
    }

    public void setAdvertDue(long advertDue) {
        this.advertDue = advertDue;
    }

    public String getAdvertOwner() {
        return advertOwner;
    }

    public void setAdvertOwner(String advertOwner) {
        this.advertOwner = advertOwner;
    }

    public String getAdvertCategory() {
        return advertCategory;
    }

    public void setAdvertCategory(String advertCategory) {
        this.advertCategory = advertCategory;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getAdvertId() {
        return advertId;
    }

    public void setAdvertId(String advertId) {
        this.advertId = advertId;
    }

    public String getAdvertGroupId() {
        return advertGroupId;
    }

    public void setAdvertGroupId(String advertGroupId) {
        this.advertGroupId = advertGroupId;
    }
}
