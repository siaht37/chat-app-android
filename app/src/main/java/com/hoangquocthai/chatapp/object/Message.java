package com.hoangquocthai.chatapp.object;

import com.hoangquocthai.chatapp.dto.GroupChat;

import java.util.Date;

public class Message {
    private Long idMessage;
    private String content;
    private Date createdAt;

    private User sender;

    private GroupChat groupChat;

    private Type type;

    public Message() {
    }

    public Message(Long idMessage, String content, Date createdAt, User sender, GroupChat groupChat, Type type) {
        this.idMessage = idMessage;
        this.content = content;
        this.createdAt = createdAt;
        this.sender = sender;
        this.groupChat = groupChat;
        this.type = type;
    }

    public Long getIdMessage() {
        return idMessage;
    }

    public void setIdMessage(Long idMessage) {
        this.idMessage = idMessage;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public GroupChat getGroupChat() {
        return groupChat;
    }

    public void setGroupChat(GroupChat groupChat) {
        this.groupChat = groupChat;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
