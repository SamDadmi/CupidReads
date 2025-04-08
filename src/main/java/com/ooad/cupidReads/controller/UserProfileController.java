package com.ooad.cupidReads.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@RequestMapping("/profile")
@SessionAttributes("user")
public class UserProfileController {  // Renamed class

    @GetMapping
    public String showProfile(Model model) {
        model.addAttribute("profilePicture", "/images/default-profile.jpg");
        model.addAttribute("userName", "John Doe");
        model.addAttribute("userBio", "Lover of books and adventures.");
        model.addAttribute("favoriteGenres", "Fantasy, Sci-Fi, Mystery");
        model.addAttribute("favoriteLanguages", "English, Spanish");

        return "profile"; // Maps to profile.html in templates/
    }
}
