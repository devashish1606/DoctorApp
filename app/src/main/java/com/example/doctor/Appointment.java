package com.example.doctor;

public class Appointment {
    String name, mobile, age, address, app_data,descibeproblem,uid,gender;

    public Appointment() {
    }

    public Appointment(String uid,String name, String mobile, String age, String address,String app_data, String descibeproblem,String gender) {
        this.uid =uid;
        this.name = name;
        this.mobile = mobile;
        this.age = age;
        this.address = address;
        this.app_data = app_data;
        this.descibeproblem = descibeproblem;
        this.gender = gender;


    }

    public String getName() {
        return name;
    }

    public String getUid() {
        return uid;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getApp_data() {
        return app_data;
    }

    public void setApp_data(String app_data) {
        this.app_data = app_data;
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

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescibeproblem() {
        return descibeproblem;
    }

    public void setDescibeproblem(String descibeproblem) {
        this.descibeproblem = descibeproblem;
    }
}
