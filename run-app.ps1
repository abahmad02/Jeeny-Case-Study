# Mini Ride Booking System - Development Environment Setup
# This script sets up the correct Java version and environment variables

Write-Host " Setting up Java 21 environment..." -ForegroundColor Green

# Set Java 21 as the active Java version
$env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-21.0.7.6-hotspot"
$env:PATH = "$env:JAVA_HOME\bin;$env:PATH"

# Verify Java version
Write-Host " Java version:" -ForegroundColor Green
java -version

# Run the application using Maven wrapper
Write-Host " Starting Spring Boot application..." -ForegroundColor Yellow
Write-Host " Once started, the application will be available at:" -ForegroundColor Cyan
Write-Host "   REST API: http://localhost:8080" -ForegroundColor White
Write-Host "   Swagger UI: http://localhost:8080/swagger-ui.html" -ForegroundColor White
Write-Host ""
Write-Host "To test the API, you can use:" -ForegroundColor Cyan
Write-Host "   The REST Client file: api-test.http in VS Code" -ForegroundColor White
Write-Host "   Swagger UI in your browser" -ForegroundColor White
Write-Host ""

# Run the application
& .\mvnw.cmd spring-boot:run
