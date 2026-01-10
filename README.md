# Devices API

## Overview
**Devices API** is a simple and scalable RESTful service for managing devices. It allows users to **create, read, update, and delete devices**, as well as filter them by brand or state. This application is designed to showcase modern backend development practices with **Java 21**, **Spring Boot 4**, and **PostgreSQL**, and is ready to run in **Docker containers** for easy deployment.

---

## Key Features
- **Create a device** with name, brand, and state.
- **Retrieve device details** by ID.
- **List all devices** or filter by brand or state.
- **Update device information** with rules for devices currently in use.
- **Delete devices**, with safeguards to prevent deletion of devices in use.
- **OpenAPI documentation** available for easy exploration of endpoints.

---

## Getting Started

### Prerequisites
- [Docker](https://www.docker.com/get-started) and [Docker Compose](https://docs.docker.com/compose/) installed.
- Optionally, Maven installed if you want to build locally.

---

### Running the Application
The simplest way to run the application is using **Docker Compose**, which sets up both the API and the PostgreSQL database:

``` bash
docker-compose up --build
```

- API will be available at: http://localhost:8080
- PostgreSQL database will be running at localhost:5432

To Stop 
``` bash
docker-compose down
```

Note: Docker Compose automatically creates a persistent volume for the database, so your data will not be lost on container restarts.

Exploring the API

API endpoints are documented using OpenAPI/Swagger UI.

After starting the app, open your browser at:
```
http://localhost:8080/swagger-ui.html
```
to explore all endpoints and interact with the API.

## Project Structure

The project is organized as follows:

```
DevicesAPI/
└── src/                # Application source code - Java
    └── main/           # Application source code - Java
    |    ├── controller/         # REST endpoints for managing devices
    |    ├── service/            # Business logic and validation rules
    |           └── impl        # Service Implementation
    |    ├── repository/         # Database access layer using Spring Data JPA
    |    ├── dto/                # Data Transfer Objects for requests and responses
    |            └── request
    |            └── response
    |    ├── exceptions/         # Custom exception handling classes
    |    ├── Dockerfile          # Instructions to build the application container
    |    ├── docker-compose.yml  # Orchestrates app and PostgreSQL database
    |    ├── pom.xml             # Maven build configuration
    └── test/           # Test Coverage
```

## Testing

Unit and integration tests are included for both service and controller layers.

Run tests using Maven:
``` bash
mvn clean test
```

Test coverage includes all key scenarios, including validation rules and exception handling.

## Possible Future Enhancements

- **UI Integration**
  - Develop a frontend using Angular (or React) to provide a user-friendly interface for managing devices. 
  - Integrate with the backend via REST APIs and display real-time device states.


- **Authentication & Authorization**
  - Introduce Spring Security with JWT or OAuth2. 
  - Support role-based access control (e.g., ADMIN, USER) for device management operations. 


- **Pagination of Data** 
  - Add pagination and sorting support for device listings using Spring Data.
  - Enable filtering by brand, state, or creation date.