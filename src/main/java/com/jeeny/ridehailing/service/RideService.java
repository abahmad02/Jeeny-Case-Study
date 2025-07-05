package com.jeeny.ridehailing.service;

import com.jeeny.ridehailing.dto.RideRequestDto;
import com.jeeny.ridehailing.dto.RideResponseDto;
import com.jeeny.ridehailing.dto.RideStatusUpdateDto;
import com.jeeny.ridehailing.entity.*;
import com.jeeny.ridehailing.exception.AccessDeniedException;
import com.jeeny.ridehailing.exception.BadRequestException;
import com.jeeny.ridehailing.exception.ResourceNotFoundException;
import com.jeeny.ridehailing.repository.RideRepository;
import com.jeeny.ridehailing.repository.UserRepository;
import com.jeeny.ridehailing.security.CustomUserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class for ride management operations
 */
@Service
@Transactional
public class RideService {
    
    private static final Logger logger = LoggerFactory.getLogger(RideService.class);
    
    @Autowired
    private RideRepository rideRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    /**
     * Request a new ride (Passenger only)
     */
    public RideResponseDto requestRide(RideRequestDto rideRequestDto) {
        User currentUser = getCurrentUser();
        
        // Only passengers can request rides
        if (currentUser.getUserType() != UserType.PASSENGER) {
            throw new BadRequestException("Only passengers can request rides");
        }
        
        // Check if passenger already has an active ride
        Optional<Ride> activeRide = rideRepository.findActiveRideByPassenger(currentUser);
        if (activeRide.isPresent()) {
            throw new BadRequestException("You already have an active ride");
        }
        
        // Create new ride
        Ride ride = new Ride();
        ride.setPassenger(currentUser);
        ride.setPickupLocation(rideRequestDto.getPickupLocation());
        ride.setDropLocation(rideRequestDto.getDropLocation());
        ride.setRideType(rideRequestDto.getRideType());
        ride.setStatus(RideStatus.REQUESTED);
        
        Ride savedRide = rideRepository.save(ride);
        logger.info("New ride requested by passenger {} with ID: {}", currentUser.getId(), savedRide.getId());
        
        return convertToRideResponseDto(savedRide);
    }
    
    /**
     * Get ride by ID
     */
    public RideResponseDto getRideById(Long rideId) {
        Ride ride = rideRepository.findById(rideId)
            .orElseThrow(() -> new ResourceNotFoundException("Ride not found with id: " + rideId));
        
        User currentUser = getCurrentUser();
        
        // Check if user has access to this ride
        if (!hasAccessToRide(currentUser, ride)) {
            throw new AccessDeniedException("You don't have access to this ride");
        }
        
        return convertToRideResponseDto(ride);
    }
    
    /**
     * Get ride history for current user
     */
    public List<RideResponseDto> getRideHistory() {
        User currentUser = getCurrentUser();
        List<Ride> rides;
        
        if (currentUser.getUserType() == UserType.PASSENGER) {
            rides = rideRepository.findByPassengerOrderByCreatedAtDesc(currentUser);
        } else {
            rides = rideRepository.findByDriverOrderByCreatedAtDesc(currentUser);
        }
        
        return rides.stream()
                .map(this::convertToRideResponseDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Get available ride requests for drivers
     */
    public List<RideResponseDto> getAvailableRideRequests() {
        User currentUser = getCurrentUser();
        
        // Only drivers can view available requests
        if (currentUser.getUserType() != UserType.DRIVER) {
            throw new BadRequestException("Only drivers can view available ride requests");
        }
        
        List<Ride> availableRides = rideRepository.findAvailableRideRequests(RideStatus.REQUESTED);
        
        return availableRides.stream()
                .map(this::convertToRideResponseDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Accept a ride request (Driver only)
     */
    public RideResponseDto acceptRide(Long rideId) {
        User currentDriver = getCurrentUser();
        
        // Only drivers can accept rides
        if (currentDriver.getUserType() != UserType.DRIVER) {
            throw new BadRequestException("Only drivers can accept rides");
        }
        
        // Check if driver is available
        if (currentDriver.getAvailabilityStatus() != AvailabilityStatus.AVAILABLE) {
            throw new BadRequestException("Driver is not available");
        }
        
        // Check if driver already has an active ride
        Optional<Ride> activeRide = rideRepository.findActiveRideByDriver(currentDriver);
        if (activeRide.isPresent()) {
            throw new BadRequestException("Driver already has an active ride");
        }
        
        Ride ride = rideRepository.findById(rideId)
            .orElseThrow(() -> new ResourceNotFoundException("Ride not found with id: " + rideId));
        
        // Check if ride is available for acceptance
        if (ride.getStatus() != RideStatus.REQUESTED || ride.getDriver() != null) {
            throw new BadRequestException("Ride is not available for acceptance");
        }
        
        // Accept the ride
        ride.setDriver(currentDriver);
        ride.setStatus(RideStatus.ACCEPTED);
        
        // Update driver availability
        currentDriver.setAvailabilityStatus(AvailabilityStatus.ON_RIDE);
        userRepository.save(currentDriver);
        
        Ride savedRide = rideRepository.save(ride);
        logger.info("Ride {} accepted by driver {}", rideId, currentDriver.getId());
        
        return convertToRideResponseDto(savedRide);
    }
    
    /**
     * Reject a ride request (Driver only)
     */
    public RideResponseDto rejectRide(Long rideId) {
        User currentDriver = getCurrentUser();
        
        // Only drivers can reject rides
        if (currentDriver.getUserType() != UserType.DRIVER) {
            throw new BadRequestException("Only drivers can reject rides");
        }
        
        Ride ride = rideRepository.findById(rideId)
            .orElseThrow(() -> new ResourceNotFoundException("Ride not found with id: " + rideId));
        
        // Check if ride can be rejected
        if (ride.getStatus() != RideStatus.REQUESTED || ride.getDriver() != null) {
            throw new BadRequestException("Ride cannot be rejected at this stage");
        }
        
        // Reject the ride
        ride.setStatus(RideStatus.REJECTED);
        
        Ride savedRide = rideRepository.save(ride);
        logger.info("Ride {} rejected by driver {}", rideId, currentDriver.getId());
        
        return convertToRideResponseDto(savedRide);
    }
    
    /**
     * Update ride status
     */
    public RideResponseDto updateRideStatus(Long rideId, RideStatusUpdateDto statusUpdateDto) {
        User currentUser = getCurrentUser();
        
        Ride ride = rideRepository.findById(rideId)
            .orElseThrow(() -> new ResourceNotFoundException("Ride not found with id: " + rideId));
        
        // Validate status transition based on user type and current status
        validateStatusTransition(currentUser, ride, statusUpdateDto.getStatus());
        
        RideStatus newStatus = statusUpdateDto.getStatus();
        ride.setStatus(newStatus);
        
        // Update driver availability when ride is completed
        if (newStatus == RideStatus.COMPLETED && ride.getDriver() != null) {
            User driver = ride.getDriver();
            driver.setAvailabilityStatus(AvailabilityStatus.AVAILABLE);
            userRepository.save(driver);
        }
        
        Ride savedRide = rideRepository.save(ride);
        logger.info("Ride {} status updated to {} by user {}", rideId, newStatus, currentUser.getId());
        
        return convertToRideResponseDto(savedRide);
    }
    
    /**
     * Cancel a ride
     */
    public RideResponseDto cancelRide(Long rideId) {
        User currentUser = getCurrentUser();
        
        Ride ride = rideRepository.findById(rideId)
            .orElseThrow(() -> new ResourceNotFoundException("Ride not found with id: " + rideId));
        
        // Check if user has permission to cancel this ride
        if (!hasAccessToRide(currentUser, ride)) {
            throw new AccessDeniedException("You don't have permission to cancel this ride");
        }
        
        // Check if ride can be cancelled
        if (ride.getStatus() == RideStatus.COMPLETED || ride.getStatus() == RideStatus.CANCELLED) {
            throw new BadRequestException("Ride cannot be cancelled");
        }
        
        // Cancel the ride
        ride.setStatus(RideStatus.CANCELLED);
        
        // If ride was accepted, make driver available again
        if (ride.getDriver() != null) {
            User driver = ride.getDriver();
            driver.setAvailabilityStatus(AvailabilityStatus.AVAILABLE);
            userRepository.save(driver);
        }
        
        Ride savedRide = rideRepository.save(ride);
        logger.info("Ride {} cancelled by user {}", rideId, currentUser.getId());
        
        return convertToRideResponseDto(savedRide);
    }
    
    /**
     * Get current authenticated user
     */
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserPrincipal userPrincipal = (CustomUserPrincipal) authentication.getPrincipal();
        
        return userRepository.findByEmail(userPrincipal.getEmail())
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
    
    /**
     * Check if user has access to a ride
     */
    private boolean hasAccessToRide(User user, Ride ride) {
        return ride.getPassenger().getId().equals(user.getId()) ||
               (ride.getDriver() != null && ride.getDriver().getId().equals(user.getId()));
    }
    
    /**
     * Validate status transition
     */
    private void validateStatusTransition(User user, Ride ride, RideStatus newStatus) {
        RideStatus currentStatus = ride.getStatus();
        
        // Check user permissions
        if (!hasAccessToRide(user, ride)) {
            throw new AccessDeniedException("You don't have permission to update this ride");
        }
        
        // Validate status transitions
        switch (newStatus) {
            case IN_PROGRESS:
                if (currentStatus != RideStatus.ACCEPTED) {
                    throw new BadRequestException("Ride must be accepted before starting");
                }
                if (user.getUserType() != UserType.DRIVER || !ride.getDriver().getId().equals(user.getId())) {
                    throw new BadRequestException("Only the assigned driver can start the ride");
                }
                break;
                
            case COMPLETED:
                if (currentStatus != RideStatus.IN_PROGRESS) {
                    throw new BadRequestException("Ride must be in progress before completing");
                }
                if (user.getUserType() != UserType.DRIVER || !ride.getDriver().getId().equals(user.getId())) {
                    throw new BadRequestException("Only the assigned driver can complete the ride");
                }
                break;
                
            default:
                throw new BadRequestException("Invalid status transition");
        }
    }
    
    /**
     * Convert Ride entity to RideResponseDto
     */
    private RideResponseDto convertToRideResponseDto(Ride ride) {
        RideResponseDto dto = new RideResponseDto();
        dto.setId(ride.getId());
        dto.setPassengerId(ride.getPassenger().getId());
        dto.setPassengerName(ride.getPassenger().getName());
        
        if (ride.getDriver() != null) {
            dto.setDriverId(ride.getDriver().getId());
            dto.setDriverName(ride.getDriver().getName());
        }
        
        dto.setPickupLocation(ride.getPickupLocation());
        dto.setDropLocation(ride.getDropLocation());
        dto.setRideType(ride.getRideType());
        dto.setStatus(ride.getStatus());
        dto.setCreatedAt(ride.getCreatedAt());
        dto.setAcceptedAt(ride.getAcceptedAt());
        dto.setStartedAt(ride.getStartedAt());
        dto.setCompletedAt(ride.getCompletedAt());
        
        return dto;
    }
}
