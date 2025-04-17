package com.example.demo.repository;

import com.example.demo.model.ClubChat;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ClubChatRepo extends Neo4jRepository<ClubChat, Long> {
    
    @Query("MATCH (c:ClubChat)-[:BELONGS_TO]->(club:Club) " +
           "WHERE club.name = $clubName " +
           "OPTIONAL MATCH (c)-[:SENT_BY]->(user:User) " +
           "WITH c, user, club " +
           "RETURN c { .*, user: user { .* }, club: club { .* } } as chat " +
           "ORDER BY c.timestamp ASC")
    List<ClubChat> findByClubNameOrderByTimestampAsc(@Param("clubName") String clubName);

    @Query("MATCH (c:ClubChat)-[:BELONGS_TO]->(club:Club) " +
           "WHERE club.name = $clubName AND c.timestamp > datetime($timestamp) " +
           "OPTIONAL MATCH (c)-[:SENT_BY]->(user:User) " +
           "WITH c, user, club " +
           "RETURN c { .*, user: user { .* }, club: club { .* } } as chat " +
           "ORDER BY c.timestamp ASC")
    List<ClubChat> findByClubAndTimestampAfterOrderByTimestampAsc(
        @Param("clubName") String clubName,
        @Param("timestamp") LocalDateTime timestamp);
}