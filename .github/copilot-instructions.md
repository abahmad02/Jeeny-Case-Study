<!-- Use this file to provide workspace-specific custom instructions to Copilot. For more details, visit https://code.visualstudio.com/docs/copilot/copilot-customization#_use-a-githubcopilotinstructionsmd-file -->

# Mini Ride Booking System - Development Guidelines

## Project Overview
This is a Spring Boot backend API for a mini ride-hailing application with user management, ride booking, and driver management functionality.

## Architecture Patterns
- Follow layered architecture: Controller → Service → Repository → Entity
- Use DTOs for data transfer between layers
- Implement proper exception handling with custom exceptions
- Use dependency injection with @Autowired or constructor injection

## Code Standards
- Use meaningful variable and method names
- Add comprehensive JavaDoc comments for public methods
- Follow Spring Boot best practices and conventions
- Implement proper validation using Bean Validation annotations
- Use enums for status and type fields

## Security Guidelines
- All protected endpoints require JWT authentication
- Implement role-based access control (PASSENGER vs DRIVER)
- Validate user permissions for ride operations
- Use BCrypt for password hashing

## Database Guidelines
- Use JPA entities with proper relationships
- Implement proper cascading and fetch strategies
- Use repository methods for data access
- Follow naming conventions for database columns

## Testing Practices
- Write unit tests for service classes using Mockito
- Create integration tests for API endpoints
- Test both positive and negative scenarios
- Mock external dependencies properly

## API Documentation
- Use OpenAPI annotations for comprehensive documentation
- Include proper response codes and descriptions
- Document all request/response schemas
- Provide example payloads

## Error Handling
- Use global exception handler for consistent error responses
- Create custom exceptions for business logic errors
- Return appropriate HTTP status codes
- Include meaningful error messages

## Specific Business Rules
- Passengers can only request rides, not accept them
- Drivers can accept/reject rides and update status
- Users can only have one active ride at a time
- Ride status follows: REQUESTED → ACCEPTED → IN_PROGRESS → COMPLETED
- Driver availability is automatically managed during ride lifecycle
