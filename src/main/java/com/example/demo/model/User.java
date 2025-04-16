package com.example.demo.model;
import java.util.*;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.annotation.Version;


@Node("User")
public class User {
    @Id
    private String user_id;
    private String password;
    private String username;
    private boolean isPremium;
    private String email;
    private String favoriteGenres;
    private String favoriteLanguages;
    private String bio;
    private String profilePicture;
    @Version
    private Long version;
    private String role = "USER"; 



    @Relationship(type = "LIKES", direction = Relationship.Direction.OUTGOING)
    private Set<Book> wishlist = new HashSet<>();

    @Relationship(type = "MEMBER_OF", direction = Relationship.Direction.OUTGOING)
    private Set<Club> clubs = new HashSet<>();

    // Getters and Setters
    public String getUser_id() { return user_id; }
    public void setUser_id(String user_id) { this.user_id = user_id; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public boolean getIsPremium() { return isPremium; }
    public void setIsPremium(boolean isPremium) { this.isPremium = isPremium; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Set<Book> getWishlist() { return wishlist; }
    public void setWishlist(Set<Book> wishlist) { this.wishlist = wishlist; }

    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public Set<Club> getClubs() { return clubs; }
    public void setClubs(Set<Club> clubs) { this.clubs = clubs; }

    public String getFavoriteGenres() { return favoriteGenres; }
    public void setFavoriteGenres(String favoriteGenres) { this.favoriteGenres = favoriteGenres; }

    public String getFavoriteLanguages() { return favoriteLanguages; }
    public void setFavoriteLanguages(String favoriteLanguages) { this.favoriteLanguages = favoriteLanguages; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public String getProfilePicture() { return profilePicture; }
    public void setProfilePicture(String profilePicture) { this.profilePicture = profilePicture; }
}
