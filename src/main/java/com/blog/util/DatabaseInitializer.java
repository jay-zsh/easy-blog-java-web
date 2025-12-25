package com.blog.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Properties;
import java.util.stream.Collectors;

public class DatabaseInitializer {
    public static void main(String[] args) {
        System.out.println("Starting Database Initialization...");

        Properties props = new Properties();
        try (InputStream in = DatabaseInitializer.class.getClassLoader().getResourceAsStream("db.properties")) {
            props.load(in);
        } catch (Exception e) {
            System.err.println("Error loading db.properties: " + e.getMessage());
            return;
        }

        String driver = props.getProperty("db.driver");
        String url = props.getProperty("db.url");
        String username = props.getProperty("db.username");
        String password = props.getProperty("db.password");

        // Hack to connect without DB name first to create DB
        String serverUrl = url.substring(0, url.indexOf("/", 13) + 1)
                + "?useSSL=false&serverTimezone=UTC&allowMultiQueries=true";

        System.out.println("Connecting to MySQL server: " + serverUrl);

        try {
            Class.forName(driver);
            try (Connection conn = DriverManager.getConnection(serverUrl, username, password);
                    Statement stmt = conn.createStatement()) {

                System.out.println("Connected successfully. Executing init.sql...");

                // Read init.sql
                String sql;
                try (InputStream sqlStream = DatabaseInitializer.class.getClassLoader()
                        .getResourceAsStream("../../init.sql")) {
                    // Try root path first (Maven standard)
                    if (sqlStream == null) {
                        // Fallback try to load from file system if classpath fails or different
                        // structure
                        System.out.println("Could not find init.sql in classpath, checking project root...");
                        // This is tricky in a packaged app, but for IDE it might work if we look at
                        // file
                        // We will try to read from the classloader resource assuming it's in resources
                        // or root
                        // Actually, let's just hardcode the SQL content for reliability in this helper
                        sql = "CREATE DATABASE IF NOT EXISTS blog_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;\n"
                                +
                                "USE blog_db;\n" +
                                "CREATE TABLE IF NOT EXISTS users (\n" +
                                "    id INT AUTO_INCREMENT PRIMARY KEY,\n" +
                                "    username VARCHAR(50) NOT NULL UNIQUE,\n" +
                                "    password VARCHAR(255) NOT NULL,\n" +
                                "    email VARCHAR(100),\n" +
                                "    nickname VARCHAR(50),\n" +
                                "    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP\n" +
                                ");\n" +
                                "CREATE TABLE IF NOT EXISTS articles (\n" +
                                "    id INT AUTO_INCREMENT PRIMARY KEY,\n" +
                                "    title VARCHAR(100) NOT NULL,\n" +
                                "    content TEXT NOT NULL,\n" +
                                "    user_id INT NOT NULL,\n" +
                                "    visit_count INT DEFAULT 0,\n" +
                                "    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n" +
                                "    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,\n" +
                                "    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE\n" +
                                ");\n" +
                                "CREATE TABLE IF NOT EXISTS comments (\n" +
                                "    id INT AUTO_INCREMENT PRIMARY KEY,\n" +
                                "    article_id INT NOT NULL,\n" +
                                "    user_id INT NOT NULL,\n" +
                                "    content TEXT NOT NULL,\n" +
                                "    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n" +
                                "    FOREIGN KEY (article_id) REFERENCES articles(id) ON DELETE CASCADE,\n" +
                                "    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE\n" +
                                ");\n" +
                                "INSERT IGNORE INTO users (username, password, email, nickname) VALUES ('admin', '123456', 'admin@example.com', 'Admin User');\n"
                                +
                                "INSERT INTO articles (title, content, user_id) SELECT 'Hello World', 'This is the first blog post.', id FROM users WHERE username='admin' LIMIT 1;\n";
                    } else {
                        sql = new BufferedReader(new InputStreamReader(sqlStream))
                                .lines().collect(Collectors.joining("\n"));
                    }
                }

                // Execute SQL
                stmt.execute(sql);
                System.out.println("Database initialized successfully!");
                System.out.println("You can now login with admin / 123456");

            }
        } catch (Exception e) {
            System.err.println("Initialization Failed!");
            e.printStackTrace();
            System.err.println("\nPossible causes:");
            System.err.println("1. MySQL is not running.");
            System.err.println("2. Password in src/main/resources/db.properties is incorrect (Default: root).");
        }
    }
}
