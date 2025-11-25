# Airxelerate Flight Inventory Management System

A RESTful API for managing flight inventory with JWT-based authentication and role-based authorization.

## 🚀 Features

- **JWT Authentication**: Secure token-based authentication
- **Role-Based Authorization**: Admin and User roles with different privileges
- **Flight Management**: CRUD operations for flight inventory
- **IATA Validation**: Validates carrier codes and airport codes
- **Clean Architecture**: Layered architecture with separation of concerns
- **Exception Handling**: Global exception handler with meaningful error messages
- **MySQL Database**: Persistent data storage

## 🛠️ Technology Stack

- **Java 25**
- **Spring Boot 4.0.0**
- **Spring Security 6**
- **Spring Data JPA**
- **MySQL 8**
- **JWT (JJWT 0.13.0)**
- **Lombok**
- **Maven**

## 📋 Prerequisites

- JDK 25 or higher
- Maven 3.6+
- MySQL 8.0+
- Git

## ⚙️ Setup Instructions

### 1. Clone the Repository

```bash
git clone <repository-url>
cd flightboard
```

### 2. Configure Database

Create a MySQL database:

```sql
CREATE DATABASE airxelerate_inventory;
```

Update `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/airxelerate_inventory?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
    username: your_mysql_username
    password: your_mysql_password
```

### 3. Build the Project

```bash
mvn clean install
```

### 4. Run the Application

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## 🔐 Default User Accounts

### Administrator Account
- **Username**: `admin`
- **Password**: `admin123`
- **Role**: `ADMIN`
- **Privileges**: Full access (add, retrieve, delete flights)

### User Account
- **Username**: `user`
- **Password**: `user123`
- **Role**: `USER`
- **Privileges**: Read-only access (retrieve flights)

## 📚 API Documentation

### Base URL
```
http://localhost:8080/api/v1
```

### Authentication Endpoints

#### 1. Login
```http
POST /auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin123"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Authentication successful",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "type": "Bearer",
    "username": "admin",
    "role": "ROLE_ADMIN"
  },
  "timestamp": "2025-01-15T10:30:00"
}
```

### Flight Endpoints

All flight endpoints require a valid JWT token in the Authorization header:
```
Authorization: Bearer <your-jwt-token>
```

#### 1. Create Flight (Admin Only)
```http
POST /flights
Authorization: Bearer <token>
Content-Type: application/json

{
  "carrierCode": "AA",
  "flightNumber": "1234",
  "flightDate": "2025-01-20",
  "origin": "JFK",
  "destination": "LAX"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Flight created successfully",
  "data": {
    "id": 1,
    "carrierCode": "AA",
    "flightNumber": "1234",
    "flightDate": "2025-01-20",
    "origin": "JFK",
    "destination": "LAX",
    "createdAt": "2025-01-15T10:30:00",
    "updatedAt": "2025-01-15T10:30:00"
  },
  "timestamp": "2025-01-15T10:30:00"
}
```

#### 2. Get Flight by ID
```http
GET /flights/{id}
Authorization: Bearer <token>
```

#### 3. Get All Flights
```http
GET /flights
Authorization: Bearer <token>
```

**Response:**
```json
{
  "success": true,
  "message": "Flights retrieved successfully",
  "data": [
    {
      "id": 1,
      "carrierCode": "AA",
      "flightNumber": "1234",
      "flightDate": "2025-01-20",
      "origin": "JFK",
      "destination": "LAX",
      "createdAt": "2025-01-15T10:30:00",
      "updatedAt": "2025-01-15T10:30:00"
    }
  ],
  "timestamp": "2025-01-15T10:30:00"
}
```

#### 4. Delete Flight (Admin Only)
```http
DELETE /flights/{id}
Authorization: Bearer <token>
```

## 🔒 Security

- **JWT Expiration**: 24 hours
- **Password Encryption**: BCrypt
- **Stateless Sessions**: No session storage
- **Role-Based Access Control**:
    - `ADMIN`: Can create, read, and delete flights
    - `USER`: Can only read flights

## 🏗️ Project Structure

```
src/main/java/com/airxelerate/flightboard/
├── config/          
    ├──AppConfig
│   ├── SecurityConfig.java
│   └── DataInitializer.java
├── controller/           
│   ├── AuthenticationController.java
│   └── FlightController.java
├── model/                
│       ├── User.java
│       ├── Flight.java
│       └── Role.java
├── dto/                 
│   ├── request/
        ├── RegisterRequest
│   │   ├── LoginRequest.java
│   │   └── FlightRequest.java
│   └── response/
│       ├── AuthResponse.java
│       ├── FlightResponse.java
        ├── UserResponse
│       └── ApiResponse.java
├── exception/  
    ├── DuplicateFlightEXception.java
    ├── UnauthorizedOperationExeption.java
    ├── UserAleardyExistsExpection.java
│   ├── GlobalExceptionHandler.java
│   ├── FlightNotFoundException.java
│   └── DuplicateFlightException.java
├── repository/         
│   ├── UserRepository.java
│   └── FlightRepository.java
├── security/           
│   └── jwt/
│       ├── JwtUtil.java
│       ├── JwtAuthenticationFilter.java
        ├── TokenBlacklistService
│       └── JwtAuthenticationEntryPoint.java
├── service/              
│   ├── AuthenticationService.java
│   ├── FlightService.java
    ├── UserService
│   └── CustomUserDetailsService.java
└── FlighboardApplication.java 
```

## 📥 Postman Collection

You can use this Postman collection to test all API endpoints quickly.

- **Download JSON file:** [Postman Collection](./docs/postman_collection.json)

**Import Instructions in Postman:**
1. Open Postman.
2. Click **Import** → **Upload Files**.
3. Select the downloaded `postman-collection.json`.
4. You can now test all endpoints with the pre-configured requests.

## 📊 Database Schema

### Users Table
```sql
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);
```

### Flights Table
```sql
CREATE TABLE flights (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    carrier_code VARCHAR(2) NOT NULL,
    flight_number VARCHAR(4) NOT NULL,
    flight_date DATE NOT NULL,
    origin VARCHAR(3) NOT NULL,
    destination VARCHAR(3) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    UNIQUE KEY unique_flight (carrier_code, flight_number, flight_date)
);
```

## 🔍 Validation Rules

### Flight Data
- **Carrier Code**: 2 uppercase letters (IATA format)
- **Flight Number**: Exactly 4 digits
- **Origin/Destination**: 3 uppercase letters (IATA airport codes)
- **Flight Date**: Valid date format (YYYY-MM-DD)
- **Uniqueness**: Combination of carrier code, flight number, and date must be unique

## 🐛 Error Handling

The API returns standardized error responses:

```json
{
  "success": false,
  "message": "Error description",
  "timestamp": "2025-01-15T10:30:00"
}
```

Common HTTP Status Codes:
- `200 OK`: Successful operation
- `201 Created`: Resource created successfully
- `400 Bad Request`: Validation error
- `401 Unauthorized`: Authentication required or failed
- `403 Forbidden`: Insufficient privileges
- `404 Not Found`: Resource not found
- `409 Conflict`: Duplicate resource
- `500 Internal Server Error`: Unexpected error

## 👥 Contributors

- Ahmed Laghrissi