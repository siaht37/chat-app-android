package com.hoangquocthai.chatapp.object;

import java.util.Date;

public class User {
    private String username;
    private String fullName;
    private String userAvatar;
    private Date createdAt;

    public User() {
    }

    public User(String username, String fullName, String userAvatar) {
        this.username = username;
        this.fullName = fullName;
        this.userAvatar = userAvatar;
    }

    public User(String username, String fullName, String userAvatar, Date createdAt) {
        this.username = username;
        this.fullName = fullName;
        this.userAvatar = userAvatar;
        this.createdAt = createdAt;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
