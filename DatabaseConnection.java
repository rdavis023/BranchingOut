package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

	private static final String URL = "jdbc:mysql://localhost:3306/BranchingOut";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "password";
    
    // Static block to load the MySQL JDBC driver during class initialization
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Error loading MySQL JDBC driver");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static Connection getConnection() {    	
        try {
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException("Error connecting to the database.", e);
        }
    }
}