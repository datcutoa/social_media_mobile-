package com.example.myapplication.Emtity;

import androidx.room.Entity;

@Entity(tableName = "follows", primaryKeys = {"followerId", "followingId"})
public class Follow {
    private int followerId;
    private int followingId;
    private String createdAt;

    // Constructor không tham số (Room yêu cầu)
    public Follow() {
    }

    // Constructor đầy đủ tham số
    public Follow(int followerId, int followingId, String createdAt) {
        this.followerId = followerId;
        this.followingId = followingId;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getFollowerId() {
        return followerId;
    }

    public void setFollowerId(int followerId) {
        this.followerId = followerId;
    }

    public int getFollowingId() {
        return followingId;
    }

    public void setFollowingId(int followingId) {
        this.followingId = followingId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
