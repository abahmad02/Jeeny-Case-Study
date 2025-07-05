package com.jeeny.ridehailing.controller;

import com.jeeny.ridehailing.dto.RideRequestDto;
import com.jeeny.ridehailing.dto.RideResponseDto;
import com.jeeny.ridehailing.dto.RideStatusUpdateDto;
import com.jeeny.ridehailing.exception.ErrorResponse;
import com.jeeny.ridehailing.service.RideService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for ride management operations
 */
@RestController
@RequestMapping("/api/rides")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Ride Management", description = "APIs for ride booking and management")
public class RideController {
    
    @Autowired
    private RideService rideService;
    
    /**
     * Request a new ride (Passenger only)
     */
    @PostMapping("/request")
    @Operation(summary = "Request a ride", description = "Passengers can request a new ride")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201", 
            description = "Ride requested successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = RideResponseDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Invalid request or passenger already has active ride",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(
                    example = """
                    {
                      "status": 400,
                      "error": "Bad Request",
                      "message": "Invalid input data or passenger already has active ride",
                      "path": "/api/rides/request",
                      "timestamp": "2025-07-05T15:30:35.827"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "401", 
            description = "Authentication required",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(
                    example = """
                    {
                      "status": 401,
                      "error": "Unauthorized",
                      "message": "Authentication required",
                      "path": "/api/rides/request",
                      "timestamp": "2025-07-05T15:30:35.827"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "403", 
            description = "Only passengers can request rides",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(
                    example = """
                    {
                      "status": 403,
                      "error": "Forbidden",
                      "message": "Only passengers can request rides",
                      "path": "/api/rides/request",
                      "timestamp": "2025-07-05T15:30:35.827"
                    }
                    """
                )
            )
        )
    })
    public ResponseEntity<RideResponseDto> requestRide(@Valid @RequestBody RideRequestDto rideRequestDto) {
        RideResponseDto ride = rideService.requestRide(rideRequestDto);
        return new ResponseEntity<>(ride, HttpStatus.CREATED);
    }
    
    /**
     * Get ride by ID
     */
    @GetMapping("/{rideId}")
    @Operation(summary = "Get ride details", description = "Get ride details by ID")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Ride found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = RideResponseDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "401", 
            description = "Authentication required",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(
                    type = "object",
                    example = """
                    {
                      "status": 401,
                      "error": "Unauthorized",
                      "message": "Authentication required",
                      "path": "/api/rides/123",
                      "timestamp": "2025-07-05T15:38:09.522Z"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "403", 
            description = "Access denied to this ride",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(
                    type = "object",
                    example = """
                    {
                      "status": 403,
                      "error": "Forbidden",
                      "message": "Access denied to this ride",
                      "path": "/api/rides/123",
                      "timestamp": "2025-07-05T15:38:09.524Z"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Ride not found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(
                    type = "object",
                    example = """
                    {
                      "status": 404,
                      "error": "Not Found",
                      "message": "Ride not found",
                      "path": "/api/rides/123",
                      "timestamp": "2025-07-05T15:38:09.525Z"
                    }
                    """
                )
            )
        )
    })
    public ResponseEntity<RideResponseDto> getRideById(@PathVariable Long rideId) {
        RideResponseDto ride = rideService.getRideById(rideId);
        return ResponseEntity.ok(ride);
    }
    
    /**
     * Get ride history for current user
     */
    @GetMapping("/history")
    @Operation(summary = "Get ride history", description = "Get ride history for the current user")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Ride history retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = RideResponseDto.class, type = "array")
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
    public ResponseEntity<List<RideResponseDto>> getRideHistory() {
        List<RideResponseDto> rideHistory = rideService.getRideHistory();
        return ResponseEntity.ok(rideHistory);
    }
    
    /**
     * Get available ride requests (Driver only)
     */
    @GetMapping("/available")
    @Operation(summary = "Get available ride requests", description = "Drivers can view available ride requests")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Available rides retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = RideResponseDto.class, type = "array")
            )
        ),
        @ApiResponse(
            responseCode = "401", 
            description = "Authentication required",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(
                    example = """
                    {
                      "status": 401,
                      "error": "Unauthorized",
                      "message": "Authentication required",
                      "path": "/api/rides/available",
                      "timestamp": "2025-07-05T15:30:35.827"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "403", 
            description = "Only drivers can view available requests",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(
                    example = """
                    {
                      "status": 403,
                      "error": "Forbidden",
                      "message": "Only drivers can view available requests",
                      "path": "/api/rides/available",
                      "timestamp": "2025-07-05T15:30:35.827"
                    }
                    """
                )
            )
        )
    })
    public ResponseEntity<List<RideResponseDto>> getAvailableRideRequests() {
        List<RideResponseDto> availableRides = rideService.getAvailableRideRequests();
        return ResponseEntity.ok(availableRides);
    }
    
    /**
     * Accept a ride request (Driver only)
     */
    @PostMapping("/{rideId}/accept")
    @Operation(summary = "Accept ride request", description = "Drivers can accept a ride request")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Ride accepted successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = RideResponseDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Invalid request or ride not available",
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
            description = "Only drivers can accept rides",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Ride not found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        )
    })
    public ResponseEntity<RideResponseDto> acceptRide(@PathVariable Long rideId) {
        RideResponseDto ride = rideService.acceptRide(rideId);
        return ResponseEntity.ok(ride);
    }
    
    /**
     * Reject a ride request (Driver only)
     */
    @PostMapping("/{rideId}/reject")
    @Operation(summary = "Reject ride request", description = "Drivers can reject a ride request")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Ride rejected successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = RideResponseDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Invalid request or ride cannot be rejected",
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
            description = "Only drivers can reject rides",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Ride not found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        )
    })
    public ResponseEntity<RideResponseDto> rejectRide(@PathVariable Long rideId) {
        RideResponseDto ride = rideService.rejectRide(rideId);
        return ResponseEntity.ok(ride);
    }
    
    /**
     * Update ride status
     */
    @PutMapping("/{rideId}/status")
    @Operation(
        summary = "Update ride status", 
        description = """
            Update ride status following the valid transition flow.
            
            **Valid Status Transitions (Driver only)**:
            - ACCEPTED → IN_PROGRESS (Start the ride)
            - IN_PROGRESS → COMPLETED (Complete the ride)
            
            **Business Rules**:
            - Only the assigned driver can update ride status
            - You cannot go backwards in the status flow
            - Use /cancel endpoint to cancel rides instead
            
            **Example request body**:
            ```json
            {
              "status": "IN_PROGRESS"
            }
            ```
            or
            ```json
            {
              "status": "COMPLETED"
            }
            ```
            """
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Ride status updated successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = RideResponseDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Invalid status transition",
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
            description = "Access denied or invalid permissions",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Ride not found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        )
    })
    public ResponseEntity<RideResponseDto> updateRideStatus(
            @PathVariable Long rideId,
            @Valid @RequestBody RideStatusUpdateDto statusUpdateDto) {
        RideResponseDto ride = rideService.updateRideStatus(rideId, statusUpdateDto);
        return ResponseEntity.ok(ride);
    }
    
    /**
     * Cancel a ride
     */
    @PostMapping("/{rideId}/cancel")
    @Operation(summary = "Cancel ride", description = "Cancel a ride (both passengers and drivers can cancel)")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Ride cancelled successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = RideResponseDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Ride cannot be cancelled",
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
            description = "Access denied to this ride",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Ride not found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        )
    })
    public ResponseEntity<RideResponseDto> cancelRide(@PathVariable Long rideId) {
        RideResponseDto ride = rideService.cancelRide(rideId);
        return ResponseEntity.ok(ride);
    }
}
