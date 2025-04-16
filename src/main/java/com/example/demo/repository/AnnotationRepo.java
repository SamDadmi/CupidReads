package com.example.demo.repository;

import com.example.demo.model.Annotation;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnnotationRepo extends Neo4jRepository<Annotation, Long> {
    // List<Annotation> findByUsernameAndPdfName(String username, String pdfName);
    void deleteByUser_UsernameAndPdfNameAndPageNumber(String username, String pdfName, int pageNumber);

    List<Annotation> findByUser_UsernameAndPdfName(String username, String pdfName);

}