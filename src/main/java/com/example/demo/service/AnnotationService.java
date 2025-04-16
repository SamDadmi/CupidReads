package com.example.demo.service;

import com.example.demo.model.Annotation;
import com.example.demo.model.User;
import com.example.demo.repository.AnnotationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AnnotationService {
    @Autowired

    private AnnotationRepo annotationRepository;
    
    // @Autowired
    // public AnnotationService(AnnotationRepo annotationRepository) {
    //     this.annotationRepository = annotationRepository;
    // }
    
    public Annotation saveAnnotation(
            String content, 
            String pdfName, 
            int pageNumber, 
            float xPosition, 
            float yPosition,
            String annotationType,
            String additionalData,
            User user) {
        
        Annotation annotation = new Annotation();
        annotation.setContent(content);
        annotation.setPdfName(pdfName);
        annotation.setPageNumber(pageNumber);
        annotation.setXPosition(xPosition);
        annotation.setYPosition(yPosition);
        annotation.setAnnotationType(annotationType);
        annotation.setAdditionalData(additionalData);
        annotation.setUser(user);
        
        return annotationRepository.save(annotation);
    }
    
    // public List<Annotation> getUserAnnotationsForPdf(String username, String pdfName) {
    //     return annotationRepository.findByUsernameAndPdfName(username, pdfName);
    // }

    public List<Annotation> getUserAnnotationsForPdf(String username, String pdfName) {
        return annotationRepository.findByUser_UsernameAndPdfName(username, pdfName);
    }
    
    public Optional<Annotation> getAnnotationById(Long id) {
        return annotationRepository.findById(id);
    }
    
    public void deleteAnnotation(Long id) {
        annotationRepository.deleteById(id);
    }
    
    @Transactional
    public void clearPageAnnotations(String username, String pdfName, int pageNumber) {
        annotationRepository.deleteByUser_UsernameAndPdfNameAndPageNumber(username, pdfName, pageNumber);
    }
}