package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PostgreSQLConnection {
    // Database connection properties
    private static final String URL = "jdbc:postgresql://localhost:5432/note";
    private static final String USER = "ditya";
    private static final String PASSWORD = ".";
    
    public static void main(String[] args) {
        try {
            // Load the PostgreSQL JDBC driver
            Class.forName("org.postgresql.Driver");
            
            // Establish a connection
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            
            if (connection != null) {
                System.out.println("Connected to PostgreSQL successfully!");
                
                // Example: Create a table
                createTable(connection);
                
                // Example: Insert data
                insertData(connection);
                
                // Example: Query data
                queryData(connection);
                
                // Close the connection
                connection.close();
                System.out.println("Connection closed.");
            } else {
                System.out.println("Failed to connect to PostgreSQL.");
            }
        } catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL JDBC driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Database connection error.");
            e.printStackTrace();
        }
    }
    
    private static void createTable(Connection connection) throws SQLException {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS employees (" +
                "id SERIAL PRIMARY KEY, " +
                "name VARCHAR(100) NOT NULL, " +
                "position VARCHAR(100), " +
                "hire_date DATE)";
        
        try (Statement statement = connection.createStatement()) {
            statement.execute(createTableSQL);
            System.out.println("Table 'employees' created or already exists.");
        }
    }
    
    private static void insertData(Connection connection) throws SQLException {
        String insertSQL = "INSERT INTO employees (name, position, hire_date) " +
                "VALUES ('John Doe', 'Software Engineer', '2023-01-15')";
        
        try (Statement statement = connection.createStatement()) {
            int rowsAffected = statement.executeUpdate(insertSQL);
            System.out.println(rowsAffected + " row(s) inserted.");
        }
    }
    
    private static void queryData(Connection connection) throws SQLException {
        String selectSQL = "SELECT * FROM employees";
        
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(selectSQL)) {
            
            System.out.println("\nEmployees:");
            System.out.println("--------------------------------------------------");
            
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String position = resultSet.getString("position");
                String hireDate = resultSet.getString("hire_date");
                
                System.out.printf("ID: %d, Name: %s, Position: %s, Hire Date: %s%n", 
                                  id, name, position, hireDate);
            }
        }
    }
}
