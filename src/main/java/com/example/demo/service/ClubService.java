package com.example.demo.service;
import java.util.*;
import com.example.demo.model.Club;
import com.example.demo.model.User;
import com.example.demo.repository.ClubRepo;
import com.example.demo.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
// import org.springframework.security.crypto.password.PasswordEncoder;

@Service
@Slf4j
public class ClubService {
    @Autowired
    private ClubRepo clubRepo;

    @Autowired
    private UserRepo userRepo;

    @Transactional
    public Club createClub(String name, String description, String clubType, String invites, String username) {
        try {
            log.info("Attempting to create club: {} by user: {}", name, username);
            
            // Check if club name already exists
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
            
            // Process invited emails
            if (invites != null && !invites.trim().isEmpty()) {
                Set<String> invitedEmails = new HashSet<>(Arrays.asList(invites.split("\\s*,\\s*")));
                club.setInvitedEmails(invitedEmails);
            }

            club.setCreatedBy(creator);
            club = clubRepo.save(club);
            
            // Add creator as first member
            creator.getClubs().add(club);
            club.getMembers().add(creator);
            userRepo.save(creator);
            
            log.info("Successfully created club: {}", name);
            return club;
        } catch (Exception e) {
            log.error("Error creating club: {}", e.getMessage(), e);
            throw e;
        }
    }

    public void joinClub(String name, String username) {
        Club club = clubRepo.findByName(name).orElseThrow();
        User user = userRepo.findByUsername(username).orElseThrow();

        user.getClubs().add(club);
        club.getMembers().add(user);

        userRepo.save(user);
        clubRepo.save(club);
    }

    public List<Club> getAllClubs() {
        List<Club> clubs = clubRepo.findAll();
        System.out.println("Clubs found: " + clubs.size());
        return clubs;
    }
}