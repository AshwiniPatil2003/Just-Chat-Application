package com.example.mychatapplication.model;

import com.google.firebase.Timestamp;

public class ChatMessageModel {
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public ChatMessageModel(String message, String senderID, Timestamp timestamp) {
        this.message = message;
        this.senderID = senderID;
        this.timestamp = timestamp;
    }

    public ChatMessageModel() {
    }

    private String senderID;
    private Timestamp timestamp;
}
