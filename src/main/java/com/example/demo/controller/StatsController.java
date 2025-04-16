package com.example.demo.controller;

import com.example.demo.service.UserService;
import com.example.demo.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class StatsController {
    private static final Logger log = LoggerFactory.getLogger(StatsController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;

    @GetMapping("/stats")
    public String showStats(Authentication auth, Model model) {
        try {
            log.info("Accessing stats page. Current user: {}", auth != null ? auth.getName() : "null");
            
            // Check if user is admin
            if (auth == null || !auth.getName().equals("admin")) {
                log.warn("Unauthorized access attempt to stats page by user: {}", 
                        auth != null ? auth.getName() : "null");
                return "redirect:/";
            }

            // Add statistics to the model
            model.addAttribute("totalUsers", userService.getTotalUsers());
            model.addAttribute("premiumUsers", userService.getPremiumUsers());
            model.addAttribute("totalBooks", bookService.getAllBooks().size());
            model.addAttribute("activeUsers", userService.getActiveUsers());
            model.addAttribute("newUsers", userService.getNewUsersLast7Days());
            model.addAttribute("avgBooksPerUser", String.format("%.2f", userService.getAverageBooksPerUser()));
            model.addAttribute("storageUsage", "75%"); // This would be calculated based on actual storage usage

            log.info("Successfully loaded stats page for admin user");
            return "stats";
        } catch (Exception e) {
            log.error("Error loading stats page", e);
            throw e; // Re-throw to see the error in the browser
        }
    }
} 