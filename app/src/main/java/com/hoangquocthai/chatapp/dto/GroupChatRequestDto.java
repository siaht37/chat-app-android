package com.hoangquocthai.chatapp.dto;

import com.hoangquocthai.chatapp.object.User;

import java.util.List;

public class GroupChatRequestDto {
    private String username;
    private List<String> users;

    public GroupChatRequestDto(){
    }

    public GroupChatRequestDto(String username, List<String> users) {
        this.username = username;
        this.users = users;
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
}
