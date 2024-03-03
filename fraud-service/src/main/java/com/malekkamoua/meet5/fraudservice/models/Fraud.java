package com.malekkamoua.meet5.fraudservice.models;

import java.sql.Timestamp;

public class Fraud {

    private long id;
    private String fraudulentUserId;
    private String cause;
    private Timestamp timestamp;

    public Fraud(String fraudulentUserId, String cause) {
        this.fraudulentUserId = fraudulentUserId;
        this.cause = cause;
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFraudulentUserId() {
        return fraudulentUserId;
    }

    public void setFraudulentUserId(String fraudulentUserId) {
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
                "id=" + id +
                ", fraudulentUserId=" + fraudulentUserId +
                ", cause='" + cause + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
