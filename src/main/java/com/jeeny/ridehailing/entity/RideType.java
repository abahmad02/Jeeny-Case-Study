package com.jeeny.ridehailing.entity;

/**
 * Enumeration for ride types
 */
public enum RideType {
    BIKE("Bike"),
    CAR("Car"),
    RICKSHAW("Rickshaw");
    
    private final String displayName;
    
    RideType(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}
