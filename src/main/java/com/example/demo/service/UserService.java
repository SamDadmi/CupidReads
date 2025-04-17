package com.example.demo.service;
import com.example.demo.repository.BookRepo;
import com.example.demo.service.IUserService;
import com.example.demo.model.Book;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class UserService implements IUserService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private BookRepo bookRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean addBookToWishlist(String username, String bookId) {
        Optional<User> userOptional = userRepo.findByUsername(username);
        User user = userOptional.orElse(null);  // returns null if not found
        if (user != null) {
            Book book = bookRepo.findById(bookId).orElse(null);
            if (book != null) {
                user.getWishlist().add(book);
                userRepo.save(user);  // Save the updated user with the new book in the wishlist
                return true;
            }
        }
        return false;  // Return false if the user or book was not found
    }

    public User findByUsername(String username) {
    return userRepo.findByUsername(username).orElse(null);
    }

    public boolean registerUser(String username, String email, String rawPassword) {
        if (userRepo.findByUsername(username).isPresent()) return false;

        User user = new User();
        user.setUser_id(UUID.randomUUID().toString());
        user.setUsername(username);
        user.setEmail(email);
        String encodedPassword = passwordEncoder.encode(rawPassword);
        user.setPassword(encodedPassword);
        user.setRole("USER");

        userRepo.save(user);
        return true;
    }

    public User getUserByUsername(String username) {
        return userRepo.findByUsername(username).orElse(null);
    }

    public void updateUser(User user) {
        userRepo.save(user);
    }

    public void upgradeToPremium(String username) {
    Optional<User> userOpt = userRepo.findByUsername(username); // now it's clear
    if (userOpt.isPresent()) {
        User user = userOpt.get();
        user.setIsPremium(true);
        userRepo.save(user);
        System.out.println("✅ Upgraded user to premium: " + user.getUsername());
    } else {
        System.out.println("❌ Couldn't upgrade. User not found for username: " + username);
    }
}
public boolean isUserPremium(String username) {
    return userRepo.findByUsername(username)
                   .map(user -> user.getIsPremium())
                   .orElse(false);
}

public long getTotalUsers() {
    return userRepo.count();
}

public long getPremiumUsers() {
    return userRepo.countByIsPremium(true);
}

public long getActiveUsers() {
    // For now, we'll consider all users as active
    // In a real application, you might want to track last login time
    return userRepo.count();
}

public long getNewUsersLast7Days() {
    // For now, we'll return a fixed number
    // In a real application, you would track user creation date
    return 5;
}

public double getAverageBooksPerUser() {
    List<User> users = userRepo.findAll();
    if (users.isEmpty()) return 0;
    
    int totalBooks = users.stream()
        .mapToInt(user -> user.getWishlist().size())
        .sum();
    
    return (double) totalBooks / users.size();
}

public User getUserById(String userId) {
    return userRepo.findById(userId).orElse(null);
}

}

