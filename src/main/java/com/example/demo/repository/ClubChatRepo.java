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
           "ORDER BY c.timestamp ASC " +
           "RETURN { " +
           "  id: id(c), " +
           "  message: c.message, " +
           "  timestamp: c.timestamp, " +
           "  user: CASE WHEN user IS NULL THEN NULL ELSE { " +
           "    username: user.username, " +
           "    id: id(user) " +
           "  } END, " +
           "  club: { " +
           "    name: club.name, " +
           "    id: id(club) " +
           "  } " +
           "} as chat")
    List<ClubChat> findByClubNameOrderByTimestampAsc(@Param("clubName") String clubName);

    @Query("MATCH (c:ClubChat)-[:BELONGS_TO]->(club:Club) " +
           "WHERE club.name = $clubName AND c.timestamp > datetime($timestamp) " +
           "OPTIONAL MATCH (c)-[:SENT_BY]->(user:User) " +
           "WITH c, user, club " +
           "ORDER BY c.timestamp ASC " +
           "RETURN { " +
           "  id: id(c), " +
           "  message: c.message, " +
           "  timestamp: c.timestamp, " +
           "  user: CASE WHEN user IS NULL THEN NULL ELSE { " +
           "    username: user.username, " +
           "    id: id(user) " +
           "  } END, " +
           "  club: { " +
           "    name: club.name, " +
           "    id: id(club) " +
           "  } " +
           "} as chat")
    List<ClubChat> findByClubAndTimestampAfterOrderByTimestampAsc(
        @Param("clubName") String clubName,
        @Param("timestamp") LocalDateTime timestamp);
}