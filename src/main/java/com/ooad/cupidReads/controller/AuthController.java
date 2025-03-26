package com.ooad.cupidReads.controller;

import ch.qos.logback.core.model.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";  // Make sure "login.html" exists in templates/
    }

    @GetMapping("/signup")
    public String showSignUpPage() {
        return "signup";  // Make sure "signup.html" exists in templates/
    }

    @PostMapping("/signup")
    public String registerUser(
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String password,
            Model model) {

        // Since there's no UserService yet, just return a message
        model.addText("Signup successful! (Database integration coming soon.)");

        return "signup"; // Stay on signup page for now, but show success message
    }
}
