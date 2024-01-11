package com.hoangquocthai.chatapp.dto;

import com.hoangquocthai.chatapp.object.User;

import java.util.List;

public class GroupChatRequestDto {
    private String username;
    private List<String> users;
    private String roomName;

    public GroupChatRequestDto(){
    }

    public GroupChatRequestDto(String username, List<String> users, String roomName) {
        this.username = username;
        this.users = users;
        this.roomName = roomName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
}
