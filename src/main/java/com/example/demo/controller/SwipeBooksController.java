package com.example.demo.controller;

import com.example.demo.repository.BookRepo;
import com.example.demo.service.UserService;
import com.example.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class SwipeBooksController {

    @Autowired
    private BookRepo bookRepository;

    @Autowired
    private UserService userService;

    @GetMapping("/swipe-books")
    public String viewBooks(Model model) {
        model.addAttribute("books", bookRepository.findAll());
        return "wishlist";
    }

    // @GetMapping("/my-books")
    // public String displayWishlist(Model model) {
    //     model.addAttribute("books", bookRepository.findAll());
    //     return "my-books";
    // }

    // @PostMapping("/wishlist/add")
    // public String addToWishlist(@RequestParam String userId, @RequestParam String bookId) {
    //     boolean added = userService.addBookToWishlist(userId, bookId);
    //     System.out.println("GET request received at /wishlist/add");
    //     return "redirect:/swipe-books";
    // }

    @PostMapping("/wishlist/add")
    public String addToWishlist(@RequestParam String bookId) {
        // Get the username (userId) of the logged-in user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();  // This is the currently logged-in user's username
        
        boolean added = userService.addBookToWishlist(username, bookId);
        System.out.println("POST request received at /wishlist/add for user: " + username);

        return "redirect:/swipe-books";
    }
}
