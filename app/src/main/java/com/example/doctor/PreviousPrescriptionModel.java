package com.example.doctor;

import com.google.firebase.Timestamp;

public class PreviousPrescriptionModel {
    private String suggested_by,user_id,image_url;
    private Timestamp timestamp;


    public PreviousPrescriptionModel() {}


    public String getSuggested_by() {
        return suggested_by;
    }

    public void setSuggested_by(String suggested_by) {
        this.suggested_by = suggested_by;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
