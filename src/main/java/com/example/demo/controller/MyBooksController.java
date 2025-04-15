package com.example.demo.controller;

import com.example.demo.model.Book;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import com.example.demo.repository.BookRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class MyBooksController {

    @Autowired
    private UserService userService;

    @Autowired
    private BookRepo bookRepo;

    @GetMapping("/my-books")
    public String viewMyBooks(Authentication auth, Model model) {
        try {
            String username = auth.getName();
            User user = userService.getUserByUsername(username);
            
            if (user != null) {
                model.addAttribute("books", user.getWishlist());
                return "my-books";
            }
            return "redirect:/login";
        } catch (Exception e) {
            return "redirect:/login";
        }
    }

    @PostMapping("/my-books/update-status")
    public String updateBookStatus(
            @RequestParam String bookId,
            @RequestParam String status,
            Authentication auth) {
        try {
            String username = auth.getName();
            User user = userService.getUserByUsername(username);
            
            if (user != null) {
                Book book = bookRepo.findById(bookId).orElse(null);
                if (book != null && user.getWishlist().contains(book)) {
                    book.setStatus(status);
                    bookRepo.save(book);
                }
            }
            return "redirect:/my-books";
        } catch (Exception e) {
            return "redirect:/my-books";
        }
    }

    @PostMapping("/my-books/update-rating")
    public String updateBookRating(
            @RequestParam String bookId,
            @RequestParam int rating,
            Authentication auth) {
        try {
            String username = auth.getName();
            User user = userService.getUserByUsername(username);
            
            if (user != null) {
                Book book = bookRepo.findById(bookId).orElse(null);
                if (book != null && user.getWishlist().contains(book)) {
                    book.setRating(rating);
                    bookRepo.save(book);
                }
            }
            return "redirect:/my-books";
        } catch (Exception e) {
            return "redirect:/my-books";
        }
    }

    @PostMapping("/my-books/update-notes")
    public String updateBookNotes(
            @RequestParam String bookId,
            @RequestParam String notes,
            Authentication auth) {
        try {
            String username = auth.getName();
            User user = userService.getUserByUsername(username);
            
            if (user != null) {
                Book book = bookRepo.findById(bookId).orElse(null);
                if (book != null && user.getWishlist().contains(book)) {
                    book.setNotes(notes);
                    bookRepo.save(book);
                }
            }
            return "redirect:/my-books";
        } catch (Exception e) {
            return "redirect:/my-books";
        }
    }
} 