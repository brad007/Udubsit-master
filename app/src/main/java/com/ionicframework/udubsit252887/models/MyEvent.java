package com.ionicframework.udubsit252887.models;

/**
 * Created by CoLab on 2016/06/09.
 */
public class MyEvent {
    private String eventID;
    private String groupID;
    private long startTime;

    public MyEvent() {
    }

    public MyEvent(String eventID, String groupID, long startTime) {
        this.eventID = eventID;
        this.groupID = groupID;
        this.startTime = startTime;
    }

    public String getEventID() {
        return eventID;
    }

    public String getGroupID() {
        return groupID;
    }

    public long getStartTime() {
        return startTime;
    }
}
