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
            // Render provides DATABASE_URL in format: postgresql://user:password@host:port/dbname
            String databaseUrl = System.getenv("DATABASE_URL");
            String dbUrl = System.getenv("DB_URL");
            String dbUser = System.getenv("DB_USER");
            String dbPassword = System.getenv("DB_PASSWORD");
            String dbHost = System.getenv("DB_HOST");
            String dbPort = System.getenv("DB_PORT");
            String dbName = System.getenv("DB_NAME");
            
            // Parse DATABASE_URL (Render format: postgresql://user:password@host:port/dbname)
            if (databaseUrl != null && !databaseUrl.isEmpty()) {
                try {
                    // Remove postgresql:// prefix
                    String url = databaseUrl.replaceFirst("^postgresql://", "");
                    
                    // Extract user:password@host:port/dbname
                    int atIndex = url.indexOf('@');
                    if (atIndex > 0) {
                        String userPass = url.substring(0, atIndex);
                        String hostPortDb = url.substring(atIndex + 1);
                        
                        // Extract user and password
                        int colonIndex = userPass.indexOf(':');
                        if (colonIndex > 0) {
                            dbUser = userPass.substring(0, colonIndex);
                            dbPassword = userPass.substring(colonIndex + 1);
                        } else {
                            dbUser = userPass;
                        }
                        
                        // Extract host, port, and database
                        int slashIndex = hostPortDb.indexOf('/');
                        if (slashIndex > 0) {
                            dbName = hostPortDb.substring(slashIndex + 1);
                            String hostPort = hostPortDb.substring(0, slashIndex);
                            
                            int portColonIndex = hostPort.indexOf(':');
                            if (portColonIndex > 0) {
                                dbHost = hostPort.substring(0, portColonIndex);
                                dbPort = hostPort.substring(portColonIndex + 1);
                            } else {
                                dbHost = hostPort;
                                dbPort = "5432";
                            }
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Error parsing DATABASE_URL: " + e.getMessage());
                }
            }
            
            // Build JDBC URL from environment variables if provided
            if (dbUrl != null && !dbUrl.isEmpty()) {
                // If already in JDBC format, use as is
                if (dbUrl.startsWith("jdbc:")) {
                    props.put("jakarta.persistence.jdbc.url", dbUrl);
                } else {
                    // Convert PostgreSQL format to JDBC format
                    props.put("jakarta.persistence.jdbc.url", 
                        dbUrl.replaceFirst("^postgresql://", "jdbc:postgresql://"));
                }
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
            System.err.println("Failed to initialize EntityManagerFactory: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize EntityManagerFactory", e);
        }
    }

    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public static void closeEntityManagerFactory() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}
