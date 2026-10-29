# 🌍 AI Travel Planner — Backend

### AI-Powered Personalized Travel Planning | Spring Boot | PostgreSQL | JWT Authentication

**Project Status:** 🟡 Minimum Viable Product (MVP)

**Live Application:** https://ai-travel-planner-frontend-theta.vercel.app

**Frontend Repository:** https://github.com/SkAfreen/ai-travel-planner-frontend

---

## 📌 Project Overview

AI Travel Planner is a full-stack, AI-powered travel planning application designed to simplify the process of creating personalized travel itineraries.

Planning a trip often requires researching destinations, identifying activities, estimating expenses, and organizing a day-by-day schedule.

AI Travel Planner aims to simplify this process by generating structured travel itineraries based on user preferences, trip duration, destination, and budget.

The backend is developed using **Java and Spring Boot**, with **PostgreSQL** for persistent data storage and an external AI service for itinerary generation.

The application also provides secure user authentication, email verification, travel preference management, and trip history.

---

## 🚧 Project Status — MVP

> **Important: This application is currently a Minimum Viable Product (MVP) and does not represent the final product.**

The current version demonstrates the core functionality and technical architecture of an AI-powered travel planning platform.

It includes working authentication, personalized itinerary generation, travel preference management, and persistent trip storage.

The application is intended for further development and refinement.

Potential future improvements include:

* Enhanced itinerary customization.
* Improved destination discovery.
* More detailed travel expense estimation.
* Integration with external travel and booking services.
* Additional user experience improvements.
* Performance optimization and scalability enhancements.
* Expanded automated testing and security hardening.

These are potential development directions, **not features currently claimed as implemented**.

---

## ✨ Key Features

### 1. User Registration and Authentication

* User registration.
* Secure login.
* JWT-based authentication.
* Password hashing using BCrypt.
* Email verification during registration.
* Protection of authenticated API endpoints.

### 2. Personalized Travel Preferences

Users can manage travel preferences such as:

* Travel style.
* Preferred activities.
* Daily travel budget.
* Number of travelers.

These preferences help personalize AI-generated itineraries.

### 3. AI-Powered Itinerary Generation

The application integrates an external AI service to generate personalized travel itineraries.

The generation process considers:

* Destination.
* Travel duration.
* Travel preferences.
* Budget.
* Number of travelers.

The AI returns a structured, day-by-day itinerary.

The application has been successfully tested with a **30-day personalized itinerary**.

### 4. Trip Management

Users can:

* Create trips.
* Retrieve saved trips.
* Update trip information.
* Delete trips.

Trip information is persisted in PostgreSQL.

### 5. Itinerary Management

The backend supports:

* Creating itineraries.
* Retrieving saved itineraries.
* Storing itinerary days.
* Updating individual itinerary days.

Saved trips and itineraries remain available across login sessions.

### 6. Destination and Activity Management

The application includes APIs for managing destination information and associated travel activities.

### 7. API Documentation

The backend includes OpenAPI/Swagger configuration to support API documentation and testing.

---

## 🛠️ Technology Stack

| Category             | Technology                  |
| -------------------- | --------------------------- |
| Programming Language | Java 21                     |
| Backend Framework    | Spring Boot 4.0.8           |
| API Architecture     | REST                        |
| Database             | PostgreSQL                  |
| ORM                  | Spring Data JPA / Hibernate |
| Authentication       | Spring Security             |
| Authorization        | JWT                         |
| Password Security    | BCrypt                      |
| AI Integration       | OpenAI Java SDK             |
| API Documentation    | OpenAPI / Swagger           |
| Build Tool           | Apache Maven                |
| Backend Deployment   | Render                      |
| Cloud Database       | Neon PostgreSQL             |
| Version Control      | Git and GitHub              |

---

## 🏗️ System Architecture

The application follows a layered backend architecture.

```text
                  USER
                   |
                   v
          NEXT.JS FRONTEND
                   |
                   | REST API Requests
                   v
          SPRING BOOT BACKEND
                   |
          +--------+---------+
          |                  |
          v                  v
     SECURITY LAYER     REST CONTROLLERS
       JWT AUTH              |
                             v
                       SERVICE LAYER
                             |
                   +---------+---------+
                   |                   |
                   v                   v
             REPOSITORIES        AI INTEGRATION
                   |                   |
                   v                   v
              POSTGRESQL          OPENAI API
```

### Backend Layers

**Controller Layer**

Receives HTTP requests and exposes REST endpoints.

**Service Layer**

Contains business logic for authentication, trips, preferences, itineraries, and AI generation.

**Repository Layer**

Provides database access through Spring Data JPA.

**Entity Layer**

Defines persistent domain objects and their database mappings.

**DTO Layer**

Defines request and response structures used for API communication.

**Security Layer**

Handles JWT authentication and access control.

**Exception Handling**

Provides centralized handling of application errors through custom exceptions and a global exception handler.

---

## 📂 Project Structure

```text
src/main/java/com/example/travelplanner/
│
├── auth/
│   ├── AuthController.java
│   ├── AuthService.java
│   ├── JwtService.java
│   └── ...
│
├── config/
│   ├── SecurityConfig.java
│   ├── JwtAuthenticationFilter.java
│   ├── OpenAIConfig.java
│   └── OpenAPIConfig.java
│
├── controller/
│   ├── TripController.java
│   ├── AIItineraryController.java
│   ├── TravelPreferenceController.java
│   ├── EmailVerificationController.java
│   └── ...
│
├── dto/
│
├── entity/
│   ├── User.java
│   ├── Trip.java
│   ├── Itinerary.java
│   ├── ItineraryDay.java
│   └── ...
│
├── repository/
│
├── service/
│   ├── AIItineraryService.java
│   ├── AIItineraryValidator.java
│   ├── ItineraryPersistenceService.java
│   └── ...
│
├── exception/
│   └── GlobalExceptionHandler.java
│
└── TravelplannerApplication.java
```

---

## 🔌 REST API Endpoints

### Authentication and User Management

| Method | Endpoint                 | Description                  |
| ------ | ------------------------ | ---------------------------- |
| POST   | `/api/users`             | Register a user              |
| POST   | `/api/auth/login`        | Authenticate a user          |
| GET    | `/api/auth/verify-email` | Verify a user's email        |
| GET    | `/api/profile`           | Access authenticated profile |

### Travel Preferences

| Method | Endpoint                  | Description          |
| ------ | ------------------------- | -------------------- |
| POST   | `/api/travel-preferences` | Create preferences   |
| GET    | `/api/travel-preferences` | Retrieve preferences |
| PUT    | `/api/travel-preferences` | Update preferences   |
| DELETE | `/api/travel-preferences` | Delete preferences   |

### Trips

| Method | Endpoint              | Description    |
| ------ | --------------------- | -------------- |
| POST   | `/api/trips`          | Create a trip  |
| GET    | `/api/trips`          | Retrieve trips |
| PUT    | `/api/trips/{tripId}` | Update a trip  |
| DELETE | `/api/trips/{tripId}` | Delete a trip  |

### AI Itinerary Generation

| Method | Endpoint                          | Description              |
| ------ | --------------------------------- | ------------------------ |
| POST   | `/api/ai/trips/{tripId}/generate` | Generate an AI itinerary |

### Itineraries

| Method | Endpoint                        | Description           |
| ------ | ------------------------------- | --------------------- |
| POST   | `/api/trips/{tripId}/itinerary` | Create an itinerary   |
| GET    | `/api/trips/{tripId}/itinerary` | Retrieve an itinerary |

### Itinerary Days

| Method | Endpoint                                     | Description             |
| ------ | -------------------------------------------- | ----------------------- |
| POST   | `/api/trips/{tripId}/itinerary/days`         | Add an itinerary day    |
| GET    | `/api/trips/{tripId}/itinerary/days`         | Retrieve itinerary days |
| PUT    | `/api/trips/{tripId}/itinerary/days/{dayId}` | Update an itinerary day |

### Destinations and Activities

| Method | Endpoint                                       | Description           |
| ------ | ---------------------------------------------- | --------------------- |
| POST   | `/api/destinations`                            | Create a destination  |
| GET    | `/api/destinations`                            | Retrieve destinations |
| POST   | `/api/destinations/{destinationId}/activities` | Create an activity    |
| GET    | `/api/destinations/{destinationId}/activities` | Retrieve activities   |

### Health Check

| Method | Endpoint      | Description              |
| ------ | ------------- | ------------------------ |
| GET    | `/api/health` | Check application health |

---

## 🤖 AI Itinerary Generation Workflow

The AI itinerary generation process follows these steps:

**Step 1 — Trip Creation**

The user provides trip details, including destination, travel dates, number of travelers, and budget.

**Step 2 — Database Storage**

The trip information is stored in PostgreSQL.

**Step 3 — Travel Preference Retrieval**

The backend retrieves the user's saved travel preferences.

**Step 4 — AI Request**

The backend prepares an AI request using the relevant trip information and travel preferences.

**Step 5 — AI Response**

The external AI service generates a structured travel itinerary.

**Step 6 — Response Processing**

The backend processes and validates the generated itinerary.

**Step 7 — Persistence**

The itinerary and its day-by-day information are saved to the database.

**Step 8 — Frontend Display**

The generated itinerary is returned to the frontend for presentation to the user.

---

## 🔐 Security Implementation

The application incorporates several security mechanisms.

### JWT Authentication

JWT tokens are used to authenticate requests to protected backend endpoints.

### Password Hashing

Passwords are hashed using BCrypt rather than stored in plaintext.

### Email Verification

New users must verify their email address as part of the registration process.

### Protected API Endpoints

Spring Security is used to enforce authentication requirements.

### Centralized Exception Handling

Custom exceptions and a global exception handler support consistent API error handling.

**Security note:** These mechanisms form part of the MVP's security implementation. They should not be interpreted as a claim that the application has undergone a comprehensive independent security audit.

---

## ☁️ Deployment Architecture

The MVP is deployed using cloud services.

```text
               USER
                |
                v
         VERCEL FRONTEND
           (Next.js)
                |
                v
          RENDER BACKEND
          (Spring Boot)
                |
                +----------------+
                |                |
                v                v
          NEON POSTGRESQL    EXTERNAL AI API
```

| Component   | Platform        |
| ----------- | --------------- |
| Frontend    | Vercel          |
| Backend     | Render          |
| Database    | Neon PostgreSQL |
| Source Code | GitHub          |

---

## 🚀 Live Demo

**Try the MVP:**

https://ai-travel-planner-frontend-theta.vercel.app

The application demonstrates:

* User registration and email verification.
* Secure login.
* Travel preference management.
* Trip creation.
* AI-powered itinerary generation.
* Dashboard and saved itinerary retrieval.

**Note:** This is a publicly accessible MVP demonstration, not the final commercial product.

---

## 🧪 Testing and Validation

The following workflows have been manually tested in the deployed application:

| Test Scenario                      | Result |
| ---------------------------------- | ------ |
| New user registration              | Passed |
| Duplicate email validation         | Passed |
| Email verification                 | Passed |
| User login                         | Passed |
| Travel preference updates          | Passed |
| AI itinerary generation            | Passed |
| Personalized itinerary generation  | Passed |
| 30-day itinerary generation        | Passed |
| Dashboard access                   | Passed |
| Trip and itinerary persistence     | Passed |
| Post-logout dashboard access check | Passed |

These results represent functional testing of the MVP and do not imply exhaustive automated, load, penetration, or security testing.

---

## 🗺️ Future Development

Potential improvements for future versions include:

* Additional travel service integrations.
* More advanced budget optimization.
* Interactive maps and location-based recommendations.
* Improved itinerary editing.
* Expanded destination and activity recommendations.
* Better performance monitoring.
* More comprehensive automated testing.
* Scalability and reliability improvements.

The final product may differ significantly from this MVP as development progresses.

---

## 👩‍💻 Development and Technical Contribution

This project was developed as a full-stack AI application, covering:

* Backend REST API development using Java and Spring Boot.
* Database design and persistence using PostgreSQL.
* Authentication and authorization using Spring Security and JWT.
* AI service integration.
* Frontend integration with backend APIs.
* Email verification functionality.
* Cloud deployment and production testing.

AI-assisted development tools were used during implementation.

The project demonstrates practical experience building and deploying an end-to-end AI-powered web application.

---

## 📄 Project Disclaimer

**AI Travel Planner is an MVP developed for demonstration, learning, and portfolio purposes.**

It is not presented as a finalized commercial travel planning platform.

The application currently demonstrates its core technical capabilities. Additional functionality, testing, refinement, and operational improvements may be required before broader commercial use.

---

**AI Travel Planner — MVP Version 1.0**

*Built with Java, Spring Boot, PostgreSQL, Next.js, and AI integration.*
