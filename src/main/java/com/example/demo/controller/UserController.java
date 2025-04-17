package com.example.demo.controller;

import com.example.demo.service.UserService;
import com.example.demo.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;
import com.example.demo.model.User;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import java.io.IOException;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping("/{userId}/swipe-books/{bookId}")
    public String addBookToWishlist(@PathVariable String userId, @PathVariable String bookId, Model model) {
        boolean success = userService.addBookToWishlist(userId, bookId);
        model.addAttribute("message", success ? "Book added!" : "Failed to add.");
        return "wishlist"; // this must match your wishlist.html template
    }

    @GetMapping("/profile/picture/{userId}")
    public ResponseEntity<byte[]> getProfilePicture(@PathVariable String userId) {
        User user = userService.getUserById(userId);
        if (user != null && user.getProfilePictureData() != null) {
            return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(user.getProfilePictureContentType()))
                .body(user.getProfilePictureData());
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/profile")
    public String viewProfile(Authentication auth, Model model, RedirectAttributes redirectAttributes) {
        try {
            String username = auth.getName();
            User user = userService.getUserByUsername(username);

            if (user != null) {
                model.addAttribute("userName", user.getUsername());
                model.addAttribute("userEmail", user.getEmail());
                model.addAttribute("userBio", user.getBio());
                model.addAttribute("favoriteGenres", user.getFavoriteGenres());
                model.addAttribute("favoriteLanguages", user.getFavoriteLanguages());
                
                // Set profile picture URL
                if (user.getProfilePictureData() != null) {
                    model.addAttribute("profilePicture", "/users/profile/picture/" + user.getUser_id());
                } else {
                    model.addAttribute("profilePicture", "/images/default-profile.png");
                }
            } else {
                redirectAttributes.addFlashAttribute("error", "User not found.");
                return "redirect:/";
            }

            return "profile";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Something went wrong: " + e.getMessage());
            return "redirect:/";
        }
    }

    @PostMapping("/profile/update")
    public String updateProfile(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam(required = false) String bio,
            @RequestParam(required = false) String favoriteGenres,
            @RequestParam(required = false) String favoriteLanguages,
            @RequestParam(required = false) MultipartFile profilePicture,
            Authentication auth,
            RedirectAttributes redirectAttributes) {
        
        try {
            String currentUsername = auth.getName();
            User user = userService.getUserByUsername(currentUsername);
            
            if (user != null) {
                user.setUsername(name);
                user.setEmail(email);
                user.setBio(bio);
                user.setFavoriteGenres(favoriteGenres);
                user.setFavoriteLanguages(favoriteLanguages);
                
                // Handle profile picture upload
                if (profilePicture != null && !profilePicture.isEmpty()) {
                    try {
                        user.setProfilePictureData(profilePicture.getBytes());
                        user.setProfilePictureContentType(profilePicture.getContentType());
                    } catch (IOException e) {
                        redirectAttributes.addFlashAttribute("error", "Error processing profile picture: " + e.getMessage());
                        return "redirect:/users/profile";
                    }
                }
                
                userService.updateUser(user);
                redirectAttributes.addFlashAttribute("success", "Profile updated successfully!");
            } else {
                redirectAttributes.addFlashAttribute("error", "User not found.");
            }
            
            return "redirect:/users/profile";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update profile: " + e.getMessage());
            return "redirect:/users/profile";
        }
    }

    @GetMapping("/premium")
    public String premiumPage(Model model, Authentication auth) {
        String username = auth.getName();
        User user = userService.findByUsername(username);

        System.out.println("Authenticated User ID: " + username);
        System.out.println("isPremium " + user.getIsPremium());



        if (user == null) {
            model.addAttribute("error", "User not found!");
            return "error"; // Ensure you have 'error.html' to handle this
        }

        System.out.println("User Premium Status: " + user.getIsPremium()); // Debug log
        model.addAttribute("isPremium", user.getIsPremium());
        return "premium";
    }

    @PostMapping("/upgrade")
    public String upgradeUser(Authentication auth) {
        String username = auth.getName(); // or however you identify users
        User user = userService.findByUsername(username);

        System.out.println("Authenticated User ID: " + username);

        if (user == null) {
            System.out.println("❌ User not found for upgrade!");
            return "redirect:/error"; // Or return an error view if needed
        }

        System.out.println("isPremium " + user.getIsPremium());

        userService.upgradeToPremium(username);
        return "redirect:/users/premium"; // make sure this matches your GET mapping
    }
}

