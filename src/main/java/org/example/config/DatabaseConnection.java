package org.example.config;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
          final String URL = "jdbc:postgresql://localhost:5432/stock";
          final String USER = "postgres";
          final String PASSWORD = "8899";
          private Connection connection;
          public Connection getConnection() throws SQLException {
              return DriverManager.getConnection(URL, USER, PASSWORD);
          }
          public int executeUpdateQuery(String query) {
        try (Statement statement = connection.createStatement()) {
            return statement.executeUpdate(query);
        } catch (SQLException e) {
            System.err.println("Error executing UPDATE/INSERT/DELETE query: " + e.getMessage());
            return -1;
        }
    }
}


