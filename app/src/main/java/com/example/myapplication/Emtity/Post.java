package com.example.myapplication.Emtity;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "post")
public class Post {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int userId;
    private String content;
    private String mediaUrl;
    private String privacy;
    private String createdAt;
    private int likeCount;

    // Constructor used by Room (includes all fields)
    public Post(int userId, String content, String mediaUrl, String privacy, String createdAt, int likeCount) {
        this.userId = userId;
        this.content = content;
        this.mediaUrl = mediaUrl;
        this.privacy = privacy;
        this.createdAt = createdAt;
        this.likeCount = likeCount;
    }

    // Convenience constructor (ignored by Room)
    @Ignore
    public Post(int userId, String content, String mediaUrl, String privacy, String createdAt) {
        this(userId, content, mediaUrl, privacy, createdAt, 0);
    }

    // Getters
    public int getId() { return id; }
    public int getUserId() { return userId; }
    public String getContent() { return content; }
    public String getMediaUrl() { return mediaUrl; }
    public String getPrivacy() { return privacy; }
    public String getCreatedAt() { return createdAt; }
    public int getLikeCount() { return likeCount; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setUserId(int userId) { this.userId = userId; }
    public void setContent(String content) { this.content = content; }
    public void setMediaUrl(String mediaUrl) { this.mediaUrl = mediaUrl; }
    public void setPrivacy(String privacy) { this.privacy = privacy; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public void setLikeCount(int likeCount) { this.likeCount = likeCount; }
}