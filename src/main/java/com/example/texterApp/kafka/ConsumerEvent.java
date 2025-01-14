package com.example.texterApp.kafka;

import com.example.texterApp.entities.User;

import java.time.LocalDateTime;

public class ConsumerEvent {
    private String message;
    private LocalDateTime timestamp;
    private User user;
    private String status;

    public ConsumerEvent() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
