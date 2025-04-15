package com.example.demo.repository;

import com.example.demo.model.Book;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface BookRepo extends Neo4jRepository<Book, String> {
}
