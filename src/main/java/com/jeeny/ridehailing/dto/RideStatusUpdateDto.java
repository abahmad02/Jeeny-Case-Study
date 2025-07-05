package com.jeeny.ridehailing.dto;

import jakarta.validation.constraints.NotNull;
import com.jeeny.ridehailing.entity.RideStatus;

/**
 * DTO for ride status update request
 */
public class RideStatusUpdateDto {
    
    @NotNull(message = "Status is required")
    private RideStatus status;
    
    // Constructors
    public RideStatusUpdateDto() {}
    
    public RideStatusUpdateDto(RideStatus status) {
        this.status = status;
    }
    
    // Getters and Setters
    public RideStatus getStatus() {
        return status;
    }
    
    public void setStatus(RideStatus status) {
        this.status = status;
    }
    
    @Override
    public String toString() {
        return "RideStatusUpdateDto{" +
                "status=" + status +
                '}';
    }
}
