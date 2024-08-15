package controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.Event;
import models.EventResponse;
import models.User;
import database.DatabaseConnection;

@WebServlet("/EventUpdate")
public class EventsController extends HttpServlet {
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {
	    // Get the session from the request
	    HttpSession session = request.getSession(false); // Don't create a new session if it doesn't exist

	    if (session != null) {
	        int userIdFromSession = User.getUserIdFromSession(session);

	        // Get the event data from the request parameters
	        String eventName = request.getParameter("evTitle");
	        String eventDate = request.getParameter("evDate");
	        String eventTime = request.getParameter("evTime");
	        /*
	        String eventLocation = request.getParameter("address");
	        String eventDescription = request.getParameter("description");
	        */

	        // Insert the event data into the database
	        boolean success = insertEvent(eventName, eventDate, eventTime, userIdFromSession);

	        // Create response string
	        String responseString;
	        if (success) {
	            responseString = "Event submitted successfully!";
	        } else {
	            responseString = "Failed to submit event.";
	        }

	        // Set response content type and write response string
	        response.setContentType("text/plain");
	        response.setCharacterEncoding("UTF-8");
	        response.getWriter().write(responseString);
	    } else {
	        // Handle session not found
	        response.setContentType("text/plain");
	        response.setCharacterEncoding("UTF-8");
	        response.getWriter().write("Session not found.");
	    }
	}

    private boolean insertEvent(String eventName, String eventDate, String eventTime,
            int userIdFromSession) {
        String sql = "INSERT INTO Event (EventName, Date, Time, UserID) " + "VALUES (?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, eventName);
            statement.setString(2, eventDate);
            statement.setString(3, eventTime);
            statement.setInt(4, userIdFromSession); // Set UserID as the fourth parameter

            int rowsInserted = statement.executeUpdate();

            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Get the session from the request
        HttpSession session = request.getSession(false); // Don't create a new session if it doesn't exist

        if (session != null) {
            int userIdFromSession = User.getUserIdFromSession(session);

            // Fetch events from the database for the logged-in user
            List<Event> events = fetchEvents(userIdFromSession);

            // Manually construct JSON response
            StringBuilder jsonResponse = new StringBuilder();
            jsonResponse.append("[");
            for (Event event : events) {
                jsonResponse.append("{");
                jsonResponse.append("\"eventID\":").append(event.getEventID()).append(",");
                jsonResponse.append("\"eventName\":\"").append(event.getEventName()).append("\",");
                jsonResponse.append("\"date\":\"").append(event.getDate()).append("\",");
                jsonResponse.append("\"time\":\"").append(event.getTime()).append("\",");
                jsonResponse.append("\"location\":\"").append(event.getLocation()).append("\",");
                jsonResponse.append("\"description\":\"").append(event.getDescription()).append("\"");
                jsonResponse.append("},");
            }
            if (!events.isEmpty()) {
                jsonResponse.deleteCharAt(jsonResponse.length() - 1); // Remove the trailing comma
            }
            jsonResponse.append("]");

            // Set response content type and write JSON response
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(jsonResponse.toString());
        } else {
            // Handle session not found
            response.setContentType("text/plain");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("Session not found.");
        }
    }

    private List<Event> fetchEvents(int userId) {
        List<Event> events = new ArrayList<>();

        String sql = "SELECT * FROM Event WHERE UserID = ?";
        try (Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Event event = new Event();
                    event.setEventID(resultSet.getInt("EventID"));
                    event.setEventName(resultSet.getString("EventName"));
                    event.setDate(resultSet.getDate("Date"));
                    event.setTime(resultSet.getTime("Time"));
                    event.setLocation(resultSet.getString("Location"));
                    event.setDescription(resultSet.getString("Description"));
                    events.add(event);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return events;
    }
}