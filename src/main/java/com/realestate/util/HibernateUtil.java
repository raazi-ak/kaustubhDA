package com.realestate.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

public class HibernateUtil {
    private static EntityManagerFactory emf;
    private static final String PERSISTENCE_UNIT_NAME = "realestatePU";

    static {
        try {
            Map<String, String> props = new HashMap<>();
            
            // Read database connection from environment variables (for Render/cloud deployment)
            String dbUrl = System.getenv("DB_URL");
            String dbUser = System.getenv("DB_USER");
            String dbPassword = System.getenv("DB_PASSWORD");
            String dbHost = System.getenv("DB_HOST");
            String dbPort = System.getenv("DB_PORT");
            String dbName = System.getenv("DB_NAME");
            
            // Build JDBC URL from environment variables if provided
            if (dbUrl != null && !dbUrl.isEmpty()) {
                props.put("jakarta.persistence.jdbc.url", dbUrl);
            } else if (dbHost != null && dbPort != null && dbName != null) {
                // Construct URL from individual components
                props.put("jakarta.persistence.jdbc.url", 
                    "jdbc:postgresql://" + dbHost + ":" + dbPort + "/" + dbName);
            }
            
            if (dbUser != null && !dbUser.isEmpty()) {
                props.put("jakarta.persistence.jdbc.user", dbUser);
            }
            
            if (dbPassword != null && !dbPassword.isEmpty()) {
                props.put("jakarta.persistence.jdbc.password", dbPassword);
            }
            
            // Create EntityManagerFactory with overridden properties
            if (props.isEmpty()) {
                emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
            } else {
                emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME, props);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize EntityManagerFactory", e);
        }
    }

    public static EntityManager getEntityManager() {
        return emf.getEntityManager();
    }

    public static void closeEntityManagerFactory() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}
