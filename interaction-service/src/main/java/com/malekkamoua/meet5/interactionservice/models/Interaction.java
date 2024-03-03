package com.malekkamoua.meet5.interactionservice.models;

import java.sql.Timestamp;
import java.util.Date;

public class Interaction {
    private long id;
    private String interactor_id;
    private String interacted_with_id;
    private String interactionType;
    private Timestamp timeStamp;

    public Interaction() {
    }

    public Interaction(String interactor_id, String interacted_with_id, String interactionType) {
        this.interactor_id = interactor_id;
        this.interacted_with_id = interacted_with_id;
        this.interactionType = interactionType;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getInteractor_id() {
        return interactor_id;
    }

    public void setInteractor_id(String interactor_id) {
        this.interactor_id = interactor_id;
    }

    public String getInteracted_with_id() {
        return interacted_with_id;
    }

    public void setInteracted_with_id(String interacted_with_id) {
        this.interacted_with_id = interacted_with_id;
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
}