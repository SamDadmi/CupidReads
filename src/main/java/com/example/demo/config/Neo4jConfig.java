package com.example.demo.config;

import org.neo4j.driver.Driver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.core.transaction.Neo4jTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class Neo4jConfig {

    @Bean
    public Neo4jTransactionManager transactionManager(Driver driver) {
        return new Neo4jTransactionManager(driver);
    }

    @Bean
    public boolean initializeBookProperties(Driver driver) {
        try (var session = driver.session()) {
            // Add status property with default value 'Wishlist'
            session.run("MATCH (b:Book) WHERE b.status IS NULL SET b.status = 'Wishlist'");
            
            // Add rating property with default value 0
            session.run("MATCH (b:Book) WHERE b.rating IS NULL SET b.rating = 0");
            
            // Add notes property with default empty string
            session.run("MATCH (b:Book) WHERE b.notes IS NULL SET b.notes = ''");
            return true;
        }
    }
} 