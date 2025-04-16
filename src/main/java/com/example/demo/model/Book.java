package com.example.demo.model;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.annotation.Version;


@Node("Book")
public class Book {
    @Id
    private String book_id;
    private String title;
    private String author;
    private String genre;
    private String image;
    private String synopsis;
    private String status = "Wishlist"; // Default status
    private int rating = 0; // Default rating
    private String notes = ""; // User's notes about the book
    @Version
    private Long version;
    private String pdfUrl;  // URL or path to the PDF


    // Getters and Setters
    public String getBook_id() { return book_id; }
    public void setBook_id(String book_id) { this.book_id = book_id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public String getSynopsis() { return synopsis; }
    public void setSynopsis(String synopsis) { this.synopsis = synopsis; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }

    public String getPdfUrl(){ return pdfUrl; }
    public void setPdfUrl(String pdfUrl) { this.pdfUrl = pdfUrl; }
}
