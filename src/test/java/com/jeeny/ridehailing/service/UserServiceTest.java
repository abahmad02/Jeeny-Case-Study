package com.jeeny.ridehailing.service;

import com.jeeny.ridehailing.dto.UserRegistrationDto;
import com.jeeny.ridehailing.dto.UserResponseDto;
import com.jeeny.ridehailing.entity.AvailabilityStatus;
import com.jeeny.ridehailing.entity.User;
import com.jeeny.ridehailing.entity.UserType;
import com.jeeny.ridehailing.exception.BadRequestException;
import com.jeeny.ridehailing.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Unit tests for UserService
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    
    @Mock
    private UserRepository userRepository;
    
    @Mock
    private PasswordEncoder passwordEncoder;
    
    @InjectMocks
    private UserService userService;
    
    private User samplePassenger;
    private User sampleDriver;
    private UserRegistrationDto passengerRegistrationDto;
    private UserRegistrationDto driverRegistrationDto;
    
    @BeforeEach
    void setUp() {
        // Sample passenger
        samplePassenger = new User();
        samplePassenger.setId(1L);
        samplePassenger.setName("John Doe");
        samplePassenger.setEmail("john.doe@example.com");
        samplePassenger.setPhoneNumber("+1234567890");
        samplePassenger.setPassword("encodedPassword");
        samplePassenger.setUserType(UserType.PASSENGER);
        samplePassenger.setCreatedAt(LocalDateTime.now());
        
        // Sample driver
        sampleDriver = new User();
        sampleDriver.setId(2L);
        sampleDriver.setName("Jane Smith");
        sampleDriver.setEmail("jane.smith@example.com");
        sampleDriver.setPhoneNumber("+1234567891");
        sampleDriver.setPassword("encodedPassword");
        sampleDriver.setUserType(UserType.DRIVER);
        sampleDriver.setAvailabilityStatus(AvailabilityStatus.AVAILABLE);
        sampleDriver.setCreatedAt(LocalDateTime.now());
        
        // Registration DTOs
        passengerRegistrationDto = new UserRegistrationDto();
        passengerRegistrationDto.setName("John Doe");
        passengerRegistrationDto.setEmail("john.doe@example.com");
        passengerRegistrationDto.setPhoneNumber("+1234567890");
        passengerRegistrationDto.setPassword("password123");
        passengerRegistrationDto.setUserType(UserType.PASSENGER);
        
        driverRegistrationDto = new UserRegistrationDto();
        driverRegistrationDto.setName("Jane Smith");
        driverRegistrationDto.setEmail("jane.smith@example.com");
        driverRegistrationDto.setPhoneNumber("+1234567891");
        driverRegistrationDto.setPassword("password123");
        driverRegistrationDto.setUserType(UserType.DRIVER);
    }
    
    @Test
    void testRegisterPassenger_Success() {
        // Arrange
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(samplePassenger);
        
        // Act
        UserResponseDto result = userService.registerUser(passengerRegistrationDto);
        
        // Assert
        assertNotNull(result);
        assertEquals(samplePassenger.getId(), result.getId());
        assertEquals(samplePassenger.getName(), result.getName());
        assertEquals(samplePassenger.getEmail(), result.getEmail());
        assertEquals(UserType.PASSENGER, result.getUserType());
        assertNull(result.getAvailabilityStatus());
        
        verify(userRepository).existsByEmail("john.doe@example.com");
        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(any(User.class));
    }
    
    @Test
    void testRegisterDriver_Success() {
        // Arrange
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(sampleDriver);
        
        // Act
        UserResponseDto result = userService.registerUser(driverRegistrationDto);
        
        // Assert
        assertNotNull(result);
        assertEquals(sampleDriver.getId(), result.getId());
        assertEquals(sampleDriver.getName(), result.getName());
        assertEquals(sampleDriver.getEmail(), result.getEmail());
        assertEquals(UserType.DRIVER, result.getUserType());
        assertEquals(AvailabilityStatus.AVAILABLE, result.getAvailabilityStatus());
        
        verify(userRepository).existsByEmail("jane.smith@example.com");
        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(any(User.class));
    }
    
    @Test
    void testRegisterUser_EmailAlreadyExists() {
        // Arrange
        when(userRepository.existsByEmail(anyString())).thenReturn(true);
        
        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            userService.registerUser(passengerRegistrationDto);
        });
        
        assertEquals("Email is already in use!", exception.getMessage());
        verify(userRepository).existsByEmail("john.doe@example.com");
        verify(userRepository, never()).save(any(User.class));
    }
    
    @Test
    void testGetAllDrivers_Success() {
        // Arrange
        List<User> drivers = Arrays.asList(sampleDriver);
        when(userRepository.findByUserType(UserType.DRIVER)).thenReturn(drivers);
        
        // Act
        List<UserResponseDto> result = userService.getAllDrivers();
        
        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(sampleDriver.getId(), result.get(0).getId());
        assertEquals(UserType.DRIVER, result.get(0).getUserType());
        
        verify(userRepository).findByUserType(UserType.DRIVER);
    }
    
    @Test
    void testGetAvailableDrivers_Success() {
        // Arrange
        List<User> availableDrivers = Arrays.asList(sampleDriver);
        when(userRepository.findByUserTypeAndAvailabilityStatus(UserType.DRIVER, AvailabilityStatus.AVAILABLE))
            .thenReturn(availableDrivers);
        
        // Act
        List<UserResponseDto> result = userService.getAvailableDrivers();
        
        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(sampleDriver.getId(), result.get(0).getId());
        assertEquals(AvailabilityStatus.AVAILABLE, result.get(0).getAvailabilityStatus());
        
        verify(userRepository).findByUserTypeAndAvailabilityStatus(UserType.DRIVER, AvailabilityStatus.AVAILABLE);
    }
    
    @Test
    void testUpdateDriverAvailability_Success() {
        // Arrange
        when(userRepository.findById(2L)).thenReturn(Optional.of(sampleDriver));
        when(userRepository.save(any(User.class))).thenReturn(sampleDriver);
        
        // Act
        UserResponseDto result = userService.updateDriverAvailability(2L, AvailabilityStatus.UNAVAILABLE);
        
        // Assert
        assertNotNull(result);
        assertEquals(sampleDriver.getId(), result.getId());
        
        verify(userRepository).findById(2L);
        verify(userRepository).save(sampleDriver);
    }
    
    @Test
    void testUpdateDriverAvailability_UserNotDriver() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(samplePassenger));
        
        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            userService.updateDriverAvailability(1L, AvailabilityStatus.UNAVAILABLE);
        });
        
        assertEquals("User is not a driver", exception.getMessage());
        verify(userRepository).findById(1L);
        verify(userRepository, never()).save(any(User.class));
    }
}
