package com.jeeny.ridehailing.repository;

import com.jeeny.ridehailing.entity.Ride;
import com.jeeny.ridehailing.entity.RideStatus;
import com.jeeny.ridehailing.entity.RideType;
import com.jeeny.ridehailing.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Ride entity operations
 */
@Repository
public interface RideRepository extends JpaRepository<Ride, Long> {
    
    /**
     * Find rides by passenger
     */
    List<Ride> findByPassengerOrderByCreatedAtDesc(User passenger);
    
    /**
     * Find rides by driver
     */
    List<Ride> findByDriverOrderByCreatedAtDesc(User driver);
    
    /**
     * Find rides by status
     */
    List<Ride> findByStatusOrderByCreatedAtDesc(RideStatus status);
    
    /**
     * Find rides by passenger and status
     */
    List<Ride> findByPassengerAndStatusOrderByCreatedAtDesc(User passenger, RideStatus status);
    
    /**
     * Find rides by driver and status
     */
    List<Ride> findByDriverAndStatusOrderByCreatedAtDesc(User driver, RideStatus status);
    
    /**
     * Find available ride requests (REQUESTED status, no driver assigned)
     */
    @Query("SELECT r FROM Ride r WHERE r.status = :status AND r.driver IS NULL ORDER BY r.createdAt ASC")
    List<Ride> findAvailableRideRequests(@Param("status") RideStatus status);
    
    /**
     * Find active ride for passenger (ACCEPTED or IN_PROGRESS)
     */
    @Query("SELECT r FROM Ride r WHERE r.passenger = :passenger AND r.status IN ('ACCEPTED', 'IN_PROGRESS')")
    Optional<Ride> findActiveRideByPassenger(@Param("passenger") User passenger);
    
    /**
     * Find active ride for driver (ACCEPTED or IN_PROGRESS)
     */
    @Query("SELECT r FROM Ride r WHERE r.driver = :driver AND r.status IN ('ACCEPTED', 'IN_PROGRESS')")
    Optional<Ride> findActiveRideByDriver(@Param("driver") User driver);
    
    /**
     * Find rides by ride type
     */
    List<Ride> findByRideTypeOrderByCreatedAtDesc(RideType rideType);
    
    /**
     * Find rides created within date range
     */
    @Query("SELECT r FROM Ride r WHERE r.createdAt BETWEEN :startDate AND :endDate ORDER BY r.createdAt DESC")
    List<Ride> findRidesByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    /**
     * Count rides by status
     */
    long countByStatus(RideStatus status);
    
    /**
     * Count rides by passenger
     */
    long countByPassenger(User passenger);
    
    /**
     * Count rides by driver
     */
    long countByDriver(User driver);
}
