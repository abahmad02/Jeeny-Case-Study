package com.jeeny.ridehailing.entity;

/**
 * Enumeration for user types in the system
 */
public enum UserType {
    PASSENGER("Passenger"),
    DRIVER("Driver");
    
    private final String displayName;
    
    UserType(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}
