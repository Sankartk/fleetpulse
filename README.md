# FleetPulse 🚛

**Fleet Operations & Predictive Maintenance Intelligence Platform**

A full-stack enterprise Java application for managing a vehicle fleet — tracking maintenance schedules, telematics data, driver assignments, and operational alerts — with a real-time operations dashboard.

[![Java](https://img.shields.io/badge/Java-21-orange)](https://openjdk.org/projects/jdk/21/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2-brightgreen)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue)](https://www.postgresql.org/)
[![Flyway](https://img.shields.io/badge/Flyway-migrations-red)](https://flywaydb.org/)

---

## Features

| Capability | Detail |
|---|---|
| **Vehicle Registry** | Register, update, retire vehicles — CRUD with validation |
| **Driver Management** | Assign/unassign drivers; track license expiry |
| **Maintenance Scheduling** | Schedule, track, and complete maintenance tasks |
| **Predictive Alerts** | Scheduler auto-marks overdue tasks and generates MAINTENANCE_DUE alerts |
| **Telematics Ingestion** | Ingest mileage, fuel level, engine hours via REST |
| **Operations Dashboard** | Dark-mode Thymeleaf + Chart.js dashboard with KPI cards & trend charts |
| **REST API** | Full CRUD across all resources, documented with OpenAPI 3 / Swagger UI |
| **Observability** | Spring Actuator health/metrics endpoints |

---

## Tech Stack

- **Java 21** — Records (DTOs), text blocks (JPQL), pattern matching
- **Spring Boot 3.2** — Web MVC, Data JPA, Validation, Actuator, Scheduling
- **Hibernate / JPA** — Entity modeling, lazy loading, JPQL queries
- **Flyway** — Versioned SQL migrations (V1 schema + V2 seed data)
- **H2** (default) / **PostgreSQL** (production profile)
- **Thymeleaf + Bootstrap 5 + Chart.js** — Server-rendered dashboard
- **springdoc-openapi** — Auto-generated Swagger UI
- **Lombok** — Boilerplate reduction
- **JUnit 5 + Mockito + MockMvc** — Unit & slice tests
- **Docker Compose** — PostgreSQL + app containerisation

---

## Architecture

```
com.fleetpulse/
├── domain/
│   ├── enums/          # VehicleStatus, MaintenanceType, AlertSeverity ...
│   ├── model/          # JPA entities: Vehicle, Driver, MaintenanceRecord, Alert ...
│   └── repository/     # Spring Data JPA repositories
├── service/            # Service interfaces (SOLID — depend on abstractions)
│   └── impl/           # Service implementations
├── web/
│   ├── api/            # REST controllers  → /api/**
│   ├── view/           # Thymeleaf controller → /, /vehicles
│   └── dto/            # Java 21 records (request/response DTOs)
├── exception/          # GlobalExceptionHandler, domain exceptions
├── config/             # OpenApiConfig
└── scheduler/          # MaintenanceAlertScheduler (@Scheduled)
```

---

## Running Locally (H2 — no setup needed)

**Prerequisites:** Java 21, Maven 3.9+

```bash
git clone https://github.com/Sankartk/fleetpulse.git
cd fleetpulse
mvn spring-boot:run
```

| URL | Description |
|---|---|
| `http://localhost:8080/` | Operations Dashboard |
| `http://localhost:8080/vehicles` | Fleet Vehicles table |
| `http://localhost:8080/swagger-ui.html` | Swagger API Explorer |
| `http://localhost:8080/h2-console` | H2 database console |
| `http://localhost:8080/actuator/health` | Health endpoint |

> **H2 Console credentials:** JDBC URL `jdbc:h2:mem:fleetpulse` · User `sa` · Password *(empty)*

---

## Running with PostgreSQL (Docker)

```bash
# Build the JAR first
mvn clean package -DskipTests

# Start PostgreSQL + app
docker-compose up --build
```

---

## Running Tests

```bash
mvn test
```

Tests cover: `VehicleService` (unit), `MaintenanceService` (unit), `VehicleApiController` (MockMvc slice), Spring context load.

---

## API Overview

```
GET    /api/vehicles                   List all vehicles (optional ?status= filter)
POST   /api/vehicles                   Register a vehicle
GET    /api/vehicles/{id}              Get vehicle by ID
PUT    /api/vehicles/{id}              Update vehicle
DELETE /api/vehicles/{id}              Delete vehicle
PATCH  /api/vehicles/{id}/assign-driver/{driverId}
PATCH  /api/vehicles/{id}/unassign-driver

GET    /api/drivers                    List all drivers
GET    /api/drivers/available          List unassigned active drivers
POST   /api/drivers                    Register a driver

GET    /api/maintenance                All maintenance records
GET    /api/maintenance/overdue        Overdue records
GET    /api/maintenance/upcoming       Next 30 days
POST   /api/maintenance               Schedule maintenance
PATCH  /api/maintenance/{id}/complete  Mark as completed

GET    /api/alerts/unresolved          Active alerts
PATCH  /api/alerts/{id}/resolve        Resolve alert
```

Full interactive docs at `/swagger-ui.html`.

---

## Design Highlights

- **Interface-first services** — Controllers depend on `VehicleService` interface, not `VehicleServiceImpl` (Dependency Inversion)
- **DTO layer** — Java 21 `record` types as immutable request/response contracts; entities never leak to API consumers
- **Global exception handling** — `@RestControllerAdvice` maps domain exceptions to structured `ApiResponse<T>` with correct HTTP status
- **Flyway versioned migrations** — Schema evolution is traceable and reproducible across environments
- **Scheduler** — `@Scheduled` task auto-transitions overdue records and generates alerts without manual intervention
- **Transactional boundaries** — Services annotated `@Transactional(readOnly = true)` by default; write operations opt in to write transactions
