package com.example.mychatapplication.model;

import com.google.firebase.Timestamp;

import java.util.List;

public class ChatroomModel {
    String chatroomID;
    List<String> UserIDs;

    public String getChatroomID() {
        return chatroomID;
    }

    public void setChatroomID(String chatroomID) {
        this.chatroomID = chatroomID;
    }

    public List<String> getUserIDs() {
        return UserIDs;
    }

    public void setUserIDs(List<String> userIDs) {
        UserIDs = userIDs;
    }

    public Timestamp getLastMessageTimestamp() {
        return lastMessageTimestamp;
    }

    public void setLastMessageTimestamp(Timestamp lastMessageTimestamp) {
        this.lastMessageTimestamp = lastMessageTimestamp;
    }

    public String getLastMessageSenderID() {
        return lastMessageSenderID;
    }

    public void setLastMessageSenderID(String lastMessageSenderID) {
        this.lastMessageSenderID = lastMessageSenderID;
    }

    Timestamp lastMessageTimestamp;
    String lastMessageSenderID;

    public ChatroomModel(String chatroomID, List<String> userIDs, Timestamp lastMessageTimestamp, String lastMessageSenderID) {
        this.chatroomID = chatroomID;
        UserIDs = userIDs;
        this.lastMessageTimestamp = lastMessageTimestamp;
        this.lastMessageSenderID = lastMessageSenderID;
    }

    public ChatroomModel() {
    }
}
