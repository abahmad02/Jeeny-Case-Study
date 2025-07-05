package com.jeeny.ridehailing.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import com.jeeny.ridehailing.entity.RideType;

/**
 * DTO for ride request
 */
public class RideRequestDto {
    
    @NotBlank(message = "Pickup location is required")
    private String pickupLocation;
    
    @NotBlank(message = "Drop location is required")
    private String dropLocation;
    
    @NotNull(message = "Ride type is required")
    private RideType rideType;
    
    // Constructors
    public RideRequestDto() {}
    
    public RideRequestDto(String pickupLocation, String dropLocation, RideType rideType) {
        this.pickupLocation = pickupLocation;
        this.dropLocation = dropLocation;
        this.rideType = rideType;
    }
    
    // Getters and Setters
    public String getPickupLocation() {
        return pickupLocation;
    }
    
    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }
    
    public String getDropLocation() {
        return dropLocation;
    }
    
    public void setDropLocation(String dropLocation) {
        this.dropLocation = dropLocation;
    }
    
    public RideType getRideType() {
        return rideType;
    }
    
    public void setRideType(RideType rideType) {
        this.rideType = rideType;
    }
    
    @Override
    public String toString() {
        return "RideRequestDto{" +
                "pickupLocation='" + pickupLocation + '\'' +
                ", dropLocation='" + dropLocation + '\'' +
                ", rideType=" + rideType +
                '}';
    }
}
