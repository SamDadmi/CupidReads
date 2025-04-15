package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.core.Authentication;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import lombok.extern.slf4j.Slf4j;

import com.example.demo.service.ClubService;  // Adjust package as per your structure

@Controller
@Slf4j
public class ClubController {

    @Autowired
    private ClubService clubService;

    @GetMapping("/clubs")
    public String viewClubs(Model model) {
        model.addAttribute("clubs", clubService.getAllClubs());
        return "clubs";
    }

    @PostMapping("/clubs/create")
    public String createClub(@RequestParam String name,
                             @RequestParam String description,
                             @RequestParam String clubType,
                             @RequestParam(required = false) String invites,
                             Authentication auth,
                             RedirectAttributes redirectAttributes) {
        try {
            String username = auth.getName();
            log.info("Creating club: {} by user: {}", name, username);
            clubService.createClub(name, description, clubType, invites, username);
            return "redirect:/clubs";
        } catch (Exception e) {
            log.error("Error creating club: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("error", "Failed to create club: " + e.getMessage());
            return "redirect:/clubs";
        }
    }

    @PostMapping("/clubs/join")
    public String joinClub(@RequestParam String name, 
                          Authentication auth,
                          RedirectAttributes redirectAttributes) {
        try {
            String username = auth.getName();
            clubService.joinClub(name, username);
            return "redirect:/clubs";
        } catch (Exception e) {
            log.error("Error joining club: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("error", "Failed to join club: " + e.getMessage());
            return "redirect:/clubs";
        }
    }
}