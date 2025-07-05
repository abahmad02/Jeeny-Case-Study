# Mini Ride Booking System Backend API

A comprehensive backend API for a mini ride-hailing application built with Java Spring Boot. This system provides user management, ride booking, and driver management functionality with JWT-based authentication.

## 🏗️ Architecture Overview

This application follows a layered architecture pattern:
- **Presentation Layer**: REST Controllers with OpenAPI documentation
- **Business Layer**: Service classes containing business logic
- **Data Layer**: JPA Repositories with H2 in-memory database
- **Security Layer**: JWT-based authentication with Spring Security

## 🚀 Features

### User Management
- **User Registration**: Register as passenger or driver with email validation
- **User Authentication**: JWT-based login system
- **User Profiles**: Manage user information and driver availability status
- **Role-based Access**: Separate permissions for passengers and drivers

### Ride Management - Passenger Flow
- **Request Ride**: Passengers can request rides with pickup/drop locations and vehicle type
- **View Ride Status**: Track ride progress through the status lifecycle
- **Ride History**: Access complete ride history
- **Cancel Ride**: Cancel rides when appropriate

### Ride Management - Driver Flow
- **View Available Requests**: See all pending ride requests
- **Accept/Reject Rides**: Manage ride acceptance with automatic availability updates
- **Update Ride Status**: Progress rides through IN_PROGRESS → COMPLETED states
- **Driver Availability**: Automatic status management (AVAILABLE, ON_RIDE, UNAVAILABLE)

### System Features
- **Comprehensive Error Handling**: Global exception handling with detailed error responses
- **API Documentation**: Interactive Swagger UI for testing endpoints
- **Data Validation**: Input validation with meaningful error messages
- **Sample Data**: Pre-loaded demo data for testing

## 🛠️ Tech Stack

- **Language**: Java 17
- **Framework**: Spring Boot 3.2.1
- **Security**: Spring Security with JWT authentication
- **Database**: H2 (in-memory)
- **Documentation**: OpenAPI 3 / Swagger UI
- **Build Tool**: Maven
- **Testing**: JUnit 5, Mockito, Spring Boot Test

## 📋 Prerequisites

- Java 17 or higher
- Visual Studio Code
- VS Code Extensions (automatically installed):
  - Extension Pack for Java
  - Spring Boot Tools
  - Maven for Java

## 🏃‍♂️ Getting Started

### 1. Open the Project in VS Code
Open the project folder in Visual Studio Code. The Java and Spring Boot extensions will automatically configure the environment.

### 2. Run the Application (Recommended Methods)

#### Method 1: Using VS Code Spring Boot Dashboard
1. Open the **Spring Boot Dashboard** panel in VS Code
2. You'll see "ride-hailing-app" listed
3. Click the ▶️ **Run** button next to the application name
4. The application will start with full debugging support

#### Method 2: Using the Main Class
1. Navigate to `src/main/java/com/jeeny/ridehailing/RideHailingApplication.java`
2. Click **"Run Java"** button that appears above the `main` method
3. Or press `F5` to run with debugging

#### Method 3: Using VS Code Tasks
1. Press `Ctrl+Shift+P` (or `Cmd+Shift+P` on Mac)
2. Type "Tasks: Run Task"
3. Select "Spring Boot Run"

#### Method 4: Using Integrated Terminal (if Maven is installed)
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### 3. Access the Application
- **API Documentation**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/api-docs
- **H2 Database Console**: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:ridehailing`
  - Username: `sa`
  - Password: `password`

### 4. Development Features
- **Live Reload**: Code changes automatically reload the application
- **Debugging**: Set breakpoints and debug directly in VS Code
- **IntelliSense**: Full Java autocompletion and error detection
- **Spring Boot Features**: Application properties validation, endpoint mapping, etc.

### 4. Access the API Documentation
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/api-docs
- **H2 Database Console**: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:ridehailing`
  - Username: `sa`
  - Password: `password`

## 💻 VS Code Development Workflow

This project is optimized for development in Visual Studio Code with the following extensions:

### Required Extensions
- Extension Pack for Java
- Spring Boot Extension Pack
- REST Client (optional, for API testing)

### Development Commands

#### Running the Application
1. **Using Spring Boot Dashboard**: 
   - Open the Spring Boot Dashboard (Ctrl+Shift+P → "Spring Boot Dashboard")
   - Click the "Run" button next to your application
   
2. **Using Command Palette**:
   - Press `Ctrl+Shift+P`
   - Type "Spring Boot: Run"
   - Select your application

3. **Using F5 Debug**:
   - Press `F5` to start debugging
   - Set breakpoints in your code for debugging

#### Running Tests
1. **Using Test Explorer**:
   - Open the Test Explorer panel
   - Click "Run All Tests" or run individual test classes

2. **Using Command Palette**:
   - Press `Ctrl+Shift+P`
   - Type "Java: Run Tests"

3. **Using CodeLens**:
   - Click "Run Test" or "Debug Test" links above test methods

#### Building the Project
1. **Using Command Palette**:
   - Press `Ctrl+Shift+P`
   - Type "Java: Build Project"

2. **Using Tasks**:
   - Press `Ctrl+Shift+P`
   - Type "Tasks: Run Task"
   - Select "Java: Build Project"

### Debugging Features
- **Breakpoints**: Click in the gutter to set breakpoints
- **Variable Inspection**: Hover over variables to see values
- **Call Stack**: View the execution path in the Debug panel
- **Hot Reload**: Changes to code will automatically reload (for most changes)

### Code Navigation
- **Go to Definition**: `F12` or `Ctrl+Click`
- **Find References**: `Shift+F12`
- **Symbol Search**: `Ctrl+T`
- **Quick Fix**: `Ctrl+.` (for import suggestions, etc.)

### API Testing
You can test the API directly from VS Code:
1. Install the "REST Client" extension
2. Create `.http` files with your requests
3. Click "Send Request" to test endpoints

Example `.http` file:
```http
### Register User
POST http://localhost:8080/api/register
Content-Type: application/json

{
  "name": "Test User",
  "email": "test@example.com",
  "password": "password123",
  "userType": "PASSENGER"
}

### Login
POST http://localhost:8080/api/login
Content-Type: application/json

{
  "email": "test@example.com",
  "password": "password123"
}
```

## 🧪 Testing

### Run Tests in VS Code (Recommended)
1. **Test Explorer**: Use the VS Code Test Explorer panel to run and debug tests
2. **Individual Tests**: Click the ▶️ button next to any test method to run it
3. **Test Classes**: Right-click on test classes and select "Run Tests"
4. **Debug Tests**: Right-click and select "Debug Tests" for breakpoint debugging

### Run Tests via Command Line (if Maven is available)
```bash
# Run unit tests
mvn test

# Run integration tests
mvn verify

# Test coverage report
mvn jacoco:report
```

### Test Coverage
Test coverage reports will be generated in `target/site/jacoco/index.html`

## 📡 API Endpoints

### Authentication Endpoints
| Method | Endpoint | Description | Authentication |
|--------|----------|-------------|----------------|
| POST | `/api/register` | Register new user | None |
| POST | `/api/login` | User login | None |

### User Management Endpoints
| Method | Endpoint | Description | Authentication |
|--------|----------|-------------|----------------|
| GET | `/api/users/{userId}` | Get user by ID | Required |
| GET | `/api/users/drivers` | Get all drivers | Required |
| GET | `/api/users/drivers/available` | Get available drivers | Required |
| PUT | `/api/users/drivers/{driverId}/availability` | Update driver availability | Required |
| GET | `/api/users/passengers` | Get all passengers | Required |

### Ride Management Endpoints
| Method | Endpoint | Description | Authentication | Allowed Users |
|--------|----------|-------------|----------------|---------------|
| POST | `/api/rides/request` | Request a ride | Required | Passengers |
| GET | `/api/rides/{rideId}` | Get ride details | Required | Ride participants |
| GET | `/api/rides/history` | Get ride history | Required | All |
| GET | `/api/rides/available` | Get available requests | Required | Drivers |
| POST | `/api/rides/{rideId}/accept` | Accept ride request | Required | Drivers |
| POST | `/api/rides/{rideId}/reject` | Reject ride request | Required | Drivers |
| PUT | `/api/rides/{rideId}/status` | Update ride status | Required | Assigned driver |
| POST | `/api/rides/{rideId}/cancel` | Cancel ride | Required | Ride participants |

## 💾 Sample Data

The application comes pre-loaded with sample data for testing:

### Sample Users
**Passengers:**
- john.doe@example.com (John Doe)
- jane.smith@example.com (Jane Smith)
- alice.johnson@example.com (Alice Johnson)

**Drivers:**
- bob.wilson@example.com (Bob Wilson) - Available
- carol.brown@example.com (Carol Brown) - On Ride
- david.lee@example.com (David Lee) - Available

**Password for all users**: `password123`

### Sample Rides
- Completed ride: Airport → City Center (Car)
- In-progress ride: Mall Road → University (Bike)
- Available requests: Train Station → Shopping Center (Rickshaw)

## 📝 Request/Response Examples

### Register User
```bash
curl -X POST http://localhost:8080/api/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john.doe@example.com",
    "password": "password123",
    "userType": "PASSENGER"
  }'
```

### Login
```bash
curl -X POST http://localhost:8080/api/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john.doe@example.com",
    "password": "password123"
  }'
```

### Request a Ride
```bash
curl -X POST http://localhost:8080/api/rides/request \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <your-jwt-token>" \
  -d '{
    "pickupLocation": "Airport",
    "dropLocation": "City Center",
    "rideType": "CAR"
  }'
```

### Accept a Ride (Driver)
```bash
curl -X POST http://localhost:8080/api/rides/{rideId}/accept \
  -H "Authorization: Bearer <driver-jwt-token>"
```

### Update Ride Status
```bash
curl -X PUT http://localhost:8080/api/rides/{rideId}/status \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <driver-jwt-token>" \
  -d '{
    "status": "IN_PROGRESS"
  }'
```

## 🎯 Business Logic & Rules

### Ride Status Lifecycle
```
REQUESTED → ACCEPTED → IN_PROGRESS → COMPLETED
    ↓           ↓           ↓
REJECTED    CANCELLED   CANCELLED
```

### User Permissions
- **Passengers**: Can request rides, view their ride history, cancel their rides
- **Drivers**: Can view available requests, accept/reject rides, update ride status, cancel accepted rides

### Driver Availability Management
- **AVAILABLE**: Can accept new rides
- **ON_RIDE**: Automatically set when accepting a ride
- **UNAVAILABLE**: Manually set, cannot accept rides

### Validation Rules
- Passengers cannot have multiple active rides
- Drivers cannot accept rides when unavailable or already on a ride
- Only assigned drivers can update ride status to IN_PROGRESS or COMPLETED
- Rides can only be cancelled if not already completed

## 🔒 Security Features

- **JWT Authentication**: Stateless authentication with configurable expiration
- **Password Encryption**: BCrypt hashing for password security
- **Role-based Authorization**: Method-level security based on user types
- **Input Validation**: Comprehensive validation with detailed error messages
- **CORS Configuration**: Configurable cross-origin resource sharing

## 🚀 Production Considerations

### Database Configuration
For production, replace H2 with a persistent database:

```properties
# PostgreSQL example
spring.datasource.url=jdbc:postgresql://localhost:5432/ridehailing
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
```

### Security Enhancements
- Use environment variables for JWT secret
- Implement refresh token mechanism
- Add rate limiting
- Enable HTTPS in production
- Implement comprehensive logging and monitoring

### Scalability Improvements
- Add caching layer (Redis)
- Implement message queues for notifications
- Add database connection pooling
- Consider microservices architecture for large scale

## 🐛 Troubleshooting

### Common Issues

**Issue**: Application fails to start in VS Code
**Solution**: 
1. Ensure Java 17+ is installed and configured in VS Code
2. Check the Java extension is properly installed
3. Reload the VS Code window (`Ctrl+Shift+P` → "Developer: Reload Window")
4. Check the "Problems" panel for any compilation errors

**Issue**: Maven commands not working
**Solution**: Use VS Code's built-in Spring Boot tools instead of command-line Maven

**Issue**: Tests not running in VS Code
**Solution**: 
1. Make sure the Test Explorer is enabled
2. Refresh the test discovery (`Ctrl+Shift+P` → "Java: Refresh Projects")
3. Check that the Java Test Runner extension is active

**Issue**: No Spring Boot Dashboard visible
**Solution**: 
1. Ensure Spring Boot Tools extension is installed and enabled
2. Open Command Palette (`Ctrl+Shift+P`) → "Spring Boot Dashboard: Open"

**Issue**: H2 Console not accessible
**Solution**: Verify `spring.h2.console.enabled=true` in application.properties

**Issue**: JWT token expired
**Solution**: Login again to get a new token (tokens expire after 24 hours)

**Issue**: Java version conflicts
**Solution**: 
1. Check VS Code Java settings (`Ctrl+,` and search for "java")
2. Ensure `java.configuration.runtimes` points to Java 17+
3. Set `java.compile.nullAnalysis.mode` to "automatic"

## 📈 Future Enhancements

- **Real-time Features**: WebSocket integration for live updates
- **Geolocation**: GPS coordinates instead of string locations
- **Payment Integration**: Payment processing and fare calculation
- **Rating System**: Driver and passenger rating system
- **Push Notifications**: Real-time notifications for ride updates
- **Admin Dashboard**: Admin interface for system management
- **Analytics**: Ride statistics and reporting features

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 📞 Support

For support and questions:
- Email: support@jeeny.com
- Documentation: Check the Swagger UI at `/swagger-ui.html`
- Issues: Create an issue in the repository

---

## 🎯 Design Decisions & Assumptions

### Assumptions Made
1. **Location Granularity**: Locations are represented as simple strings rather than GPS coordinates
2. **Single Active Ride**: Users can only have one active ride at a time
3. **Immediate Driver Assignment**: No ride matching algorithm; drivers manually accept rides
4. **No Payment Processing**: Focus on core ride management logic
5. **Simplified Driver Management**: No vehicle information or driver verification
6. **No Real-time Updates**: Status updates require API calls

### Technical Decisions
1. **H2 Database**: Chosen for simplicity and demo purposes
2. **JWT Authentication**: Stateless authentication suitable for APIs
3. **Spring Boot**: Rapid development with production-ready features
4. **OpenAPI Documentation**: Self-documenting API with interactive testing
5. **Layered Architecture**: Clear separation of concerns for maintainability

This backend provides a solid foundation for a ride-hailing application and can be extended with additional features as needed.
