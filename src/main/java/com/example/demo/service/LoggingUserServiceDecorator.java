package com.example.demo.service;

import com.example.demo.model.User;

public class LoggingUserServiceDecorator implements IUserService {

    private final IUserService delegate;

    public LoggingUserServiceDecorator(IUserService delegate) {
        this.delegate = delegate;
    }

    @Override
    public boolean addBookToWishlist(String username, String bookId) {
        System.out.println("📚 [LOG] Adding book to wishlist for user: " + username);
        return delegate.addBookToWishlist(username, bookId);
    }

    @Override
    public User findByUsername(String username) {
        System.out.println("🔍 [LOG] Finding user by username: " + username);
        return delegate.findByUsername(username);
    }

    @Override
    public boolean registerUser(String username, String email, String rawPassword) {
        System.out.println("✍️ [LOG] Registering user: " + username);
        return delegate.registerUser(username, email, rawPassword);
    }

    @Override
    public User getUserByUsername(String username) {
        System.out.println("🔍 [LOG] Getting user by username: " + username);
        return delegate.getUserByUsername(username);
    }

    @Override
    public void updateUser(User user) {
        System.out.println("📝 [LOG] Updating user: " + user.getUsername());
        delegate.updateUser(user);
    }

    @Override
    public void upgradeToPremium(String username) {
        System.out.println("⚡ [LOG] Upgrading user to premium: " + username);
        delegate.upgradeToPremium(username);
    }

    @Override
    public boolean isUserPremium(String username) {
        System.out.println("🔒 [LOG] Checking if user is premium: " + username);
        return delegate.isUserPremium(username);
    }

    @Override
    public long getTotalUsers() {
        System.out.println("📊 [LOG] Fetching total users count.");
        return delegate.getTotalUsers();
    }

    @Override
    public long getPremiumUsers() {
        System.out.println("📊 [LOG] Fetching premium users count.");
        return delegate.getPremiumUsers();
    }

    @Override
    public long getActiveUsers() {
        System.out.println("📊 [LOG] Fetching active users count.");
        return delegate.getActiveUsers();
    }

    @Override
    public long getNewUsersLast7Days() {
        System.out.println("📅 [LOG] Fetching new users in last 7 days.");
        return delegate.getNewUsersLast7Days();
    }

    @Override
    public double getAverageBooksPerUser() {
        System.out.println("📚 [LOG] Fetching average books per user.");
        return delegate.getAverageBooksPerUser();
    }
}
