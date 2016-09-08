package com.ionicframework.udubsit252887.models;

/**
 * Created by CoLab on 2016/05/21.
 */
public class Users {
    private String user;
    private String email;
    private String photoUri;
    private String UID;

    public Users() {
    }

    public Users(String user) {
        this.user = user;
    }

    public Users(String user, String email, String photoUri, String UID) {
        this.user = user;
        this.email = email;
        this.photoUri = photoUri;
        this.UID = UID;
    }

    public String getUID() {

        return UID;
    }

    public String getUser() {
        return user;
    }

    public String getEmail() {
        return email;
    }

    public String getPhotoUri() {
        return photoUri;
    }
}
