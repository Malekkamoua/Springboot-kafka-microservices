package com.malekkamoua.meet5.userservice.models;

import java.sql.Timestamp;

public class Visitor {

    private String username;
    private String gender;
    private Timestamp timestamp;

    public Visitor(String username, String gender, Timestamp timestamp) {
        this.username = username;
        this.gender = gender;
        this.timestamp = timestamp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
