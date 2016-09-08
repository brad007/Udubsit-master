package com.ionicframework.udubsit252887.models;

/**
 * Created by CoLab on 2016/05/10.
 */
public class Groups {
    private String groupId;
    private String name;
    private String description;
    private long numOfUsers;
    private String imageUrl;
    private String thumbnailUrl;

    public Groups() {
    }

    public Groups(String groupId, String name, String description, long numOfUsers, String imageUrl, String thumbnailUrl) {
        this.groupId = groupId;
        this.name = name;
        this.description = description;
        this.numOfUsers = numOfUsers;
        this.imageUrl = imageUrl;
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getGroupId() {

        return groupId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public long getNumOfUsers() {
        return numOfUsers;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }
}
