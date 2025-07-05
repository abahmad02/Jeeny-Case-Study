package com.jeeny.ridehailing.entity;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Enumeration for user types in the system
 */
@Schema(description = "Type of user account", allowableValues = {"PASSENGER", "DRIVER"})
public enum UserType {
    @Schema(description = "Regular user who books rides")
    PASSENGER("Passenger"),
    
    @Schema(description = "Driver who provides ride services")
    DRIVER("Driver");
    
    private final String displayName;
    
    UserType(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}
