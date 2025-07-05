package com.jeeny.ridehailing.entity;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Enumeration for driver availability status
 */
@Schema(description = "Driver availability status", allowableValues = {"AVAILABLE", "UNAVAILABLE", "ON_RIDE"})
public enum AvailabilityStatus {
    @Schema(description = "Driver is available for new rides")
    AVAILABLE("Available"),
    
    @Schema(description = "Driver is not available")
    UNAVAILABLE("Unavailable"),
    
    @Schema(description = "Driver is currently on a ride")
    ON_RIDE("On Ride");
    
    private final String displayName;
    
    AvailabilityStatus(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}
