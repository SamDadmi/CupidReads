package com.example.demo.controller;

import com.example.demo.model.ClubChat;
import com.example.demo.dto.ChatMessageResponse;
import com.example.demo.service.ClubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/chat")
public class ChatRestController {

    @Autowired
    private ClubService clubService;

    @PostMapping("/{clubName}/messages")
    public ResponseEntity<ChatMessageResponse> sendMessage(
            @PathVariable String clubName,
            @RequestBody Map<String, String> payload) {
        String message = payload.get("message");
        String username = payload.get("username");
        ClubChat chat = clubService.addClubChat(clubName, message, username);
        
        ChatMessageResponse response = new ChatMessageResponse(
            chat.getId(),
            chat.getMessage(),
            chat.getTimestamp(),
            chat.getUser().getUsername()
        );
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{clubName}/messages")
    public ResponseEntity<List<ChatMessageResponse>> getMessages(
            @PathVariable String clubName,
            @RequestParam(defaultValue = "0") Long after) {
        List<ClubChat> messages = clubService.getClubChatsAfter(clubName, after);
        List<ChatMessageResponse> responses = messages.stream()
            .map(chat -> new ChatMessageResponse(
                chat.getId(),
                chat.getMessage(),
                chat.getTimestamp(),
                chat.getUser().getUsername()
            ))
            .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }
} 