package com.ionicframework.udubsit252887.models;

/**
 * Created by CoLab on 2016/05/21.
 */
public class Student {
    private String faculty;
    private String FName;
    private String SNAME;
    private String email;

    public Student() {
    }

    public Student(String faculty, String FName, String SNAME, String email) {

        this.faculty = faculty;
        this.FName = FName;
        this.SNAME = SNAME;
        this.email = email;
    }

    public String getFaculty() {
        return faculty;
    }

    public String getFName() {
        return FName;
    }

    public String getSNAME() {
        return SNAME;
    }

    public String getEmail() {
        return email;
    }
}
