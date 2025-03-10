package com.example.myapplication.Emtity;

import androidx.room.Entity;

@Entity(tableName = "friendships", primaryKeys = {"userId", "friendId"})
public class Friendship {
    private int userId;
    private int friendId;
    private String status;
    private String createdAt;

    // Constructor không tham số (Room yêu cầu)
    public Friendship() {
    }

    // Constructor đầy đủ tham số
    public Friendship(int userId, int friendId, String status, String createdAt) {
        this.userId = userId;
        this.friendId = friendId;
        this.status = status;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getFriendId() {
        return friendId;
    }

    public void setFriendId(int friendId) {
        this.friendId = friendId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
