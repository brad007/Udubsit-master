package com.ionicframework.udubsit252887.models;

/**
 * Created by CoLab on 2016/05/10.
 */
public class
Event {
    private String owner;
    private String title;
    private String description;
    private String eventId;
    private String groupId;
    private String category;
    private String group;

    private double latitude;
    private double longitude;

    private long startDate;
    private long endDate;

    private String locationDescription;
    private long numOfPeople;
    private String eventImageUrl;
    private String eventThumbnailUrl;
    private String address;

    private int reportCounter;

    public Event() {
    }

    public Event(String owner, String title, String description, String eventId, String groupId, String category, String group, double latitude, double longitude, long startDate, long endDate, String locationDescription, long numOfPeople, String eventImageUrl, String eventThumbnailUrl, String address) {
        this.owner = owner;
        this.title = title;
        this.description = description;
        this.eventId = eventId;
        this.groupId = groupId;
        this.category = category;
        this.group = group;
        this.latitude = latitude;
        this.longitude = longitude;
        this.startDate = startDate;
        this.endDate = endDate;
        this.locationDescription = locationDescription;
        this.numOfPeople = numOfPeople;
        this.eventImageUrl = eventImageUrl;
        this.eventThumbnailUrl = eventThumbnailUrl;
        this.address = address;
        this.reportCounter = 0;
    }

    public int getReportCounter() {
        return reportCounter;
    }

    public void setReportCounter(int reportCounter) {
        this.reportCounter = reportCounter;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
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

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
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

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    public String getLocationDescription() {
        return locationDescription;
    }

    public void setLocationDescription(String locationDescription) {
        this.locationDescription = locationDescription;
    }

    public long getNumOfPeople() {
        return numOfPeople;
    }

    public void setNumOfPeople(long numOfPeople) {
        this.numOfPeople = numOfPeople;
    }

    public String getEventImageUrl() {
        return eventImageUrl;
    }

    public void setEventImageUrl(String eventImageUrl) {
        this.eventImageUrl = eventImageUrl;
    }

    public String getEventThumbnailUrl() {
        return eventThumbnailUrl;
    }

    public void setEventThumbnailUrl(String eventThumbnailUrl) {
        this.eventThumbnailUrl = eventThumbnailUrl;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
