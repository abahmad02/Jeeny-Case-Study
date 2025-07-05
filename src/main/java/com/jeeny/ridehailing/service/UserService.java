package com.jeeny.ridehailing.service;

import com.jeeny.ridehailing.dto.AuthResponseDto;
import com.jeeny.ridehailing.dto.LoginDto;
import com.jeeny.ridehailing.dto.UserRegistrationDto;
import com.jeeny.ridehailing.dto.UserResponseDto;
import com.jeeny.ridehailing.entity.AvailabilityStatus;
import com.jeeny.ridehailing.entity.User;
import com.jeeny.ridehailing.entity.UserType;
import com.jeeny.ridehailing.exception.BadRequestException;
import com.jeeny.ridehailing.exception.ResourceNotFoundException;
import com.jeeny.ridehailing.repository.UserRepository;
import com.jeeny.ridehailing.security.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for user management operations
 */
@Service
@Transactional
public class UserService {
    
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private JwtUtils jwtUtils;
    
    /**
     * Register a new user
     */
    public UserResponseDto registerUser(UserRegistrationDto registrationDto) {
        logger.info("Registering new user with email: {}", registrationDto.getEmail());
        
        // Check if email already exists
        if (userRepository.existsByEmail(registrationDto.getEmail())) {
            throw new BadRequestException("Email is already in use!");
        }
        
        // Create new user
        User user = new User();
        user.setName(registrationDto.getName());
        user.setEmail(registrationDto.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        user.setUserType(registrationDto.getUserType());
        
        // Set availability status for drivers
        if (registrationDto.getUserType() == UserType.DRIVER) {
            user.setAvailabilityStatus(AvailabilityStatus.AVAILABLE);
        }
        
        User savedUser = userRepository.save(user);
        logger.info("User registered successfully with ID: {}", savedUser.getId());
        
        return convertToUserResponseDto(savedUser);
    }
    
    /**
     * Authenticate user login
     */
    public AuthResponseDto authenticateUser(LoginDto loginDto) {
        logger.info("Authenticating user with email: {}", loginDto.getEmail());
        
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())
        );
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        
        User user = userRepository.findByEmail(loginDto.getEmail())
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        UserResponseDto userResponse = convertToUserResponseDto(user);
        
        logger.info("User authenticated successfully: {}", user.getEmail());
        return new AuthResponseDto(jwt, userResponse);
    }
    
    /**
     * Get user by ID
     */
    public UserResponseDto getUserById(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        return convertToUserResponseDto(user);
    }
    
    /**
     * Get user by email
     */
    public UserResponseDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        
        return convertToUserResponseDto(user);
    }
    
    /**
     * Get all drivers
     */
    public List<UserResponseDto> getAllDrivers() {
        List<User> drivers = userRepository.findByUserType(UserType.DRIVER);
        return drivers.stream()
                .map(this::convertToUserResponseDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Get available drivers
     */
    public List<UserResponseDto> getAvailableDrivers() {
        List<User> availableDrivers = userRepository.findByUserTypeAndAvailabilityStatus(
            UserType.DRIVER, AvailabilityStatus.AVAILABLE
        );
        return availableDrivers.stream()
                .map(this::convertToUserResponseDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Update driver availability status
     */
    public UserResponseDto updateDriverAvailability(Long driverId, AvailabilityStatus status) {
        User driver = userRepository.findById(driverId)
            .orElseThrow(() -> new ResourceNotFoundException("Driver not found with id: " + driverId));
        
        if (driver.getUserType() != UserType.DRIVER) {
            throw new BadRequestException("User is not a driver");
        }
        
        driver.setAvailabilityStatus(status);
        User updatedDriver = userRepository.save(driver);
        
        logger.info("Driver {} availability updated to {}", driverId, status);
        return convertToUserResponseDto(updatedDriver);
    }
    
    /**
     * Get all passengers
     */
    public List<UserResponseDto> getAllPassengers() {
        List<User> passengers = userRepository.findByUserType(UserType.PASSENGER);
        return passengers.stream()
                .map(this::convertToUserResponseDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Convert User entity to UserResponseDto
     */
    private UserResponseDto convertToUserResponseDto(User user) {
        UserResponseDto dto = new UserResponseDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setUserType(user.getUserType());
        dto.setAvailabilityStatus(user.getAvailabilityStatus());
        dto.setCreatedAt(user.getCreatedAt());
        return dto;
    }
}
