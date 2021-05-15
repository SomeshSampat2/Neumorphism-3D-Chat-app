package com.covidhelpapp.a3d_chat.Models;

public class User {

    private String email,username,status,profileImage,userId;

    public User() {
    }

    public User(String email, String username, String status, String profileImage, String userId) {
        this.email = email;
        this.username = username;
        this.status = status;
        this.profileImage = profileImage;
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
