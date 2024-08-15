package controllers;

import java.io.File;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

@SuppressWarnings("serial")
@WebServlet("/TreeServlet")
@MultipartConfig // Annotation to enable file uploads
public class TreeController extends HttpServlet {
    private String uploadDir;

    @Override
    public void init() throws ServletException {
        // Set the path to upload directory within the web root
        uploadDir = getServletContext().getRealPath("/photos");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
        	Part filePart = request.getPart("file");
        
	        String fileName = getFileName(filePart);
	
	        File uploadPath = new File(uploadDir, fileName);
	        filePart.write(uploadPath.getAbsolutePath());
	
	        response.getWriter().println("File uploaded successfully: " + fileName);
        }
        catch (Exception e) {
            e.printStackTrace(); // Log the exception for debugging purposes
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error");
        }
    }

    private String getFileName(Part part) {
        String contentDisposition = part.getHeader("content-disposition");
        String[] tokens = contentDisposition.split(";");
        for (String token : tokens) {
            if (token.trim().startsWith("filename")) {
                return token.substring(token.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return "";
    }
}
