package com.example.demo.controller;

import com.example.demo.model.Club; // Import Club
import com.example.demo.model.ClubChat; // <-- Import new model
import com.example.demo.model.User; // <-- Import User model
import com.example.demo.service.ClubService;
import com.example.demo.service.UserService; // <-- Import UserService
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable; // <-- Import PathVariable
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;
import java.util.List; // <-- Import List
import java.util.stream.Collectors;

@Controller
public class ClubController {
    private static final Logger log = LoggerFactory.getLogger(ClubController.class);

    @Autowired
    private ClubService clubService;

    @Autowired // <-- Add Autowired for UserService (needed for getting current user)
    private UserService userService;

    // --- Existing Endpoints (/clubs, /clubs/create, /clubs/join) ---
    @GetMapping("/clubs")
    public String viewClubs(Model model, Authentication auth) {
        try {
            // Get current user's clubs
            String username = auth.getName();
            User currentUser = userService.getUserByUsername(username);
            
            if (currentUser != null) {
                // Get clubs the user is a member of
                model.addAttribute("myBookClubs", currentUser.getClubs());
                
                // Get all clubs except those the user is already a member of
                List<Club> allClubs = clubService.getAllClubs();
                List<Club> discoverableClubs = allClubs.stream()
                    .filter(club -> !currentUser.getClubs().contains(club))
                    .collect(Collectors.toList());
                model.addAttribute("clubs", discoverableClubs);
            } else {
                model.addAttribute("myBookClubs", Collections.emptyList());
                model.addAttribute("clubs", Collections.emptyList());
                model.addAttribute("error", "User not found");
            }
            
            return "clubs";
        } catch (Exception e) {
            log.error("Error viewing clubs page: {}", e.getMessage(), e);
            model.addAttribute("error", "Could not load clubs.");
            return "clubs";
        }
    }


    @PostMapping("/clubs/create")
    public String createClub(@RequestParam String name,
                             @RequestParam String description,
                             @RequestParam String clubType,
                             @RequestParam(required = false) String invites,
                             Authentication auth, // Get authenticated user
                             RedirectAttributes redirectAttributes) {
        try {
            String username = auth.getName(); // Get username from Authentication
            log.info("User '{}' attempting to create club: {}", username, name);
            clubService.createClub(name, description, clubType, invites, username);
            redirectAttributes.addFlashAttribute("success", "Club '" + name + "' created successfully!");
            return "redirect:/clubs";
        } catch (IllegalArgumentException e) { // Catch specific exceptions
             log.error("Illegal argument error creating club '{}': {}", name, e.getMessage());
             redirectAttributes.addFlashAttribute("error", "Failed to create club: " + e.getMessage());
             return "redirect:/clubs"; // Redirect back to the clubs page with error
         } catch (Exception e) { // Catch generic exceptions
            log.error("Generic error creating club '{}': {}", name, e.getMessage(), e);
            redirectAttributes.addFlashAttribute("error", "An unexpected error occurred while creating the club.");
            return "redirect:/clubs";
        }
    }

    // NOTE: The original /clubs/join POST mapping might need adjustment.
    // It redirects back to /clubs, but maybe it should redirect to the specific club chat page?
    // For now, keeping the original logic. You might want to change the redirect.
     @PostMapping("/clubs/join")
     public String joinClub(@RequestParam String name, // Club name is passed
                           Authentication auth,
                           RedirectAttributes redirectAttributes) {
         try {
             String username = auth.getName();
             log.info("User '{}' attempting to join club '{}'", username, name);
             clubService.joinClub(name, username); // Call service method
             redirectAttributes.addFlashAttribute("success", "Successfully joined club: " + name);
             // Consider redirecting to the club chat page instead:
             // return "redirect:/clubs/" + name;
             return "redirect:/clubs"; // Current: Redirects back to the main clubs list
         } catch (IllegalArgumentException e) {
              log.error("Failed to join club '{}' for user '{}': {}", name, auth.getName(), e.getMessage());
              redirectAttributes.addFlashAttribute("error", "Failed to join club: " + e.getMessage());
              return "redirect:/clubs";
          } catch (Exception e) {
              log.error("Unexpected error joining club '{}' for user '{}': {}", name, auth.getName(), e.getMessage(), e);
              redirectAttributes.addFlashAttribute("error", "An unexpected error occurred.");
              return "redirect:/clubs";
         }
     }


    // --- NEW Endpoints for Chat ---

    /**
     * Displays the chat page for a specific club.
     * Fetches club details and chat messages.
     * Ensures the user is a member before displaying the chat.
     */
    @GetMapping("/club/{clubName}/chats")
    public String viewClubChat(@PathVariable String clubName,
                               Authentication auth,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        try {
            log.info("Received request for club chat with name: '{}'", clubName);
            
            String username = auth.getName();
            log.info("User '{}' attempting to view chat for club '{}'", username, clubName);

            User currentUser = userService.getUserByUsername(username);
            if (currentUser == null) {
                log.error("Authenticated user '{}' not found in database", username);
                redirectAttributes.addFlashAttribute("error", "Could not identify current user.");
                return "redirect:/login";
            }

            // Get club details from database
            Club club = clubService.getClubByName(clubName);
            if (club == null) {
                log.error("Club '{}' not found in database", clubName);
                redirectAttributes.addFlashAttribute("error", "Club '" + clubName + "' not found.");
                return "redirect:/clubs";
            }

            // Check if the current user is a member of the club
            boolean isMember = club.getMembers().stream()
                                 .anyMatch(member -> member.getUsername().equals(username));

            if (!isMember) {
                if ("public".equalsIgnoreCase(club.getClubType())) {
                    log.info("Public club '{}'. Automatically joining user '{}'", clubName, username);
                    clubService.joinClub(clubName, username);
                    club = clubService.getClubByName(clubName); // Refresh club data
                } else {
                    log.warn("User '{}' is not a member of non-public club '{}'. Access denied.", username, clubName);
                    redirectAttributes.addFlashAttribute("error", "You must be a member to view this club's chat.");
                    return "redirect:/clubs";
                }
            }

            // Fetch all chat messages from database
            List<ClubChat> chats = clubService.getClubChats(clubName);
            log.info("Found {} chat messages for club '{}'", chats.size(), clubName);
            
            // Add data to model
            model.addAttribute("club", club);
            model.addAttribute("chats", chats);
            model.addAttribute("currentUser", currentUser);
            model.addAttribute("memberCount", club.getMembers().size());
            model.addAttribute("clubType", club.getClubType());
            model.addAttribute("description", club.getDescription());

            log.info("Successfully prepared club chat view for '{}'", clubName);
            return "club-chat";

        } catch (Exception e) {
            log.error("Error viewing club chat for '{}': {}", clubName, e.getMessage(), e);
            redirectAttributes.addFlashAttribute("error", "An unexpected error occurred while loading the club chat: " + e.getMessage());
            return "redirect:/clubs";
        }
    }

    /**
     * Handles posting a new chat message to a specific club.
     */
    @PostMapping("/clubs/{clubName}/chat")
    public String addClubChat(@PathVariable String clubName,
                              @RequestParam String message, // Get message from form input
                              Authentication auth,
                              RedirectAttributes redirectAttributes) {
        try {
            String username = auth.getName();
            log.info("User '{}' posting message to club '{}': {}", username, clubName, message);

            if (message == null || message.trim().isEmpty()) {
                 redirectAttributes.addFlashAttribute("error", "Message cannot be empty.");
                 return "redirect:/clubs/" + clubName;
            }

            clubService.addClubChat(clubName, message.trim(), username);
            // No success message needed usually, the page refresh shows the new message
            // redirectAttributes.addFlashAttribute("success", "Message sent!");

        } catch (IllegalArgumentException e) {
            log.error("Illegal argument error posting chat to club '{}': {}", clubName, e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Failed to send message: " + e.getMessage());
        } catch (Exception e) {
            log.error("Error posting chat to club '{}': {}", clubName, e.getMessage(), e);
            redirectAttributes.addFlashAttribute("error", "An unexpected error occurred while sending the message.");
        }

        // Redirect back to the club chat page after posting
        return "redirect:/clubs/" + clubName;
    }
}