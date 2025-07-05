# Mini Ride Booking System - Design Document

## 📋 Project Overview
A backend API prototype for a ride-hailing application designed for testing in smaller cities. The system focuses on core booking functionality without real-time GPS/mapping integration.

## 🏗️ Tech Stack Selection

### **Core Framework**
- **Spring Boot 3.2.1**: 
  - Mature, enterprise-grade framework
  - Excellent ecosystem and community support
  - Built-in security, validation, and testing capabilities
  - Easy deployment and configuration

### **Programming Language**
- **Java 21 LTS**:
  - Long-term support version
  - Excellent tooling and IDE support
  - Strong type safety and performance
  - Industry standard for enterprise applications

### **Database**
- **H2 In-Memory Database**:
  - Perfect for prototyping and testing
  - Zero configuration required
  - Built-in web console for debugging
  - Easy to reset and populate with sample data

### **Security**
- **JWT (JSON Web Tokens)**:
  - Stateless authentication
  - Industry standard for API authentication
  - Scalable across multiple services
  - Built-in expiration and validation

### **Documentation**
- **OpenAPI 3 / Swagger**:
  - Interactive API documentation
  - Automatic schema generation
  - Built-in testing interface
  - Industry standard for REST APIs

## 🎯 Business Requirements Mapping

### **Core Features**
| Requirement | Implementation | Endpoint |
|-------------|----------------|----------|
| User Registration/Login | JWT-based auth with validation | POST /api/register, /api/login |
| Request a Ride | Location names + ride type selection | POST /api/rides/request |
| View Ride Status | Status flow with real-time updates | GET /api/rides/{id} |
| Ride History | User's complete ride history | GET /api/rides/history |
| Driver Accept/Reject | Driver workflow management | POST /api/rides/{id}/accept |

### **Data Entities**

#### **User Entity**
```java
User {
    Long id;                    // Primary key
    String name;               // User's display name
    String email;              // Login credential (unique)
    String password;           // BCrypt encrypted
    UserType userType;         // PASSENGER or DRIVER
    AvailabilityStatus status; // For drivers: AVAILABLE/UNAVAILABLE/ON_RIDE
    LocalDateTime createdAt;   // Registration timestamp
}
```

#### **Ride Entity**
```java
Ride {
    Long id;                   // Primary key
    User passenger;            // Requesting passenger
    User driver;               // Assigned driver (nullable)
    String pickupLocation;     // Pickup point name
    String dropLocation;       // Destination name
    RideType rideType;         // CAR, BIKE, RICKSHAW
    RideStatus status;         // Current ride status
    LocalDateTime createdAt;   // Request timestamp
}
```

### **Business Rules**

#### **Ride Status Flow**
```
REQUESTED → ACCEPTED → IN_PROGRESS → COMPLETED
    ↓
REJECTED/CANCELLED
```

#### **User Permissions**
- **Passengers**: Can request rides, view history, cancel rides
- **Drivers**: Can view available requests, accept/reject, update status

#### **Data Validation**
- Email format validation
- Password minimum length (6 characters)
- Required fields validation
- Enum value validation

## 🔒 Security Architecture

### **Authentication Flow**
1. User registers with email/password
2. Password encrypted with BCrypt
3. Login returns JWT token with user info
4. Token required for all protected endpoints
5. Token validated on each request

### **Authorization**
- Role-based access control (RBAC)
- Endpoint-level security annotations
- User context injection in services
- Resource ownership validation

## 📊 Database Schema

### **Entity Relationship Diagram**
```
User (1) ----< Ride (passenger)
User (1) ----< Ride (driver) [nullable]

User:
- id (PK)
- name
- email (unique)
- password
- user_type
- availability_status
- created_at

Ride:
- id (PK)
- passenger_id (FK -> User.id)
- driver_id (FK -> User.id, nullable)
- pickup_location
- drop_location
- ride_type
- status
- created_at
```

### **Sample Data**
```sql
-- Pre-loaded users for testing
INSERT INTO users VALUES 
  (1, 'John Doe', 'john.doe@example.com', '$2a$...', 'PASSENGER', NULL),
  (2, 'Bob Wilson', 'bob.wilson@example.com', '$2a$...', 'DRIVER', 'AVAILABLE');

-- Sample ride data
INSERT INTO rides VALUES
  (1, 1, 2, 'Airport', 'Downtown', 'CAR', 'COMPLETED', NOW());
```

## 🚀 API Design

### **RESTful Principles**
- Proper HTTP methods (GET, POST, PUT, DELETE)
- Meaningful HTTP status codes
- Resource-based URL structure
- JSON request/response format

### **Error Handling**
```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid status transition",
  "path": "/api/rides/5/status",
  "timestamp": "2025-07-03T22:21:00.994Z",
  "validationErrors": null
}
```

### **Response Format**
```json
{
  "id": 1,
  "name": "John Doe",
  "email": "john.doe@example.com",
  "userType": "PASSENGER",
  "availabilityStatus": null,
  "createdAt": "2025-07-03T17:01:03.282Z"
}
```

## 🧪 Testing Strategy

### **Unit Tests**
- Service layer business logic
- Validation scenarios
- Error handling paths
- Mockito for dependencies

### **Integration Tests**
- End-to-end API workflows
- Authentication flows
- Database interactions
- Spring Boot Test slices

### **Manual Testing**
- Swagger UI for interactive testing
- REST Client file for step-by-step testing
- PowerShell scripts for environment setup

## 🏃‍♂️ Development Workflow

### **Environment Setup**
1. Java 21 LTS installation
2. Maven wrapper for dependency management
3. VS Code with Spring Boot extensions
4. PowerShell scripts for easy startup

### **Development Scripts**
```powershell
.\run-app.ps1      # Start the application
.\run-tests.ps1    # Run all tests
```

### **API Testing**
1. Swagger UI: `http://localhost:8080/swagger-ui.html`
2. H2 Console: `http://localhost:8080/h2-console`
3. REST Client: `api-test.http` file in VS Code

## 🎉 Project Achievements

### **Requirements Fulfilled**
✅ User Registration/Login with JWT
✅ Ride Request with location and type selection
✅ Ride Status tracking with full workflow
✅ Ride History for users
✅ Driver Accept/Reject functionality
✅ Comprehensive API documentation

### **Additional Features**
✅ Role-based access control
✅ Driver availability management
✅ Data validation and error handling
✅ Sample data for testing
✅ Developer-friendly setup scripts
✅ Professional API documentation
✅ Comprehensive test coverage

## 🔮 Future Enhancements
- Real-time notifications (WebSocket)
- GPS integration for location tracking
- Ride fare calculation
- Driver ratings and reviews
- Real-time ride tracking
- Mobile app frontend
- Payment integration
- Admin dashboard

---

**Created by**: Mini Ride Booking System Team
**Date**: July 3, 2025
**Version**: 1.0.0
