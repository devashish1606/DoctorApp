package com.example.doctor;

public class person
{
    // Variable to store data corresponding
    // to firstname keyword in database
    private String name;

    // Variable to store data corresponding
    // to lastname keyword in database
    private String mobile;

    // Variable to store data corresponding
    // to age keyword in database
    private String app_data;

    private String descibeproblem;

    // Mandatory empty constructor
    // for use of FirebaseUI
    public person() {}

    // Getter and setter method


    public String getDescibeproblem() {
        return descibeproblem;
    }

    public void setDescibeproblem(String descibeproblem) {
        this.descibeproblem = descibeproblem;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getApp_data() {
        return app_data;
    }

    public void setApp_data(String app_data) {
        this.app_data = app_data;
    }
}