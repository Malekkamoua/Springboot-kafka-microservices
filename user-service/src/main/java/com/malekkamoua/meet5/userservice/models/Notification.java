package com.malekkamoua.meet5.userservice.models;

import java.sql.Timestamp;

public class Notification {

    private Long userID;
    private String message;

    public Notification(Long userID, String message) {
        this.userID = userID;
        this.message = message;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
