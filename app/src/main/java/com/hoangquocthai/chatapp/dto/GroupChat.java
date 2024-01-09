package com.hoangquocthai.chatapp.dto;

import com.hoangquocthai.chatapp.object.User;

import java.io.Serializable;
import java.util.Date;

public class GroupChat implements Serializable {
    private Long groupId;
    private String groupName;
    private String groupAvatar;
    private Date createdAt;
    private User creator;

    public GroupChat(){

    }

    public GroupChat(Long groupId, String groupName, String groupAvatar, Date createdAt, User creator) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.groupAvatar = groupAvatar;
        this.createdAt = createdAt;
        this.creator = creator;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupAvatar() {
        return groupAvatar;
    }

    public void setGroupAvatar(String groupAvatar) {
        this.groupAvatar = groupAvatar;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }
}
