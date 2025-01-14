package com.example.texterApp.kafka;

import java.time.LocalDateTime;

public class ProducerEvent {
    private String message;
    private LocalDateTime timestamp;
    private String username;
    private String status;

    public ProducerEvent() {
    }

    public ProducerEvent(String message, LocalDateTime timestamp) {
        this.message = message;
        this.timestamp = timestamp;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
