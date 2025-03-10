package com.example.myapplication.Emtity;

import androidx.room.Entity;

@Entity(tableName = "event_participants", primaryKeys = {"eventId", "userId"})
public class EventParticipant {
    private int eventId;
    private int userId;
    private String status;
    private String respondedAt;

    // Constructor không tham số (Room yêu cầu)
    public EventParticipant() {
    }

    // Constructor đầy đủ tham số
    public EventParticipant(int eventId, int userId, String status, String respondedAt) {
        this.eventId = eventId;
        this.userId = userId;
        this.status = status;
        this.respondedAt = respondedAt;
    }

    // Getters and Setters
    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRespondedAt() {
        return respondedAt;
    }

    public void setRespondedAt(String respondedAt) {
        this.respondedAt = respondedAt;
    }
}


