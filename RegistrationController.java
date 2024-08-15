package controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import database.DatabaseConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.User;

@SuppressWarnings("serial")
@WebServlet("/Register")
public class RegistrationController extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {    	
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        
        boolean userExists = User.verifyUsername(username);
        boolean emailExists = User.verifyEmail(email);
        boolean registrationSuccess = false;
        
		if(!userExists == true && !emailExists == true) {
			registrationSuccess = registerUser(username, password, email);
		}
		
        if (registrationSuccess) {
            response.getWriter().println("Registration successful!");
        } else {
        	if (userExists) {
        		response.getWriter().println("Registration failed: Username already exists.");
        	}
        	else if (emailExists) {
            	response.getWriter().println("Registration failed: Email already exists.");
            }
            else { response.getWriter().println("Unexpected error"); }
        }
    }

    private boolean registerUser(String username, String password, String email) {
        boolean success = false;
        String insertSql = "INSERT INTO User (Username, Password, Email) VALUES (?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(insertSql)) {

            statement.setString(1, username);
            statement.setString(2, password);
            statement.setString(3, email);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                success = true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return success;
    }
}
