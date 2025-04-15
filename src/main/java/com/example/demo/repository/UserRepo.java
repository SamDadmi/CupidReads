package com.example.demo.repository;

import com.example.demo.model.User;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import java.util.Optional;

public interface UserRepo extends Neo4jRepository<User, String> {
    Optional<User> findByUsername(String username);
    Optional<User> findById(String userId);
    boolean existsByUsername(String username);
} 