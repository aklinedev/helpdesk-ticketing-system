# Help Desk Ticketing System

A full-stack help desk ticket management application built with **Spring Boot, PostgreSQL, React, and TypeScript**.

The project is designed to model a real-world IT support workflow while providing hands-on experience with backend architecture, REST APIs, database persistence, automated testing, frontend API integration, and application design.

The application currently supports creating, retrieving, updating, and deleting help desk tickets with validation, ticket lifecycle rules, error handling, API documentation, and a React frontend that retrieves and displays ticket data from the backend.

---

## Features

### Ticket Management

- Create help desk tickets
- Retrieve all tickets
- Retrieve an individual ticket by ID
- Partially update tickets
- Delete tickets
- Track ticket priority and status
- Track creation, update, and resolution timestamps

### Ticket Lifecycle

Tickets support the following statuses:

- `OPEN`
- `IN_PROGRESS`
- `RESOLVED`
- `CLOSED`

Status transitions are validated by the backend.

Additional business rules include:

- Closed tickets cannot be modified
- Invalid status transitions are rejected
- `resolvedAt` is recorded when a ticket is resolved
- `resolvedAt` is cleared if a resolved ticket is moved back into progress
- Resolution timestamps remain preserved when appropriate

### Validation

Incoming ticket requests are validated before being processed.

Examples include:

- Required ticket titles
- Required descriptions
- Required priorities
- Maximum field lengths
- Rejection of blank values
- Rejection of update requests containing no fields

### Error Handling

The backend provides centralized exception handling with meaningful HTTP status codes.

Examples include:

- `400 Bad Request` — invalid request or validation failure
- `403 Forbidden` — attempted modification of a closed ticket
- `404 Not Found` — ticket does not exist
- `409 Conflict` — invalid ticket status transition
- `500 Internal Server Error` — unexpected server error

The React frontend converts backend error responses into a reusable `ApiError` model for consistent error handling.

---

## Tech Stack

### Backend

- Java 21
- Spring Boot
- Spring Web MVC
- Spring Data JPA
- Jakarta Validation
- PostgreSQL
- Maven
- Hibernate

### Frontend

- React
- TypeScript
- Vite
- Fetch API
- Custom React hooks

### Testing

- JUnit
- Mockito
- MockMvc
- AssertJ
- H2 test database

### API Documentation

- OpenAPI
- Swagger UI
- Springdoc

---

## Architecture

The backend follows a layered architecture:

```text
HTTP Request
     ↓
Controller
     ↓
Request DTO
     ↓
Service
     ↓
Repository
     ↓
PostgreSQL
```

Responses travel back through:

```text
Entity
  ↓
Mapper
  ↓
Response DTO
  ↓
Controller
  ↓
JSON Response
```

The frontend adds another layer on top of the REST API:

```text
React Components
      ↓
Custom Hooks
      ↓
Ticket API Layer
      ↓
Generic API Client
      ↓
HTTP / JSON
      ↓
Spring Boot REST API
      ↓
PostgreSQL
```

This separation keeps HTTP behavior, ticket-specific API operations, React state management, UI rendering, and backend business logic independent from one another.

---

## Frontend Structure

The frontend currently uses the following organization:

```text
frontend/src/
│
├── api/
│   ├── api-error.ts
│   ├── client.ts
│   └── tickets.ts
│
├── components/
│   ├── TicketCard.tsx
│   └── TicketList.tsx
│
├── hooks/
│   └── use-tickets.ts
│
├── types/
│   ├── requests/
│   │   ├── create-ticket.ts
│   │   └── update-ticket.ts
│   │
│   ├── responses/
│   │   └── error-response.ts
│   │
│   └── ticket.ts
│
├── App.tsx
├── main.tsx
└── index.css
```

### Frontend Data Flow

```text
TicketList
    ↓
useTickets()
    ↓
getTickets()
    ↓
apiRequest<T>()
    ↓
Vite development proxy
    ↓
Spring Boot
```

The generic API client handles:

- JSON requests and responses
- HTTP failures
- Backend error responses
- Network failures
- `204 No Content` responses
- Conversion of API errors into `ApiError` objects

---

## REST API

Base path:

```text
/api/tickets
```

### Get All Tickets

```http
GET /api/tickets
```

Returns all help desk tickets.

### Get Ticket by ID

```http
GET /api/tickets/{id}
```

Returns a single ticket using its unique ID.

### Create Ticket

```http
POST /api/tickets
```

Example request:

```json
{
  "title": "Warehouse printer offline",
  "description": "The shipping department printer cannot be reached from any workstation.",
  "ticketPriority": "HIGH"
}
```

New tickets are automatically initialized with the `OPEN` status.

### Update Ticket

```http
PATCH /api/tickets/{id}
```

Updates one or more ticket fields.

Fields omitted from the request remain unchanged.

Example:

```json
{
  "ticketStatus": "IN_PROGRESS"
}
```

### Delete Ticket

```http
DELETE /api/tickets/{id}
```

Successfully deleted tickets return:

```text
204 No Content
```

---

## Example Ticket Response

```json
{
  "id": 1,
  "title": "Warehouse printer offline",
  "description": "The shipping department printer cannot be reached from any workstation.",
  "ticketPriority": "HIGH",
  "ticketStatus": "OPEN",
  "createdAt": "2026-11-12T18:40:00",
  "updatedAt": "2026-11-12T18:40:00",
  "resolvedAt": null
}
```

---

## API Documentation

When the Spring Boot application is running, interactive API documentation is available through Swagger UI:

```text
http://localhost:8080/swagger-ui/index.html
```

The generated OpenAPI specification is available at:

```text
http://localhost:8080/v3/api-docs
```

Swagger documentation includes endpoint descriptions, HTTP response codes, parameters, request models, and response models.

---

## Running the Backend

### Requirements

- Java 21
- PostgreSQL
- Maven or the included Maven Wrapper

Configure the PostgreSQL connection in:

```text
src/main/resources/application.properties
```

Sensitive values such as the database password should be supplied through environment variables rather than committed to source control.

Start the backend using the Maven Wrapper:

```bash
./mvnw spring-boot:run
```

On Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

The backend runs by default at:

```text
http://localhost:8080
```

---

## Running the Frontend

Navigate to:

```text
frontend/
```

Install dependencies:

```bash
npm install
```

Start the Vite development server:

```bash
npm run dev
```

The frontend runs by default at:

```text
http://localhost:5173
```

During development, Vite proxies requests beginning with `/api` to the Spring Boot backend running on port `8080`.

Both the backend and frontend should be running when using the full application.

---

## Testing

The backend includes automated tests for multiple application layers.

Current coverage includes:

- Service CRUD behavior
- Ticket status transitions
- Closed-ticket immutability
- Resolution timestamp behavior
- DTO validation
- JPA lifecycle behavior
- Entity mapping
- Controller responses
- Invalid request handling
- Missing tickets
- Malformed JSON
- Invalid enum values
- Unexpected server errors

Tests use mocked dependencies where appropriate and H2 for isolated persistence testing.

Run the backend test suite with:

```bash
./mvnw test
```

On Windows:

```powershell
.\mvnw.cmd test
```

---

## Current Development Status

### Completed

- Spring Boot REST API
- PostgreSQL persistence
- Ticket CRUD operations
- DTO request/response architecture
- Input validation
- Ticket lifecycle rules
- Closed-ticket immutability
- Resolution timestamps
- Centralized exception handling
- Automated backend tests
- Swagger/OpenAPI documentation
- React + TypeScript frontend initialization
- Generic frontend API client
- Typed ticket API operations
- React ticket-loading hook
- Backend-to-frontend API integration
- Displaying database tickets in React

### In Progress

- Frontend layout and styling
- Improved ticket presentation
- Loading, empty, and error states
- Ticket creation interface
- Ticket editing controls
- Status-change controls
- Delete-ticket interface

### Planned

- User accounts
- Technician assignment
- Authentication
- Spring Security
- Role-based authorization
- `USER`, `TECHNICIAN`, and `ADMIN` roles
- Ticket ownership
- Search and filtering
- Pagination
- Ticket comments/history
- Dashboard metrics
- Docker
- CI/CD
- Deployment

---

## Project Goals

This project is being developed as a portfolio project and learning environment for building production-style full-stack software.

Major areas of focus include:

- REST API design
- Layered backend architecture
- Object-oriented programming
- Database persistence
- Business-rule enforcement
- Automated testing
- Error handling
- API documentation
- Type-safe frontend development
- React state management
- Frontend/backend integration
- Authentication and authorization
- Software architecture and maintainability

---

## Repository

GitHub:

```text
https://github.com/aklinedev/helpdesk-ticketing-system
```