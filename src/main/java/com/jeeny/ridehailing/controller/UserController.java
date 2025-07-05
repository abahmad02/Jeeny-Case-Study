package com.jeeny.ridehailing.controller;

import com.jeeny.ridehailing.dto.AuthResponseDto;
import com.jeeny.ridehailing.dto.LoginDto;
import com.jeeny.ridehailing.dto.UserRegistrationDto;
import com.jeeny.ridehailing.dto.UserResponseDto;
import com.jeeny.ridehailing.entity.AvailabilityStatus;
import com.jeeny.ridehailing.exception.ErrorResponse;
import com.jeeny.ridehailing.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for user management operations
 */
@RestController
@RequestMapping("/api")
@Tag(name = "User Management", description = "APIs for user registration, authentication, and management")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    /**
     * Register a new user
     */
    @PostMapping("/register")
    @Operation(
        summary = "Register a new user", 
        description = """
            Register a new passenger or driver.
            
            **Note**: This endpoint does NOT require authentication.
            
            **Example request body**:
            ```json
            {
              "name": "John Doe",
              "email": "john.doe@example.com",
              "phoneNumber": "+1234567890",
              "password": "password123",
              "userType": "PASSENGER"
            }
            ```
            """
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201", 
            description = "User registered successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UserResponseDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Invalid input or email already exists",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(
                    example = """
                    {
                      "status": 400,
                      "error": "Bad Request",
                      "message": "Invalid input or email already exists",
                      "path": "/api/register",
                      "timestamp": "2025-07-05T15:30:35.827"
                    }
                    """
                )
            )
        )
    })
    public ResponseEntity<UserResponseDto> registerUser(@Valid @RequestBody UserRegistrationDto registrationDto) {
        UserResponseDto user = userService.registerUser(registrationDto);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }
    
    /**
     * Authenticate user login
     */
    @PostMapping("/login")
    @Operation(
        summary = "User login", 
        description = """
            Authenticate user and return JWT token.
            
            **Note**: This endpoint does NOT require authentication.
            
            **Steps after login**:
            1. Copy the `token` from the response
            2. Click the Authorize button at the top
            3. Enter: `Bearer YOUR_TOKEN_HERE`
            4. Click Authorize
            
            **Example request body**:
            ```json
            {
              "email": "john.doe@example.com",
              "password": "password123"
            }
            ```
            
            **Sample users (password: password123)**:
            - john.doe@example.com (PASSENGER)
            - bob.wilson@example.com (DRIVER)
            """
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Login successful - copy the token for authorization",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AuthResponseDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "401", 
            description = "Invalid credentials",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(
                    example = """
                    {
                      "status": 401,
                      "error": "Unauthorized",
                      "message": "Invalid credentials",
                      "path": "/api/login",
                      "timestamp": "2025-07-05T15:30:35.827"
                    }
                    """
                )
            )
        )
    })
    public ResponseEntity<AuthResponseDto> loginUser(@Valid @RequestBody LoginDto loginDto) {
        AuthResponseDto authResponse = userService.authenticateUser(loginDto);
        return ResponseEntity.ok(authResponse);
    }
    
    /**
     * Get user by ID
     */
    @GetMapping("/users/{userId}")
    @Operation(summary = "Get user by ID", description = "Retrieve user information by user ID")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "User found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UserResponseDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "401", 
            description = "Authentication required",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "User not found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        )
    })
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long userId) {
        UserResponseDto user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }
    
    /**
     * Get all drivers
     */
    @GetMapping("/users/drivers")
    @Operation(summary = "Get all drivers", description = "Retrieve list of all drivers")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Drivers retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UserResponseDto.class, type = "array")
            )
        ),
        @ApiResponse(
            responseCode = "401", 
            description = "Authentication required",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        )
    })
    public ResponseEntity<List<UserResponseDto>> getAllDrivers() {
        List<UserResponseDto> drivers = userService.getAllDrivers();
        return ResponseEntity.ok(drivers);
    }
    
    /**
     * Get available drivers
     */
    @GetMapping("/users/drivers/available")
    @Operation(summary = "Get available drivers", description = "Retrieve list of available drivers")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Available drivers retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UserResponseDto.class, type = "array")
            )
        ),
        @ApiResponse(
            responseCode = "401", 
            description = "Authentication required",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        )
    })
    public ResponseEntity<List<UserResponseDto>> getAvailableDrivers() {
        List<UserResponseDto> availableDrivers = userService.getAvailableDrivers();
        return ResponseEntity.ok(availableDrivers);
    }
    
    /**
     * Update driver availability
     */
    @PutMapping("/users/drivers/{driverId}/availability")
    @Operation(summary = "Update driver availability", description = "Update driver availability status")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Availability updated successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UserResponseDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Invalid request or user is not a driver",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "401", 
            description = "Authentication required",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "403", 
            description = "Access denied",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Driver not found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        )
    })
    public ResponseEntity<UserResponseDto> updateDriverAvailability(
            @PathVariable Long driverId,
            @RequestParam AvailabilityStatus status) {
        UserResponseDto updatedDriver = userService.updateDriverAvailability(driverId, status);
        return ResponseEntity.ok(updatedDriver);
    }
    
    /**
     * Get all passengers
     */
    @GetMapping("/users/passengers")
    @Operation(summary = "Get all passengers", description = "Retrieve list of all passengers")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Passengers retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UserResponseDto.class, type = "array")
            )
        ),
        @ApiResponse(
            responseCode = "401", 
            description = "Authentication required",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        )
    })
    public ResponseEntity<List<UserResponseDto>> getAllPassengers() {
        List<UserResponseDto> passengers = userService.getAllPassengers();
        return ResponseEntity.ok(passengers);
    }
}
