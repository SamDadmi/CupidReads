package com.example.demo.service;

import com.example.demo.model.User;

public interface IUserService {
    boolean addBookToWishlist(String username, String bookId);
    User findByUsername(String username);
    boolean registerUser(String username, String email, String rawPassword);
    User getUserByUsername(String username);
    void updateUser(User user);
    void upgradeToPremium(String username);
    boolean isUserPremium(String username);
    long getTotalUsers();
    long getPremiumUsers();
    long getActiveUsers();
    long getNewUsersLast7Days();
    double getAverageBooksPerUser();
}
