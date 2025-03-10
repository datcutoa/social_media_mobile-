package com.example.myapplication.Emtity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "likes")
public class Like {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private int userId;
    private int postId;
    private int commentId;
    private String createdAt;

    // Constructor không tham số (Room yêu cầu)
    public Like() {
    }

    // Constructor đầy đủ tham số
    public Like(int id, int userId, int postId, int commentId, String createdAt) {
        this.id = id;
        this.userId = userId;
        this.postId = postId;
        this.commentId = commentId;
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

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
