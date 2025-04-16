package com.example.demo.model;
import com.example.demo.model.User;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

@Node
public class Annotation {
    @Id
    @GeneratedValue
    private Long id;
    
    private String content;     // The annotation text
    private String pdfName;     // Which PDF this belongs to
    private int pageNumber;     // Page number in the PDF
    private float xPosition;    // X coordinate on the page
    private float yPosition;    // Y coordinate on the page
    private String annotationType;  // Type: highlight, text, drawing
    private String additionalData;  // JSON string with additional annotation data
    
    @Relationship(type = "CREATED_BY", direction = Relationship.Direction.OUTGOING)
    private User user;
    
    // Getters and setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public String getPdfName() {
        return pdfName;
    }
    
    public void setPdfName(String pdfName) {
        this.pdfName = pdfName;
    }
    
    public int getPageNumber() {
        return pageNumber;
    }
    
    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }
    
    public float getXPosition() {
        return xPosition;
    }
    
    public void setXPosition(float xPosition) {
        this.xPosition = xPosition;
    }
    
    public float getYPosition() {
        return yPosition;
    }
    
    public void setYPosition(float yPosition) {
        this.yPosition = yPosition;
    }
    
    public String getAnnotationType() {
        return annotationType;
    }
    
    public void setAnnotationType(String annotationType) {
        this.annotationType = annotationType;
    }
    
    public String getAdditionalData() {
        return additionalData;
    }
    
    public void setAdditionalData(String additionalData) {
        this.additionalData = additionalData;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
}