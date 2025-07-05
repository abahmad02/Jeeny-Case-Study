package com.jeeny.ridehailing.service;

import com.jeeny.ridehailing.dto.RideRequestDto;
import com.jeeny.ridehailing.dto.RideResponseDto;
import com.jeeny.ridehailing.entity.*;
import com.jeeny.ridehailing.exception.BadRequestException;
import com.jeeny.ridehailing.exception.ResourceNotFoundException;
import com.jeeny.ridehailing.repository.RideRepository;
import com.jeeny.ridehailing.repository.UserRepository;
import com.jeeny.ridehailing.security.CustomUserPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for RideService
 */
@ExtendWith(MockitoExtension.class)
class RideServiceTest {
    
    @Mock
    private RideRepository rideRepository;
    
    @Mock
    private UserRepository userRepository;
    
    @Mock
    private SecurityContext securityContext;
    
    @Mock
    private Authentication authentication;
    
    @Mock
    private CustomUserPrincipal userPrincipal;
    
    @InjectMocks
    private RideService rideService;
    
    private User samplePassenger;
    private User sampleDriver;
    private Ride sampleRide;
    private RideRequestDto rideRequestDto;
    
    @BeforeEach
    void setUp() {
        // Sample passenger
        samplePassenger = new User();
        samplePassenger.setId(1L);
        samplePassenger.setName("John Doe");
        samplePassenger.setEmail("john.doe@example.com");
        samplePassenger.setUserType(UserType.PASSENGER);
        
        // Sample driver
        sampleDriver = new User();
        sampleDriver.setId(2L);
        sampleDriver.setName("Jane Smith");
        sampleDriver.setEmail("jane.smith@example.com");
        sampleDriver.setUserType(UserType.DRIVER);
        sampleDriver.setAvailabilityStatus(AvailabilityStatus.AVAILABLE);
        
        // Sample ride
        sampleRide = new Ride();
        sampleRide.setId(1L);
        sampleRide.setPassenger(samplePassenger);
        sampleRide.setPickupLocation("Airport");
        sampleRide.setDropLocation("City Center");
        sampleRide.setRideType(RideType.CAR);
        sampleRide.setStatus(RideStatus.REQUESTED);
        sampleRide.setCreatedAt(LocalDateTime.now());
        
        // Ride request DTO
        rideRequestDto = new RideRequestDto();
        rideRequestDto.setPickupLocation("Airport");
        rideRequestDto.setDropLocation("City Center");
        rideRequestDto.setRideType(RideType.CAR);
    }
    
    private void mockAuthentication(User user) {
        when(userPrincipal.getEmail()).thenReturn(user.getEmail());
        when(authentication.getPrincipal()).thenReturn(userPrincipal);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
    }
    
    @Test
    void testRequestRide_Success() {
        // Arrange
        mockAuthentication(samplePassenger);
        when(rideRepository.findActiveRideByPassenger(samplePassenger)).thenReturn(Optional.empty());
        when(rideRepository.save(any(Ride.class))).thenReturn(sampleRide);
        
        try (MockedStatic<SecurityContextHolder> mockedSecurityContextHolder = mockStatic(SecurityContextHolder.class)) {
            mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            
            // Act
            RideResponseDto result = rideService.requestRide(rideRequestDto);
            
            // Assert
            assertNotNull(result);
            assertEquals(sampleRide.getId(), result.getId());
            assertEquals(samplePassenger.getId(), result.getPassengerId());
            assertEquals(samplePassenger.getName(), result.getPassengerName());
            assertEquals("Airport", result.getPickupLocation());
            assertEquals("City Center", result.getDropLocation());
            assertEquals(RideType.CAR, result.getRideType());
            assertEquals(RideStatus.REQUESTED, result.getStatus());
            
            verify(rideRepository).findActiveRideByPassenger(samplePassenger);
            verify(rideRepository).save(any(Ride.class));
        }
    }
    
    @Test
    void testRequestRide_DriverCannotRequest() {
        // Arrange
        mockAuthentication(sampleDriver);
        
        try (MockedStatic<SecurityContextHolder> mockedSecurityContextHolder = mockStatic(SecurityContextHolder.class)) {
            mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            
            // Act & Assert
            BadRequestException exception = assertThrows(BadRequestException.class, () -> {
                rideService.requestRide(rideRequestDto);
            });
            
            assertEquals("Only passengers can request rides", exception.getMessage());
            verify(rideRepository, never()).save(any(Ride.class));
        }
    }
    
    @Test
    void testRequestRide_PassengerAlreadyHasActiveRide() {
        // Arrange
        mockAuthentication(samplePassenger);
        when(rideRepository.findActiveRideByPassenger(samplePassenger)).thenReturn(Optional.of(sampleRide));
        
        try (MockedStatic<SecurityContextHolder> mockedSecurityContextHolder = mockStatic(SecurityContextHolder.class)) {
            mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            
            // Act & Assert
            BadRequestException exception = assertThrows(BadRequestException.class, () -> {
                rideService.requestRide(rideRequestDto);
            });
            
            assertEquals("You already have an active ride", exception.getMessage());
            verify(rideRepository).findActiveRideByPassenger(samplePassenger);
            verify(rideRepository, never()).save(any(Ride.class));
        }
    }
    
    @Test
    void testAcceptRide_Success() {
        // Arrange
        mockAuthentication(sampleDriver);
        when(rideRepository.findActiveRideByDriver(sampleDriver)).thenReturn(Optional.empty());
        when(rideRepository.findById(1L)).thenReturn(Optional.of(sampleRide));
        when(rideRepository.save(any(Ride.class))).thenReturn(sampleRide);
        when(userRepository.save(any(User.class))).thenReturn(sampleDriver);
        
        try (MockedStatic<SecurityContextHolder> mockedSecurityContextHolder = mockStatic(SecurityContextHolder.class)) {
            mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            
            // Act
            RideResponseDto result = rideService.acceptRide(1L);
            
            // Assert
            assertNotNull(result);
            assertEquals(sampleRide.getId(), result.getId());
            
            verify(rideRepository).findActiveRideByDriver(sampleDriver);
            verify(rideRepository).findById(1L);
            verify(rideRepository).save(sampleRide);
            verify(userRepository).save(sampleDriver);
        }
    }
    
    @Test
    void testAcceptRide_PassengerCannotAccept() {
        // Arrange
        mockAuthentication(samplePassenger);
        
        try (MockedStatic<SecurityContextHolder> mockedSecurityContextHolder = mockStatic(SecurityContextHolder.class)) {
            mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            
            // Act & Assert
            BadRequestException exception = assertThrows(BadRequestException.class, () -> {
                rideService.acceptRide(1L);
            });
            
            assertEquals("Only drivers can accept rides", exception.getMessage());
            verify(rideRepository, never()).findById(any());
        }
    }
    
    @Test
    void testAcceptRide_DriverNotAvailable() {
        // Arrange
        sampleDriver.setAvailabilityStatus(AvailabilityStatus.ON_RIDE);
        mockAuthentication(sampleDriver);
        
        try (MockedStatic<SecurityContextHolder> mockedSecurityContextHolder = mockStatic(SecurityContextHolder.class)) {
            mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            
            // Act & Assert
            BadRequestException exception = assertThrows(BadRequestException.class, () -> {
                rideService.acceptRide(1L);
            });
            
            assertEquals("Driver is not available", exception.getMessage());
            verify(rideRepository, never()).findById(any());
        }
    }
    
    @Test
    void testGetAvailableRideRequests_Success() {
        // Arrange
        mockAuthentication(sampleDriver);
        List<Ride> availableRides = Arrays.asList(sampleRide);
        when(rideRepository.findAvailableRideRequests(RideStatus.REQUESTED)).thenReturn(availableRides);
        
        try (MockedStatic<SecurityContextHolder> mockedSecurityContextHolder = mockStatic(SecurityContextHolder.class)) {
            mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            
            // Act
            List<RideResponseDto> result = rideService.getAvailableRideRequests();
            
            // Assert
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals(sampleRide.getId(), result.get(0).getId());
            
            verify(rideRepository).findAvailableRideRequests(RideStatus.REQUESTED);
        }
    }
    
    @Test
    void testGetAvailableRideRequests_PassengerCannotView() {
        // Arrange
        mockAuthentication(samplePassenger);
        
        try (MockedStatic<SecurityContextHolder> mockedSecurityContextHolder = mockStatic(SecurityContextHolder.class)) {
            mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            
            // Act & Assert
            BadRequestException exception = assertThrows(BadRequestException.class, () -> {
                rideService.getAvailableRideRequests();
            });
            
            assertEquals("Only drivers can view available ride requests", exception.getMessage());
            verify(rideRepository, never()).findAvailableRideRequests(any());
        }
    }
    
    @Test
    void testGetRideById_Success() {
        // Arrange
        mockAuthentication(samplePassenger);
        when(rideRepository.findById(1L)).thenReturn(Optional.of(sampleRide));
        
        try (MockedStatic<SecurityContextHolder> mockedSecurityContextHolder = mockStatic(SecurityContextHolder.class)) {
            mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            
            // Act
            RideResponseDto result = rideService.getRideById(1L);
            
            // Assert
            assertNotNull(result);
            assertEquals(sampleRide.getId(), result.getId());
            assertEquals(samplePassenger.getId(), result.getPassengerId());
            
            verify(rideRepository).findById(1L);
        }
    }
    
    @Test
    void testGetRideById_RideNotFound() {
        // Arrange
        mockAuthentication(samplePassenger);
        when(rideRepository.findById(999L)).thenReturn(Optional.empty());
        
        try (MockedStatic<SecurityContextHolder> mockedSecurityContextHolder = mockStatic(SecurityContextHolder.class)) {
            mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            
            // Act & Assert
            ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
                rideService.getRideById(999L);
            });
            
            assertEquals("Ride not found with id: 999", exception.getMessage());
            verify(rideRepository).findById(999L);
        }
    }
}
