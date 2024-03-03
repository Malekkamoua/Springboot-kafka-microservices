package com.malekkamoua.meet5.userservice.models;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.sql.Timestamp;
import java.util.Date;

public class Interaction {
    private String interactorID;
    private String interactedWithID;
    private String interactionType;
    private Timestamp timeStamp;

    public Interaction(String interactorID, String interactedWithID, String interactionType) {
        this.interactorID = interactorID;
        this.interactedWithID = interactedWithID;
        this.interactionType = interactionType;
        this.timeStamp = new Timestamp(System.currentTimeMillis());
    }

    public String getInteractorID() {
        return interactorID;
    }

    public void setInteractorID(String interactorID) {
        this.interactorID = interactorID;
    }

    public String getInteractedWithID() {
        return interactedWithID;
    }

    public void setInteractedWithID(String interactedWithID) {
        this.interactedWithID = interactedWithID;
    }

    public String getInteractionType() {
        return interactionType;
    }

    public void setInteractionType(String interactionType) {
        this.interactionType = interactionType;
    }

    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Timestamp timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString() {
        return "Interaction{" +
                "interactorID='" + interactorID + '\'' +
                ", interactedWithID='" + interactedWithID + '\'' +
                ", interactionType='" + interactionType + '\'' +
                ", timeStamp=" + timeStamp +
                '}';
    }
}
