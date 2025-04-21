package com.example.demo.controller;

import com.example.demo.model.ClubChat;
import com.example.demo.service.ClubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@Controller
public class ChatWebSocketController {

    @Autowired
    private ClubService clubService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Data
    public static class ChatMessage {
        private String message;
        private String username;
        private String type;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    @MessageMapping("/chat/{clubName}")
    @SendTo("/topic/club/{clubName}")
    public ClubChat sendMessage(@DestinationVariable String clubName, 
                              ChatMessage chatMessage,
                              SimpMessageHeaderAccessor headerAccessor) {
        System.out.println("Received chat message: " + chatMessage);
        System.out.println("For club: " + clubName);
        System.out.println("From user: " + chatMessage.getUsername());
        
        try {
            // Save message to database and get the saved message
            ClubChat savedChat = clubService.addClubChat(clubName, chatMessage.getMessage(), chatMessage.getUsername());
            
            // Log the saved message
            System.out.println("Saved chat message: " + savedChat);
            
            // Manually send the message to ensure it's being sent
            messagingTemplate.convertAndSend("/topic/club/" + clubName, savedChat);
            
            return savedChat;
        } catch (Exception e) {
            System.err.println("Error processing message: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
} 