package com.example.myapplication.Emtity;

import androidx.room.Entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String username;
    private String email;
    private String password;
    private String name;
    private String bio;
    private String profilePicture;
    private String coverPhoto;
    private String gender;
    private String birthdate;
    private String createdAt;
    public  User(){}

    public User(int id,String username, String password, String email,  String name, String bio, String coverPhoto, String profilePicture, String gender, String birthdate, String createdAt) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.id = id;
        this.name = name;
        this.bio = bio;
        this.coverPhoto = coverPhoto;
        this.profilePicture = profilePicture;
        this.gender = gender;
        this.birthdate = birthdate;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBio() {
        return bio;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public String getCoverPhoto() {
        return coverPhoto;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getEmail() {
        return email;
    }

    public String getGender() {
        return gender;
    }

    public String getPassword() {
        return password;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public String getUsername() {
        return username;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public void setCoverPhoto(String coverPhoto) {
        this.coverPhoto=coverPhoto;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public void setUsername(String username) {
        this.username = username;
    }

// Getters and Setters
}

