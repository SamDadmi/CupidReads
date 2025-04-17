package com.example.demo.model;

import java.time.LocalDateTime;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Node("ClubChat")
public class ClubChat {

    @Id @GeneratedValue
    private Long id;

    private String message;
    private LocalDateTime timestamp;

    @JsonIgnoreProperties({"clubs", "password", "roles", "authorities", "accountNonExpired", "accountNonLocked", "credentialsNonExpired", "enabled"})
    @Relationship(type = "SENT_BY", direction = Relationship.Direction.OUTGOING)
    private User user;

    @JsonIgnoreProperties({"members", "invitedEmails", "createdBy"})
    @Relationship(type = "BELONGS_TO", direction = Relationship.Direction.OUTGOING)
    private Club club;

    // Default constructor
    public ClubChat() {
        this.timestamp = LocalDateTime.now();
    }

    // Constructor for creating a new chat message
    public ClubChat(String message, User user, Club club) {
        this();
        this.message = message;
        this.user = user;
        this.club = club;
    }

    // --- Getters and Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Club getClub() {
        return club;
    }

    public void setClub(Club club) {
        this.club = club;
    }

    @Override
    public String toString() {
        return "ClubChat{" +
            "id=" + id +
            ", message='" + message + '\'' +
            ", timestamp=" + timestamp +
            ", user=" + (user != null ? user.getUsername() : "null") +
            ", club=" + (club != null ? club.getName() : "null") +
            '}';
    }
}