package com.example.demo.controller;

import com.example.demo.model.ClubChat;
import com.example.demo.model.Club;
import com.example.demo.model.User;
import com.example.demo.repository.ClubChatRepo;
import com.example.demo.repository.ClubRepo;
import com.example.demo.repository.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatRestController {
    private static final Logger logger = LoggerFactory.getLogger(ChatRestController.class);

    @Autowired
    private ClubChatRepo clubChatRepo;

    @Autowired
    private ClubRepo clubRepo;

    @Autowired
    private UserRepo userRepo;

    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(
            @RequestParam String message,
            @RequestParam String clubName,
            Authentication authentication) {
        
        try {
            logger.info("Attempting to send message to club: {}", clubName);
            
            String username = authentication.getName();
            User user = userRepo.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found: " + username));
            
            Club club = clubRepo.findByName(clubName)
                    .orElseThrow(() -> new RuntimeException("Club not found: " + clubName));
            
            ClubChat chat = new ClubChat(message, user, club);
            clubChatRepo.save(chat);
            
            logger.info("Message sent successfully to club: {}", clubName);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Error sending message to club {}: {}", clubName, e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/messages")
    public ResponseEntity<List<ClubChat>> getMessages(
            @RequestParam String clubName,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime lastMessageTime) {
        
        try {
            logger.info("Fetching messages for club: {}, after time: {}", clubName, lastMessageTime);
            
            // Validate club exists
            if (!clubRepo.findByName(clubName).isPresent()) {
                logger.error("Club not found: {}", clubName);
                return ResponseEntity.badRequest().build();
            }

            List<ClubChat> messages;
            if (lastMessageTime != null) {
                // Don't allow future timestamps
                if (lastMessageTime.isAfter(LocalDateTime.now())) {
                    lastMessageTime = LocalDateTime.now();
                }
                messages = clubChatRepo.findByClubAndTimestampAfterOrderByTimestampAsc(
                    clubName, lastMessageTime);
            } else {
                messages = clubChatRepo.findByClubNameOrderByTimestampAsc(clubName);
            }
            
            logger.info("Found {} messages for club: {}", messages.size(), clubName);
            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            logger.error("Error fetching messages for club {}: {}", clubName, e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }
} 