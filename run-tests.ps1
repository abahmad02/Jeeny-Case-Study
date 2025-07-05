# Mini Ride Booking System - Test Runner
# This script sets up the correct Java version and runs tests

Write-Host "🧪 Setting up Java 21 environment for testing..." -ForegroundColor Green

# Set Java 21 as the active Java version
$env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-21.0.7.6-hotspot"
$env:PATH = "$env:JAVA_HOME\bin;$env:PATH"

# Verify Java version
Write-Host "✅ Java version:" -ForegroundColor Green
java -version

Write-Host ""
Write-Host "🧪 Running tests..." -ForegroundColor Yellow

# Run tests using Maven wrapper
& .\mvnw.cmd test
