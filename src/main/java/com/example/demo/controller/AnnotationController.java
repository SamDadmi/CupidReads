package com.example.demo.controller;

import com.example.demo.model.Annotation;
import com.example.demo.dto.AnnotationDTO;

import org.springframework.stereotype.Controller;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepo;
import com.example.demo.service.AnnotationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/api/annotations")
public class AnnotationController {
    @Autowired
    private  AnnotationService annotationService;
    @Autowired
    private UserRepo userRepository;
    
    // @Autowired
    // public AnnotationController(AnnotationService annotationService, UserRepo userRepository) {
    //     this.annotationService = annotationService;
    //     this.userRepository = userRepository;
    // }
    
    // @GetMapping("/pdf/{pdfName}")
    // public ResponseEntity<List<Annotation>> getUserAnnotationsForPdf(
    //         @PathVariable String pdfName,
    //         Authentication authentication) {
        
    //     if (authentication == null) {
    //         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    //     }
        
    //     // Option 1: Using username
    //     String username = authentication.getName();
    //     List<Annotation> annotations = annotationService.getUserAnnotationsForPdf(username, pdfName);
    //     return ResponseEntity.ok(annotations);
    // }

    @GetMapping("/pdf/{pdfName}")
    public ResponseEntity<List<AnnotationDTO>> getUserAnnotationsForPdf(
        @PathVariable String pdfName,
        Authentication authentication) {

    if (authentication == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    String username = authentication.getName();
    List<Annotation> annotations = annotationService.getUserAnnotationsForPdf(username, pdfName);

    // Convert to DTOs
    List<AnnotationDTO> annotationDTOs = annotations.stream().map(annotation ->
            new AnnotationDTO(
                    annotation.getId(),
                    annotation.getContent(),
                    annotation.getPageNumber(),
                    annotation.getXPosition(),
                    annotation.getYPosition(),
                    annotation.getAnnotationType(),
                    annotation.getAdditionalData()
            )
    ).toList();

    return ResponseEntity.ok(annotationDTOs);
}

    /**
     * Get all annotations for the current user and specified PDF
     */
    // @GetMapping("/pdf/{pdfName}")
    // public ResponseEntity<List<Annotation>> getUserAnnotationsForPdf(@PathVariable String pdfName) {
    //     User currentUser = getCurrentUser();
    //     if (currentUser == null) {
    //         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    //     }
        
    //     List<Annotation> annotations = annotationService.getUserAnnotationsForPdf(currentUser.getUsername(), pdfName);
    //     return ResponseEntity.ok(annotations);
    // }
    
    /**
     * Save a new annotation
     */
    // @PostMapping("/pdf/{pdfName}")
    // public ResponseEntity<Annotation> addAnnotation(
    //         @PathVariable String pdfName,
    //         @RequestBody Map<String, Object> request) {
        
    //     User currentUser = getCurrentUser();
    //     if (currentUser == null) {
    //         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    //     }
        
    //     try {
    //         String content = (String) request.get("content");
    //         Integer pageNumber = (Integer) request.get("pageNumber");
    //         Float xPosition = ((Number) request.get("xPosition")).floatValue();
    //         Float yPosition = ((Number) request.get("yPosition")).floatValue();
    //         String annotationType = (String) request.get("annotationType");
    //         ObjectMapper mapper = new ObjectMapper();
    //         String additionalData = mapper.writeValueAsString(request.get("additionalData"));            
    //         Annotation annotation = annotationService.saveAnnotation(
    //             content, pdfName, pageNumber, xPosition, yPosition, annotationType, additionalData, currentUser
    //         );
            
    //         return ResponseEntity.ok(annotation);
    //     } catch (Exception e) {
    //         return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    //     }
    // }

//     @PostMapping("/pdf/{pdfName}")
// public ResponseEntity<Annotation> addAnnotation(
//         @PathVariable String pdfName,
//         @RequestBody Map<String, Object> request) {

//     User currentUser = getCurrentUser();
//     if (currentUser == null) {
//         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//     }

//     try {
//         String content = (String) request.get("content");
//         Integer pageNumber = (Integer) request.get("pageNumber");
//         Float xPosition = ((Number) request.get("xPosition")).floatValue();
//         Float yPosition = ((Number) request.get("yPosition")).floatValue();
//         String annotationType = (String) request.get("annotationType");

//         ObjectMapper mapper = new ObjectMapper();
//         String additionalData = mapper.writeValueAsString(request.get("additionalData"));

//         Annotation annotation = annotationService.saveAnnotation(
//             content, pdfName, pageNumber, xPosition, yPosition, annotationType, additionalData, currentUser
//         );

//         return ResponseEntity.ok(annotation);
//     } catch (Exception e) {
//         e.printStackTrace(); // Optional: log for debugging
//         return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//     }
// }

    @PostMapping("/pdf/{pdfName}")
public ResponseEntity<AnnotationDTO> addAnnotation(
        @PathVariable String pdfName,
        @RequestBody Map<String, Object> request) {

    User currentUser = getCurrentUser();
    if (currentUser == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    try {
        String content = (String) request.get("content");
        Integer pageNumber = (Integer) request.get("pageNumber");
        Float xPosition = ((Number) request.get("xPosition")).floatValue();
        Float yPosition = ((Number) request.get("yPosition")).floatValue();
        String annotationType = (String) request.get("annotationType");
        String additionalData = (String) request.get("additionalData");

        Annotation annotation = annotationService.saveAnnotation(
                content, pdfName, pageNumber, xPosition, yPosition, annotationType, additionalData, currentUser
        );

        AnnotationDTO dto = new AnnotationDTO(
                annotation.getId(),
                annotation.getContent(),
                annotation.getPageNumber(),
                annotation.getXPosition(),
                annotation.getYPosition(),
                annotation.getAnnotationType(),
                annotation.getAdditionalData()
        );

        return ResponseEntity.ok(dto);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}

    /**
     * Delete an annotation
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAnnotation(@PathVariable Long id) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        Optional<Annotation> annotation = annotationService.getAnnotationById(id);
        if (annotation.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        // Security check: ensure the user owns this annotation
        if (!annotation.get().getUser().getUsername().equals(currentUser.getUsername())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        annotationService.deleteAnnotation(id);
        return ResponseEntity.ok(Map.of("success", true));
    }
    
    /**
     * Helper method to get the current authenticated user
     */
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        
        String username = authentication.getName();
        return userRepository.findByUsername(username).orElse(null);
    }
}