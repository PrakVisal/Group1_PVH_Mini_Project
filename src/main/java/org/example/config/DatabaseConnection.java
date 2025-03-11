package org.example.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
          final String URL = "jdbc:postgresql://localhost:5432/stock_management";
          final String USER = "postgres";
          final String PASSWORD = "password";
          public Connection getConnection() throws SQLException {
              return DriverManager.getConnection(URL, USER, PASSWORD);
          }
}
