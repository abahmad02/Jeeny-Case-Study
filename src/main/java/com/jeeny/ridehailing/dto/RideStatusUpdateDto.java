package com.jeeny.ridehailing.dto;

import jakarta.validation.constraints.NotNull;
import com.jeeny.ridehailing.entity.RideStatus;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO for ride status update request
 */
@Schema(description = "Ride status update request")
public class RideStatusUpdateDto {
    
    @NotNull(message = "Status is required")
    @Schema(description = "New ride status", example = "ACCEPTED", required = true)
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
