package com.example.demo.repository;

import com.example.demo.model.Club;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import java.util.Optional;

public interface ClubRepo extends Neo4jRepository<Club, String> {
    Optional<Club> findByName(String name);
}