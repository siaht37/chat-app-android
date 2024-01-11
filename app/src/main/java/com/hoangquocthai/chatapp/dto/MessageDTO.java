package com.hoangquocthai.chatapp.dto;

import com.hoangquocthai.chatapp.object.Message;
import com.hoangquocthai.chatapp.object.User;

public class MessageDTO {
    private User user;
    private Message message;

    public MessageDTO(){

    }

    public MessageDTO(User user, Message message) {
        this.user = user;
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
