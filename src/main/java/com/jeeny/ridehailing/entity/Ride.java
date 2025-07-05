package com.jeeny.ridehailing.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

/**
 * Ride entity representing a ride booking in the system
 */
@Entity
@Table(name = "rides")
public class Ride {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "passenger_id", nullable = false)
    @NotNull(message = "Passenger is required")
    private User passenger;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id")
    private User driver;
    
    @NotBlank(message = "Pickup location is required")
    @Column(nullable = false, name = "pickup_location")
    private String pickupLocation;
    
    @NotBlank(message = "Drop location is required")
    @Column(nullable = false, name = "drop_location")
    private String dropLocation;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "ride_type")
    @NotNull(message = "Ride type is required")
    private RideType rideType;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "status")
    private RideStatus status = RideStatus.REQUESTED;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @Column(name = "accepted_at")
    private LocalDateTime acceptedAt;
    
    @Column(name = "started_at")
    private LocalDateTime startedAt;
    
    @Column(name = "completed_at")
    private LocalDateTime completedAt;
    
    // Constructors
    public Ride() {}
    
    public Ride(User passenger, String pickupLocation, String dropLocation, RideType rideType) {
        this.passenger = passenger;
        this.pickupLocation = pickupLocation;
        this.dropLocation = dropLocation;
        this.rideType = rideType;
        this.status = RideStatus.REQUESTED;
    }
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        
        // Set timestamps based on status changes
        switch (status) {
            case ACCEPTED:
                if (acceptedAt == null) {
                    acceptedAt = LocalDateTime.now();
                }
                break;
            case IN_PROGRESS:
                if (startedAt == null) {
                    startedAt = LocalDateTime.now();
                }
                break;
            case COMPLETED:
                if (completedAt == null) {
                    completedAt = LocalDateTime.now();
                }
                break;
            case REQUESTED:
            case REJECTED:
            case CANCELLED:
                // No specific timestamp action needed for these states
                break;
        }
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public User getPassenger() {
        return passenger;
    }
    
    public void setPassenger(User passenger) {
        this.passenger = passenger;
    }
    
    public User getDriver() {
        return driver;
    }
    
    public void setDriver(User driver) {
        this.driver = driver;
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
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
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
    
    @Override
    public String toString() {
        return "Ride{" +
                "id=" + id +
                ", passenger=" + (passenger != null ? passenger.getName() : "null") +
                ", driver=" + (driver != null ? driver.getName() : "null") +
                ", pickupLocation='" + pickupLocation + '\'' +
                ", dropLocation='" + dropLocation + '\'' +
                ", rideType=" + rideType +
                ", status=" + status +
                '}';
    }
}
