package com.example.demo.model;

import java.util.*;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.annotation.Version;


@Node
public class Club {
    // @Id @GeneratedValue
    // private String id;
    @Id
    private String name;
    private String description;
    private String clubType; // public, private, or restricted
    private Set<String> invitedEmails = new HashSet<>();
    
    @Relationship(type = "MEMBER_OF", direction = Relationship.Direction.INCOMING)
    private Set<User> members = new HashSet<>();

    @Relationship(type = "CREATED", direction = Relationship.Direction.INCOMING)
    private User createdBy;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getClubType() { return clubType; }
    public void setClubType(String clubType) { this.clubType = clubType; }

    public Set<String> getInvitedEmails() { return invitedEmails; }
    public void setInvitedEmails(Set<String> invitedEmails) { this.invitedEmails = invitedEmails; }

    public Set<User> getMembers() { return members; }
    public void setMembers(Set<User> members) { this.members = members; }

    public User getCreatedBy() { return createdBy; }
    public void setCreatedBy(User createdBy) { this.createdBy = createdBy; }

}
