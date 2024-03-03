package com.malekkamoua.meet5.userservice.models.kafka;

import java.sql.Timestamp;

public class InteractionResponse {

    private long id;
    private String interactor_id;
    private String interacted_with_id;
    private String interactionType;
    private Timestamp timeStamp;

    public InteractionResponse() {}

    public InteractionResponse(long id, String interactor_id, String interacted_with_id, String interactionType, Timestamp timeStamp) {
        this.id = id;
        this.interactor_id = interactor_id;
        this.interacted_with_id = interacted_with_id;
        this.interactionType = interactionType;
        this.timeStamp = timeStamp;
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
