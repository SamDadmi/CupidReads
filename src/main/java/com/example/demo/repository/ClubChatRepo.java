package com.example.demo.repository;

import com.example.demo.model.ClubChat;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ClubChatRepo extends Neo4jRepository<ClubChat, Long> {
    
    @Query("MATCH (c:ClubChat)-[:BELONGS_TO]->(club:Club) " +
           "MATCH (user:User)-[:POSTED]->(c) " +
           "WHERE club.name = $clubName " +
           "RETURN c, user, club " +
           "ORDER BY c.timestamp ASC")
    List<ClubChat> findByClubNameOrderByTimestampAsc(@Param("clubName") String clubName);

    // Spring Data Neo4j can also derive queries from method names for simple cases,
    // but using @Query provides more control for complex relationships.
    // Example of derived query (might work depending on complexity):
    // List<ClubChat> findByClub_NameOrderByTimestampAsc(String clubName);
}