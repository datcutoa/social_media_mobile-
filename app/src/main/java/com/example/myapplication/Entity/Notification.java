package com.example.myapplication.Entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "notifications")
public class Notification {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private int userId;
    private String type;
    private int referenceId;
    private String content;
    private boolean readStatus;
    private String createdAt;

    // Constructor không tham số (Room yêu cầu)
    public Notification() {
    }

    // Constructor đầy đủ tham số
    public Notification(int id, int userId, String type, int referenceId, String content, boolean readStatus, String createdAt) {
        this.id = id;
        this.userId = userId;
        this.type = type;
        this.referenceId = referenceId;
        this.content = content;
        this.readStatus = readStatus;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(int referenceId) {
        this.referenceId = referenceId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isReadStatus() {
        return readStatus;
    }

    public void setReadStatus(boolean readStatus) {
        this.readStatus = readStatus;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
