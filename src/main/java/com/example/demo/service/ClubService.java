package com.example.demo.service;
import java.util.*;
import com.example.demo.model.Club;
import com.example.demo.model.User;
import com.example.demo.model.ClubChat;
import com.example.demo.repository.ClubRepo;
import com.example.demo.repository.UserRepo;
import com.example.demo.repository.ClubChatRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
// import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ClubService {
    private static final Logger log = LoggerFactory.getLogger(ClubService.class);

    @Autowired
    private ClubRepo clubRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired 
    private ClubChatRepo clubChatRepo;

    @Transactional
    public Club createClub(String name, String description, String clubType, String invites, String username) {
        try {
            log.info("Attempting to create club: {} by user: {}", name, username);

            if (clubRepo.findByName(name).isPresent()) {
                log.error("Club with name {} already exists", name);
                throw new IllegalArgumentException("Club name already exists");
            }

            User creator = userRepo.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));

            Club club = new Club();
            club.setName(name);
            club.setDescription(description);
            club.setClubType(clubType);

            if (invites != null && !invites.trim().isEmpty()) {
                Set<String> invitedEmails = new HashSet<>(Arrays.asList(invites.split("\\s*,\\s*")));
                club.setInvitedEmails(invitedEmails);
            }

            club.setCreatedBy(creator);

            // Add creator as the first member automatically
            Set<User> members = new HashSet<>();
            members.add(creator);
            club.setMembers(members); // Set the initial members

            Club savedClub = clubRepo.save(club); // Save the club first

            // Update the user side of the relationship (optional but good practice for bidirectional mapping)
            creator.getClubs().add(savedClub);
            userRepo.save(creator);


            log.info("Successfully created club: {}", name);
            return savedClub;
        } catch (Exception e) {
            log.error("Error creating club: {}", e.getMessage(), e);
            throw e; // Rethrow the exception to be handled by the controller
        }
    }

    @Transactional
    public void joinClub(String clubName, String username) {
        log.info("User {} attempting to join club {}", username, clubName);
        
        // First, verify both entities exist
        Club club = clubRepo.findByName(clubName)
                .orElseThrow(() -> new IllegalArgumentException("Club not found: " + clubName));

        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));

        // Check if user is already a member
        if (club.getMembers().stream().anyMatch(member -> member.getUsername().equals(username))) {
             log.warn("User {} is already a member of club {}", username, clubName);
             return;
        }

        // Add user to the club's members
        club.getMembers().add(user);

        // Add club to the user's clubs
        user.getClubs().add(club);

        // Save both entities to persist the relationship changes
        clubRepo.save(club);
        userRepo.save(user);
        
        log.info("User {} successfully joined club {}", username, clubName);
    }


    public List<Club> getAllClubs() {
        List<Club> clubs = clubRepo.findAll();
        log.info("Retrieved {} clubs", clubs.size());
        return clubs;
    }

    // --- NEW Methods for Chat Functionality ---

    /**
     * Retrieves all chat messages for a specific club, ordered by time.
     * @param clubName The name of the club.
     * @return List of ClubChat messages.
     */
    public List<ClubChat> getClubChats(String clubName) {
        try {
            log.info("Fetching chats for club: {}", clubName);
            
            // First verify the club exists
            Club club = clubRepo.findByName(clubName)
                .orElseThrow(() -> {
                    log.error("Club not found while fetching chats: {}", clubName);
                    return new IllegalArgumentException("Club not found: " + clubName);
                });
            
            // Use the custom query method from ClubChatRepo
            List<ClubChat> chats = clubChatRepo.findByClubNameOrderByTimestampAsc(clubName);
            log.info("Found {} chats for club: {}", chats.size(), clubName);
            
            if (chats.isEmpty()) {
                log.info("No chat messages found for club: {}", clubName);
            }
            
            return chats;
        } catch (Exception e) {
            log.error("Error fetching chats for club {}: {}", clubName, e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Retrieves a single club by its name.
     * @param name The name of the club.
     * @return The Club object.
     * @throws IllegalArgumentException if the club is not found.
     */
    public Club getClubByName(String name) {
        log.info("Fetching club by name: {}", name);
        return clubRepo.findByName(name)
                .orElseThrow(() -> {
                    log.error("Club not found: {}", name);
                    return new IllegalArgumentException("Club not found: " + name);
                });
    }

    /**
     * Adds a new chat message to a club.
     * @param clubName The name of the club to add the message to.
     * @param message The content of the message.
     * @param username The username of the user sending the message.
     * @return The saved ClubChat object.
     * @throws IllegalArgumentException if the club or user is not found, or if the user is not a member.
     */
    @Transactional // Ensure atomicity
    public ClubChat addClubChat(String clubName, String message, String username) {
        log.debug("Adding chat message '{}' to club '{}' by user '{}'", message, clubName, username);

        Club club = clubRepo.findByName(clubName)
            .orElseThrow(() -> {
                log.error("Club not found while adding chat: {}", clubName);
                return new IllegalArgumentException("Club not found: " + clubName);
            });

        User user = userRepo.findByUsername(username)
            .orElseThrow(() -> {
                log.error("User not found while adding chat: {}", username);
                return new IllegalArgumentException("User not found: " + username);
            });

        // Crucial check: Ensure the user is actually a member of the club before allowing them to post.
        // We need to compare by a unique identifier, like user_id or username.
        boolean isMember = club.getMembers().stream()
                               .anyMatch(member -> member.getUsername().equals(username));

        if (!isMember) {
            log.error("User '{}' attempted to post in club '{}' but is not a member.", username, clubName);
            throw new IllegalArgumentException("User is not a member of this club and cannot post messages.");
        }

        // Create and save the new chat message
        ClubChat chat = new ClubChat(message, user, club);
        ClubChat savedChat = clubChatRepo.save(chat);
        log.info("Successfully saved chat message with ID: {}", savedChat.getId());
        return savedChat;
    }
}