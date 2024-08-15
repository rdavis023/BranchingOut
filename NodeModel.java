package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import database.DatabaseConnection;

public class NodeModel {
    private int id;
    private Integer pid; // Using Integer for nullable parent id
    private String fullName;

    public NodeModel(int id, Integer pid, String fullName) {
        this.id = id;
        this.pid = pid;
        this.fullName = fullName;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    // Method to save a new node to the database
    public void save() {
        String sql = "INSERT INTO Nodes (id, pid, fullName) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            statement.setObject(2, pid);
            statement.setString(3, fullName);

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error saving node to the database.", e);
        }
    }

    // Method to update an existing node in the database
    public void update() {
        String sql = "UPDATE Nodes SET pid = ?, fullName = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setObject(1, pid);
            statement.setString(2, fullName);
            statement.setInt(3, id);

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating node in the database.", e);
        }
    }

    // Method to delete a node from the database
    public void delete() {
        String sql = "DELETE FROM Nodes WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting node from the database.", e);
        }
    }
}
