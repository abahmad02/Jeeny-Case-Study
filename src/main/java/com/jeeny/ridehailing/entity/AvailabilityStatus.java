package com.jeeny.ridehailing.entity;

/**
 * Enumeration for driver availability status
 */
public enum AvailabilityStatus {
    AVAILABLE("Available"),
    UNAVAILABLE("Unavailable"),
    ON_RIDE("On Ride");
    
    private final String displayName;
    
    AvailabilityStatus(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}
