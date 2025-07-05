package com.jeeny.ridehailing.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO for authentication response
 */
@Schema(description = "Authentication response with JWT token")
public class AuthResponseDto {
    
    @Schema(description = "JWT token for authentication", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;
    
    @Schema(description = "Token type", example = "Bearer")
    private String type = "Bearer";
    
    @Schema(description = "User information")
    private UserResponseDto user;
    
    // Constructors
    public AuthResponseDto() {}
    
    public AuthResponseDto(String token, UserResponseDto user) {
        this.token = token;
        this.user = user;
    }
    
    // Getters and Setters
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public UserResponseDto getUser() {
        return user;
    }
    
    public void setUser(UserResponseDto user) {
        this.user = user;
    }
}
