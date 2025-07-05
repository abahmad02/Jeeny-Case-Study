package com.jeeny.ridehailing.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Standard error response structure
 */
@Schema(description = "Error response structure")
public class ErrorResponse {
    
    @Schema(description = "HTTP status code")
    private int status;
    
    @Schema(description = "Error type")
    private String error;
    
    @Schema(description = "Error message")
    private String message;
    
    @Schema(description = "Request path")
    private String path;
    
    @Schema(description = "Timestamp when error occurred")
    private LocalDateTime timestamp;
    
    @Schema(description = "Validation errors for fields (if applicable)")
    private Map<String, String> validationErrors;
    
    // Constructors
    public ErrorResponse() {}
    
    public ErrorResponse(int status, String error, String message, String path, LocalDateTime timestamp) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
        this.timestamp = timestamp;
    }
    
    // Getters and Setters
    public int getStatus() {
        return status;
    }
    
    public void setStatus(int status) {
        this.status = status;
    }
    
    public String getError() {
        return error;
    }
    
    public void setError(String error) {
        this.error = error;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public String getPath() {
        return path;
    }
    
    public void setPath(String path) {
        this.path = path;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    
    public Map<String, String> getValidationErrors() {
        return validationErrors;
    }
    
    public void setValidationErrors(Map<String, String> validationErrors) {
        this.validationErrors = validationErrors;
    }
}
