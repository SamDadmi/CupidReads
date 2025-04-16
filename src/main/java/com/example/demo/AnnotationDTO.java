package com.example.demo.dto;

public class AnnotationDTO {
    private Long id;
    private String content;
    private int pageNumber;
    private float xPosition;
    private float yPosition;
    private String annotationType;
    private String additionalData;

    // Constructors
    public AnnotationDTO() {}

    public AnnotationDTO(Long id, String content, int pageNumber, float xPosition, float yPosition, String annotationType, String additionalData) {
        this.id = id;
        this.content = content;
        this.pageNumber = pageNumber;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.annotationType = annotationType;
        this.additionalData = additionalData;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public int getPageNumber() { return pageNumber; }
    public void setPageNumber(int pageNumber) { this.pageNumber = pageNumber; }

    public float getXPosition() { return xPosition; }
    public void setXPosition(float xPosition) { this.xPosition = xPosition; }

    public float getYPosition() { return yPosition; }
    public void setYPosition(float yPosition) { this.yPosition = yPosition; }

    public String getAnnotationType() { return annotationType; }
    public void setAnnotationType(String annotationType) { this.annotationType = annotationType; }

    public String getAdditionalData() { return additionalData; }
    public void setAdditionalData(String additionalData) { this.additionalData = additionalData; }
}
