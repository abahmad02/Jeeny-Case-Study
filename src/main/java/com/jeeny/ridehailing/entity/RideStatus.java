package com.jeeny.ridehailing.entity;

/**
 * Enumeration for ride status lifecycle
 */
public enum RideStatus {
    REQUESTED("Requested"),
    ACCEPTED("Accepted"),
    IN_PROGRESS("In Progress"),
    COMPLETED("Completed"),
    REJECTED("Rejected"),
    CANCELLED("Cancelled");
    
    private final String displayName;
    
    RideStatus(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}
