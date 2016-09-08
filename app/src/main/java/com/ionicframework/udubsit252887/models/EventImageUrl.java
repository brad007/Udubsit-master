package com.ionicframework.udubsit252887.models;

/**
 * Created by CoLab on 2016/05/25.
 */
public class EventImageUrl {
    private String original;
    private String thumbnail;
    private String imageID;
    private String eventID;

    public EventImageUrl() {
    }

    public EventImageUrl(String original, String thumbnail, String imageID, String eventID) {
        this.original = original;
        this.thumbnail = thumbnail;
        this.imageID = imageID;
        this.eventID = eventID;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getImageID() {
        return imageID;
    }

    public void setImageID(String imageID) {
        this.imageID = imageID;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

}
