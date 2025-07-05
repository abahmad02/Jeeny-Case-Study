package com.jeeny.ridehailing.config;

import com.jeeny.ridehailing.entity.*;
import com.jeeny.ridehailing.repository.RideRepository;
import com.jeeny.ridehailing.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Data initialization component to seed the database with sample data
 */
@Component
public class DataInitializer implements CommandLineRunner {
    
    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RideRepository rideRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) throws Exception {
        initializeData();
    }
    
    private void initializeData() {
        // Skip initialization if data already exists
        if (userRepository.count() > 0) {
            logger.info("Database already contains data, skipping initialization");
            return;
        }
        
        logger.info("Initializing database with sample data...");
        
        // Create sample passengers
        User passenger1 = createUser("John Doe", "john.doe@example.com", "+1234567890", "password123", UserType.PASSENGER);
        User passenger2 = createUser("Jane Smith", "jane.smith@example.com", "+1234567891", "password123", UserType.PASSENGER);
        User passenger3 = createUser("Alice Johnson", "alice.johnson@example.com", "+1234567892", "password123", UserType.PASSENGER);
        
        // Create sample drivers
        User driver1 = createUser("Bob Wilson", "bob.wilson@example.com", "+1234567893", "password123", UserType.DRIVER);
        User driver2 = createUser("Carol Brown", "carol.brown@example.com", "+1234567894", "password123", UserType.DRIVER);
        User driver3 = createUser("David Lee", "david.lee@example.com", "+1234567895", "password123", UserType.DRIVER);
        
        // Save users
        userRepository.save(passenger1);
        userRepository.save(passenger2);
        userRepository.save(passenger3);
        userRepository.save(driver1);
        userRepository.save(driver2);
        userRepository.save(driver3);
        
        // Create sample rides
        createSampleRides(passenger1, passenger2, passenger3, driver1, driver2);
        
        logger.info("Database initialization completed successfully");
        logger.info("Sample users created:");
        logger.info("Passengers: john.doe@example.com, jane.smith@example.com, alice.johnson@example.com");
        logger.info("Drivers: bob.wilson@example.com, carol.brown@example.com, david.lee@example.com");
        logger.info("Password for all users: password123");
    }
    
    private User createUser(String name, String email, String phoneNumber, String password, UserType userType) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);
        user.setPassword(passwordEncoder.encode(password));
        user.setUserType(userType);
        
        if (userType == UserType.DRIVER) {
            user.setAvailabilityStatus(AvailabilityStatus.AVAILABLE);
        }
        
        return user;
    }
    
    private void createSampleRides(User passenger1, User passenger2, User passenger3, User driver1, User driver2) {
        // Completed ride
        Ride completedRide = new Ride();
        completedRide.setPassenger(passenger1);
        completedRide.setDriver(driver1);
        completedRide.setPickupLocation("Airport");
        completedRide.setDropLocation("City Center");
        completedRide.setRideType(RideType.CAR);
        completedRide.setStatus(RideStatus.COMPLETED);
        rideRepository.save(completedRide);
        
        // In progress ride
        Ride inProgressRide = new Ride();
        inProgressRide.setPassenger(passenger2);
        inProgressRide.setDriver(driver2);
        inProgressRide.setPickupLocation("Mall Road");
        inProgressRide.setDropLocation("University");
        inProgressRide.setRideType(RideType.BIKE);
        inProgressRide.setStatus(RideStatus.IN_PROGRESS);
        rideRepository.save(inProgressRide);
        
        // Update driver2 availability to ON_RIDE
        driver2.setAvailabilityStatus(AvailabilityStatus.ON_RIDE);
        userRepository.save(driver2);
        
        // Requested ride (available for drivers to accept)
        Ride requestedRide = new Ride();
        requestedRide.setPassenger(passenger3);
        requestedRide.setPickupLocation("Train Station");
        requestedRide.setDropLocation("Shopping Center");
        requestedRide.setRideType(RideType.RICKSHAW);
        requestedRide.setStatus(RideStatus.REQUESTED);
        rideRepository.save(requestedRide);
        
        // Another requested ride
        Ride requestedRide2 = new Ride();
        requestedRide2.setPassenger(passenger1);
        requestedRide2.setPickupLocation("Hotel");
        requestedRide2.setDropLocation("Beach");
        requestedRide2.setRideType(RideType.CAR);
        requestedRide2.setStatus(RideStatus.REQUESTED);
        rideRepository.save(requestedRide2);
        
        logger.info("Sample rides created with various statuses");
    }
}
