package com.jeeny.ridehailing.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import com.jeeny.ridehailing.entity.UserType;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO for user registration request
 */
@Schema(description = "User registration request")
public class UserRegistrationDto {
    
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    @Schema(description = "User's full name", example = "John Doe", required = true)
    private String name;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Schema(description = "User's email address", example = "john.doe@example.com", required = true)
    private String email;
    
    @NotBlank(message = "Phone number is required")
    @Size(min = 10, max = 15, message = "Phone number must be between 10 and 15 characters")
    @Schema(description = "User's phone number", example = "+1234567890", required = true)
    private String phoneNumber;
    
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    @Schema(description = "User's password", example = "password123", required = true)
    private String password;
    
    @NotNull(message = "User type is required")
    @Schema(description = "Type of user account", example = "PASSENGER", required = true)
    private UserType userType;
    
    // Constructors
    public UserRegistrationDto() {}
    
    public UserRegistrationDto(String name, String email, String phoneNumber, String password, UserType userType) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.userType = userType;
    }
    
    // Getters and Setters
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
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public UserType getUserType() {
        return userType;
    }
    
    public void setUserType(UserType userType) {
        this.userType = userType;
    }
    
    @Override
    public String toString() {
        return "UserRegistrationDto{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", userType=" + userType +
                '}';
    }
}
