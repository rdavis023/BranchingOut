package models;

public class EventResponse {
    private boolean success;
    private String message;

    // Default constructor (required by Gson)
    public EventResponse() {
    }

    // Constructor with parameters
    public EventResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    // Getters and setters for fields
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
