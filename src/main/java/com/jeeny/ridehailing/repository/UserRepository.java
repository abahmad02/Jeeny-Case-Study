package com.jeeny.ridehailing.repository;

import com.jeeny.ridehailing.entity.User;
import com.jeeny.ridehailing.entity.UserType;
import com.jeeny.ridehailing.entity.AvailabilityStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for User entity operations
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * Find user by email
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Check if email exists
     */
    boolean existsByEmail(String email);
    
    /**
     * Find users by type
     */
    List<User> findByUserType(UserType userType);
    
    /**
     * Find available drivers
     */
    @Query("SELECT u FROM User u WHERE u.userType = :userType AND u.availabilityStatus = :status")
    List<User> findByUserTypeAndAvailabilityStatus(
        @Param("userType") UserType userType, 
        @Param("status") AvailabilityStatus status
    );
    
    /**
     * Find drivers by availability status
     */
    List<User> findByAvailabilityStatus(AvailabilityStatus status);
    
    /**
     * Count users by type
     */
    long countByUserType(UserType userType);
}
