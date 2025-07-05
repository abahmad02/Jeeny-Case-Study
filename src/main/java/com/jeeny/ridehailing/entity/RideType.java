package com.jeeny.ridehailing.entity;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Enumeration for ride types
 */
@Schema(description = "Type of ride service", allowableValues = {"BIKE", "CAR", "RICKSHAW"})
public enum RideType {
    @Schema(description = "Motorcycle/bike ride")
    BIKE("Bike"),
    
    @Schema(description = "Car ride")
    CAR("Car"),
    
    @Schema(description = "Rickshaw ride")
    RICKSHAW("Rickshaw");
    
    private final String displayName;
    
    RideType(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}
