package com.jeeny.ridehailing.dto;

import com.jeeny.ridehailing.entity.RideType;
import com.jeeny.ridehailing.entity.RideStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

/**
 * DTO for ride response
 */
@Schema(description = "Ride information response")
public class RideResponseDto {
    
    @Schema(description = "Ride ID", example = "1")
    private Long id;
    
    @Schema(description = "Passenger ID", example = "1")
    private Long passengerId;
    
    @Schema(description = "Passenger name", example = "John Doe")
    private String passengerName;
    
    @Schema(description = "Driver ID", example = "2")
    private Long driverId;
    
    @Schema(description = "Driver name", example = "Bob Wilson")
    private String driverName;
    
    @Schema(description = "Pickup location", example = "123 Main St")
    private String pickupLocation;
    
    @Schema(description = "Drop-off location", example = "456 Oak Ave")
    private String dropLocation;
    
    @Schema(description = "Type of ride", example = "ECONOMY")
    private RideType rideType;
    
    @Schema(description = "Current ride status", example = "REQUESTED")
    private RideStatus status;
    
    @Schema(description = "Ride creation timestamp")
    private LocalDateTime createdAt;
    
    @Schema(description = "Ride acceptance timestamp")
    private LocalDateTime acceptedAt;
    
    @Schema(description = "Ride start timestamp")
    private LocalDateTime startedAt;
    
    @Schema(description = "Ride completion timestamp")
    private LocalDateTime completedAt;
    
    // Constructors
    public RideResponseDto() {}
    
    public RideResponseDto(Long id, Long passengerId, String passengerName, Long driverId, 
                          String driverName, String pickupLocation, String dropLocation, 
                          RideType rideType, RideStatus status, LocalDateTime createdAt,
                          LocalDateTime acceptedAt, LocalDateTime startedAt, LocalDateTime completedAt) {
        this.id = id;
        this.passengerId = passengerId;
        this.passengerName = passengerName;
        this.driverId = driverId;
        this.driverName = driverName;
        this.pickupLocation = pickupLocation;
        this.dropLocation = dropLocation;
        this.rideType = rideType;
        this.status = status;
        this.createdAt = createdAt;
        this.acceptedAt = acceptedAt;
        this.startedAt = startedAt;
        this.completedAt = completedAt;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getPassengerId() {
        return passengerId;
    }
    
    public void setPassengerId(Long passengerId) {
        this.passengerId = passengerId;
    }
    
    public String getPassengerName() {
        return passengerName;
    }
    
    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }
    
    public Long getDriverId() {
        return driverId;
    }
    
    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }
    
    public String getDriverName() {
        return driverName;
    }
    
    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }
    
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
    
    public RideStatus getStatus() {
        return status;
    }
    
    public void setStatus(RideStatus status) {
        this.status = status;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getAcceptedAt() {
        return acceptedAt;
    }
    
    public void setAcceptedAt(LocalDateTime acceptedAt) {
        this.acceptedAt = acceptedAt;
    }
    
    public LocalDateTime getStartedAt() {
        return startedAt;
    }
    
    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }
    
    public LocalDateTime getCompletedAt() {
        return completedAt;
    }
    
    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }
}
