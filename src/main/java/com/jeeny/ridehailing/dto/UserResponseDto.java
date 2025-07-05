package com.jeeny.ridehailing.dto;

import com.jeeny.ridehailing.entity.UserType;
import com.jeeny.ridehailing.entity.AvailabilityStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

/**
 * DTO for user response
 */
@Schema(description = "User information response")
public class UserResponseDto {
    
    @Schema(description = "User ID", example = "1")
    private Long id;
    
    @Schema(description = "User's full name", example = "John Doe")
    private String name;
    
    @Schema(description = "User's email address", example = "john.doe@example.com")
    private String email;
    
    @Schema(description = "User's phone number", example = "+1234567890")
    private String phoneNumber;
    
    @Schema(description = "Type of user", example = "PASSENGER")
    private UserType userType;
    
    @Schema(description = "Current availability status", example = "AVAILABLE")
    private AvailabilityStatus availabilityStatus;
    
    @Schema(description = "Account creation timestamp")
    private LocalDateTime createdAt;
    
    // Constructors
    public UserResponseDto() {}
    
    public UserResponseDto(Long id, String name, String email, String phoneNumber, UserType userType, 
                          AvailabilityStatus availabilityStatus, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.userType = userType;
        this.availabilityStatus = availabilityStatus;
        this.createdAt = createdAt;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public UserType getUserType() {
        return userType;
    }
    
    public void setUserType(UserType userType) {
        this.userType = userType;
    }
    
    public AvailabilityStatus getAvailabilityStatus() {
        return availabilityStatus;
    }
    
    public void setAvailabilityStatus(AvailabilityStatus availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    @Override
    public String toString() {
        return "UserResponseDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", userType=" + userType +
                ", availabilityStatus=" + availabilityStatus +
                ", createdAt=" + createdAt +
                '}';
    }
}
