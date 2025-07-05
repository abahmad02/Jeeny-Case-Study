package com.jeeny.ridehailing.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeeny.ridehailing.dto.LoginDto;
import com.jeeny.ridehailing.dto.RideRequestDto;
import com.jeeny.ridehailing.dto.UserRegistrationDto;
import com.jeeny.ridehailing.entity.RideType;
import com.jeeny.ridehailing.entity.UserType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the ride booking API
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebMvc
@ActiveProfiles("test")
class RideBookingIntegrationTest {
    
    @Autowired
    private WebApplicationContext webApplicationContext;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    private MockMvc mockMvc;
    
    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }
    
    @Test
    void testCompleteRideBookingFlow() throws Exception {
        // Register a passenger
        UserRegistrationDto passengerDto = new UserRegistrationDto();
        passengerDto.setName("Test Passenger");
        passengerDto.setEmail("passenger@test.com");
        passengerDto.setPassword("password123");
        passengerDto.setUserType(UserType.PASSENGER);
        
        mockMvc.perform(post("/api/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(passengerDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test Passenger"))
                .andExpect(jsonPath("$.email").value("passenger@test.com"))
                .andExpect(jsonPath("$.userType").value("PASSENGER"));
        
        // Register a driver
        UserRegistrationDto driverDto = new UserRegistrationDto();
        driverDto.setName("Test Driver");
        driverDto.setEmail("driver@test.com");
        driverDto.setPassword("password123");
        driverDto.setUserType(UserType.DRIVER);
        
        mockMvc.perform(post("/api/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(driverDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test Driver"))
                .andExpect(jsonPath("$.email").value("driver@test.com"))
                .andExpect(jsonPath("$.userType").value("DRIVER"))
                .andExpect(jsonPath("$.availabilityStatus").value("AVAILABLE"));
        
        // Login as passenger
        LoginDto passengerLogin = new LoginDto();
        passengerLogin.setEmail("passenger@test.com");
        passengerLogin.setPassword("password123");
        
        MvcResult passengerLoginResult = mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(passengerLogin)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.user.userType").value("PASSENGER"))
                .andReturn();
        
        String passengerResponse = passengerLoginResult.getResponse().getContentAsString();
        String passengerToken = objectMapper.readTree(passengerResponse).get("token").asText();
        
        // Login as driver
        LoginDto driverLogin = new LoginDto();
        driverLogin.setEmail("driver@test.com");
        driverLogin.setPassword("password123");
        
        MvcResult driverLoginResult = mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(driverLogin)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.user.userType").value("DRIVER"))
                .andReturn();
        
        String driverResponse = driverLoginResult.getResponse().getContentAsString();
        String driverToken = objectMapper.readTree(driverResponse).get("token").asText();
        
        // Passenger requests a ride
        RideRequestDto rideRequest = new RideRequestDto();
        rideRequest.setPickupLocation("Test Pickup");
        rideRequest.setDropLocation("Test Destination");
        rideRequest.setRideType(RideType.CAR);
        
        MvcResult rideRequestResult = mockMvc.perform(post("/api/rides/request")
                .header("Authorization", "Bearer " + passengerToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(rideRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.pickupLocation").value("Test Pickup"))
                .andExpect(jsonPath("$.dropLocation").value("Test Destination"))
                .andExpect(jsonPath("$.rideType").value("CAR"))
                .andExpect(jsonPath("$.status").value("REQUESTED"))
                .andReturn();
        
        String rideResponseContent = rideRequestResult.getResponse().getContentAsString();
        Long rideId = objectMapper.readTree(rideResponseContent).get("id").asLong();
        
        // Driver views available rides
        mockMvc.perform(get("/api/rides/available")
                .header("Authorization", "Bearer " + driverToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(rideId))
                .andExpect(jsonPath("$[0].status").value("REQUESTED"));
        
        // Driver accepts the ride
        mockMvc.perform(post("/api/rides/" + rideId + "/accept")
                .header("Authorization", "Bearer " + driverToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ACCEPTED"))
                .andExpect(jsonPath("$.driverId").exists());
        
        // Check ride history for passenger
        mockMvc.perform(get("/api/rides/history")
                .header("Authorization", "Bearer " + passengerToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(rideId))
                .andExpect(jsonPath("$[0].status").value("ACCEPTED"));
        
        // Check ride history for driver
        mockMvc.perform(get("/api/rides/history")
                .header("Authorization", "Bearer " + driverToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(rideId))
                .andExpect(jsonPath("$[0].status").value("ACCEPTED"));
    }
    
    @Test
    void testUnauthorizedAccess() throws Exception {
        // Try to request a ride without authentication
        RideRequestDto rideRequest = new RideRequestDto();
        rideRequest.setPickupLocation("Test Pickup");
        rideRequest.setDropLocation("Test Destination");
        rideRequest.setRideType(RideType.CAR);
        
        mockMvc.perform(post("/api/rides/request")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(rideRequest)))
                .andExpect(status().isUnauthorized());
        
        // Try to get ride history without authentication
        mockMvc.perform(get("/api/rides/history"))
                .andExpect(status().isUnauthorized());
        
        // Try to get available rides without authentication
        mockMvc.perform(get("/api/rides/available"))
                .andExpect(status().isUnauthorized());
    }
    
    @Test
    void testInvalidRegistration() throws Exception {
        // Try to register with invalid email
        UserRegistrationDto invalidUser = new UserRegistrationDto();
        invalidUser.setName("Test User");
        invalidUser.setEmail("invalid-email");
        invalidUser.setPassword("password123");
        invalidUser.setUserType(UserType.PASSENGER);
        
        mockMvc.perform(post("/api/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidUser)))
                .andExpect(status().isBadRequest());
        
        // Try to register with short password
        UserRegistrationDto shortPasswordUser = new UserRegistrationDto();
        shortPasswordUser.setName("Test User");
        shortPasswordUser.setEmail("test@example.com");
        shortPasswordUser.setPassword("123");
        shortPasswordUser.setUserType(UserType.PASSENGER);
        
        mockMvc.perform(post("/api/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(shortPasswordUser)))
                .andExpect(status().isBadRequest());
    }
}
