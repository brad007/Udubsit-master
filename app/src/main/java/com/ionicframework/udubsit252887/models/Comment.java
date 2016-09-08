package com.ionicframework.udubsit252887.models;

/**
 * Created by CoLab on 2016/05/25.
 */
public class Comment {
    private String owner;
    private String text;
    private long timeCreated;

    public Comment() {
    }

    public Comment(String owner, String text, long timeCreated) {

        this.owner = owner;
        this.text = text;
        this.timeCreated = timeCreated;
    }

    public String getOwner() {
        return owner;
    }

    public String getText() {
        return text;
    }

    public long getTimeCreated() {
        return timeCreated;
    }
}
