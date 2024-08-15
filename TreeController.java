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
import com.fasterxml.jackson.databind.ObjectMapper;
import database.DatabaseConnection;
import models.FamilyMember;
import models.NodeModel;
import models.User;

@SuppressWarnings("serial")
@WebServlet("/UpdateTree")
public class TreeController extends HttpServlet {
	private ObjectMapper objectMapper = new ObjectMapper();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action) {
            case "add":
                addNode(request, response);
                break;
            case "update":
                updateNode(request, response);
                break;
            case "remove":
                removeNode(request, response);
                break;
            default:
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Invalid action");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action) {
            case "read":
                readNodes(request, response);
                break;
            default:
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Invalid action");
        }
    }

    private void readNodes(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Retrieve nodes from the database
        List<NodeModel> nodes = fetchNodesFromDatabase();

        //String jsonNodes = objectMapper.writeValueAsString(nodes);
        response.setContentType("application/json");
        //response.getWriter().write(jsonNodes);
    }

    private List<NodeModel> fetchNodesFromDatabase() {
        List<NodeModel> nodes = new ArrayList<>();
        String sql = "SELECT id, pid, fullName FROM Nodes";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                Integer pid = resultSet.getInt("pid");
                String fullName = resultSet.getString("fullName");

                NodeModel node = new NodeModel(id, pid, fullName);
                nodes.add(node);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching nodes from the database.", e);
        }

        return nodes;
    }

    private void addNode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Extract parameters from request
        int userId = getUserIdFromSession(request);
        int nodeId = Integer.parseInt(request.getParameter("nodeId"));
        String nodeName = request.getParameter("nodeName");

        // Create NodeModel object and save to database
        NodeModel newNode = new NodeModel(nodeId, null, nodeName);
        newNode.save();

        // Update FamilyMember table with corresponding data
        FamilyMember familyMember = new FamilyMember();
        familyMember.setFirstName(nodeName);
        familyMember.setUser(User.getUserById(userId)); // Get user by ID
        familyMember.setFamily(null); // Set family as needed
        familyMember.save(); // Save family member to the database

        //String jsonResponse = objectMapper.writeValueAsString(newNode); // Use objectMapper for serialization
        response.setContentType("application/json");
        //response.getWriter().write(jsonResponse);
    }

    private void updateNode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Extract parameters from request
        int id = Integer.parseInt(request.getParameter("id"));
        Integer pid = Integer.parseInt(request.getParameter("pid"));
        String fullName = request.getParameter("fullName");

        // Create NodeModel object and update in database
        NodeModel updatedNode = new NodeModel(id, pid, fullName);
        updatedNode.update();

        //String jsonResponse = objectMapper.writeValueAsString(updatedNode); // Use objectMapper for serialization
        response.setContentType("application/json");
        //response.getWriter().write(jsonResponse);
    }

    private void removeNode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Extract parameter from request
        int id = Integer.parseInt(request.getParameter("id"));

        // Delete the node from the database
        deleteNodeFromDatabase(id);

        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write("Node removed");
    }

    private void deleteNodeFromDatabase(int id) {
        String sql = "DELETE FROM Nodes WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting node from the database.", e);
        }
    }
    
    private int getUserIdFromSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            Object userIdObj = session.getAttribute("userId");
            if (userIdObj != null) {
                return (int) userIdObj;
            }
        }
        return -1; // Invalid user ID
    }
}
