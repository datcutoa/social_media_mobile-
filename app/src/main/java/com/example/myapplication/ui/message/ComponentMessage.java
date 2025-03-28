package com.example.myapplication.ui.message;

public class ComponentMessage {
    private int userId;
    private String name;
    private String profilePicture;
    private String lastMessage;
    private String lastMessageTime;

    public ComponentMessage(int userId, String name, String profilePicture, String lastMessage, String lastMessageTime) {
        this.userId = userId;
        this.name = name;
        this.profilePicture = profilePicture;
        this.lastMessage = lastMessage;
        this.lastMessageTime = lastMessageTime;
    }

    public int getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public String getLastMessageTime() {
        return lastMessageTime;
    }
}
