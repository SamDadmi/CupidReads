package com.example.demo.controller;

import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;
import com.example.demo.model.User;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.stereotype.Controller;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/{userId}/swipe-books/{bookId}")
    public String addBookToWishlist(@PathVariable String userId, @PathVariable String bookId, Model model) {
        boolean success = userService.addBookToWishlist(userId, bookId);
        model.addAttribute("message", success ? "Book added!" : "Failed to add.");
        return "wishlist"; // this must match your wishlist.html template
    }

    @GetMapping("/profile")
    public String viewProfile(Authentication auth, Model model, RedirectAttributes redirectAttributes) {
        try {
            String username = auth.getName();
            User user = userService.getUserByUsername(username);

            if (user != null) {
                model.addAttribute("user", user);
            } else {
                redirectAttributes.addFlashAttribute("error", "User not found.");
                return "redirect:/";
            }

            return "profile"; // profile.html
        } catch (Exception e) {
            // Optionally use log.error here if you're using logging
            redirectAttributes.addFlashAttribute("error", "Something went wrong: " + e.getMessage());
            return "redirect:/";
        }
    }

    @PostMapping("/profile/update")
    public String updateProfile(
            @RequestParam String username,
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
                // Update user details
                user.setUsername(username);
                user.setEmail(email);
                user.setFavoriteGenres(favoriteGenres);
                user.setFavoriteLanguages(favoriteLanguages);
                
                // Handle profile picture upload if provided
                if (profilePicture != null && !profilePicture.isEmpty()) {
                    // Here you would implement the logic to save the profile picture
                    // For now, we'll just update the other fields
                }
                
                // Save the updated user
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

}

