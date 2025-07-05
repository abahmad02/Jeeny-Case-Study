package com.jeeny.ridehailing.entity;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Enumeration for ride status lifecycle
 */
@Schema(description = "Current status of a ride", allowableValues = {"REQUESTED", "ACCEPTED", "IN_PROGRESS", "COMPLETED", "REJECTED", "CANCELLED"})
public enum RideStatus {
    @Schema(description = "Ride has been requested by passenger")
    REQUESTED("Requested"),
    
    @Schema(description = "Ride has been accepted by driver")
    ACCEPTED("Accepted"),
    
    @Schema(description = "Ride is currently in progress")
    IN_PROGRESS("In Progress"),
    
    @Schema(description = "Ride has been completed")
    COMPLETED("Completed"),
    
    @Schema(description = "Ride was rejected by driver")
    REJECTED("Rejected"),
    
    @Schema(description = "Ride was cancelled")
    CANCELLED("Cancelled");
    
    private final String displayName;
    
    RideStatus(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}
