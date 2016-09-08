package com.ionicframework.udubsit252887.models;

/**
 * Created by CoLab on 2016/06/02.
 */
public class Schedual {
    private String contact;
    private String enddate;
    private String from;
    private String location;
    private String room;
    private String startdate;
    private String till;
    private String title;

    public Schedual(String contact, String enddate, String from, String location, String room, String startdate, String till, String title) {

        this.contact = contact;
        this.enddate = enddate;
        this.from = from;
        this.location = location;
        this.room = room;
        this.startdate = startdate;
        this.till = till;
        this.title = title;
    }

    public Schedual() {
    }

    public String getContact() {
        return contact;
    }

    public String getEnddate() {
        return enddate;
    }

    public String getFrom() {
        return from;
    }

    public String getLocation() {
        return location;
    }

    public String getRoom() {
        return room;
    }

    public String getStartdate() {
        return startdate;
    }

    public String getTill() {
        return till;
    }

    public String getTitle() {
        return title;
    }
}
