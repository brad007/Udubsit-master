package com.ionicframework.udubsit252887.models;

/**
 * Created by CoLab on 2016/06/02.
 */
public class Shoutout {
    private String title;
    private String description;
    private int time;
    private String group;
    private String cat;

    public Shoutout() {
    }

    public Shoutout(String title, String description, int time, String group, String cat) {
        this.title = title;
        this.description = description;
        this.time = time;
        this.group = group;
        this.cat = cat;
    }

    public String getTitle() {

        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getTime() {
        return time;
    }

    public String getGroup() {
        return group;
    }

    public String getCat() {
        return cat;
    }
}
