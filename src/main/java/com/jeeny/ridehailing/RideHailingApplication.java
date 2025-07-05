package com.jeeny.ridehailing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for the Mini Ride Booking System
 * 
 * This Spring Boot application provides a backend API for a ride-hailing service
 * with user management, ride booking, and driver management functionality.
 */
@SpringBootApplication
public class RideHailingApplication {

    public static void main(String[] args) {
        SpringApplication.run(RideHailingApplication.class, args);
    }
}
