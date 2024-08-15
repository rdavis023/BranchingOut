package models;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import database.DatabaseConnection;

public class FamilyMember {
    private int memberID;
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private String relationship;
    private String gender;
    private String profilePicture;
    private User user;
    private Family family;

    // Constructors, other fields, and methods...

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Family getFamily() {
        return family;
    }

    public void setFamily(Family family) {
        this.family = family;
    }
    
    public void save() {
        String sql = "INSERT INTO FamilyMember (FirstName, LastName, DateOfBirth, Relationship, Gender, ProfilePicture, UserID) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setDate(3, dateOfBirth);
            statement.setString(4, relationship);
            statement.setString(5, gender);
            statement.setString(6, profilePicture);
            statement.setInt(7, user.getUserID());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception as needed
        }
    }
}
