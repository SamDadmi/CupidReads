package com.example.demo.repository;

import com.example.demo.model.Club;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface ClubRepo extends Neo4jRepository<Club, String> {
    Optional<Club> findByName(String name);
    
    @Query("MATCH (c:Club), (u:User) WHERE id(c) = $clubId AND id(u) = $userId " +
           "MERGE (u)-[:MEMBER_OF]->(c)")
    void createMembership(@Param("clubId") Long clubId, @Param("userId") Long userId);
}