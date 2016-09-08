package com.ionicframework.udubsit252887.models;

/**
 * Created by CoLab on 2016/06/07.
 */
public class Person {
    private String email;
    private String tagline;

    public Person() {
    }

    public Person(String email, String tagline) {

        this.email = email;
        this.tagline = tagline;
    }

    public String getEmail() {

        return email;
    }

    public String getTagline() {
        return tagline;
    }
}
