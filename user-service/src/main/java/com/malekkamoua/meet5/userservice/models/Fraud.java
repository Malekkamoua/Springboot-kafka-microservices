package com.malekkamoua.meet5.userservice.models;

import java.sql.Timestamp;

public class Fraud {

    private Long fraudulentUserId;
    private String cause;
    private Timestamp timestamp;

    public Fraud(Long fraudulentUserId, String cause, Timestamp timestamp) {
        this.fraudulentUserId = fraudulentUserId;
        this.cause = cause;
        this.timestamp = timestamp;
    }

    public Long getFraudulentUserId() {
        return fraudulentUserId;
    }

    public void setFraudulentUserId(Long fraudulentUserId) {
        this.fraudulentUserId = fraudulentUserId;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Fraud{" +
                "fraudulentUserId='" + fraudulentUserId + '\'' +
                ", cause='" + cause + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
