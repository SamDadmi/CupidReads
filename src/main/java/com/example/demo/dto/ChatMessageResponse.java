package com.example.demo.dto;

import java.time.LocalDateTime;

public class ChatMessageResponse {
    private Long id;
    private String message;
    private LocalDateTime timestamp;
    private String username;

    public ChatMessageResponse(Long id, String message, LocalDateTime timestamp, String username) {
        this.id = id;
        this.message = message;
        this.timestamp = timestamp;
        this.username = username;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getUsername() {
        return username;
    }
} 