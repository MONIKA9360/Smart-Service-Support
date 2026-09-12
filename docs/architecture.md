# Architecture — Smart Service & Support Management System

## Overview

The system uses a classic 4-tier architecture:

```
[Browser / React SPA]
       ↕  HTTPS REST (JSON)
[Spring Boot API — Port 8080]
       ↕  Spring Security / JWT
[Service Layer (Business Logic)]
       ↕  Spring Data JPA / Hibernate
[MySQL 8 Database — Port 3306]
```

## Tier Responsibilities

| Tier | Technology | Responsibility |
|------|-----------|----------------|
| **Presentation** | React 18 + TypeScript + Vite | UI rendering, routing, form validation, chart display |
| **API** | Spring Boot 3 REST Controllers | Request routing, auth filter, response formatting |
| **Business Logic** | Spring Service classes | Ticket workflow, role checks, notification triggers |
| **Persistence** | Spring Data JPA + Hibernate → MySQL 8 | Data storage, query optimisation, relationship management |

## Authentication Flow

```
Client                  Backend
  |                        |
  |-- POST /auth/login --> |
  |                        |-- Validate credentials (BCrypt)
  |                        |-- Generate JWT (HS256, 24h)
  |<-- { token: "..." } ---|
  |                        |
  |-- GET /tickets ------> |
  |   Authorization: Bearer <token>
  |                        |-- JwtAuthenticationFilter validates token
  |                        |-- Sets SecurityContext
  |                        |-- @PreAuthorize checks role
  |<-- 200 OK ------------ |
```

## Role-Based Access Control

| Role | Granted Authorities | Access Scope |
|------|---------------------|--------------|
| ROLE_ADMIN | All endpoints | System-wide |
| ROLE_AGENT | Assigned ticket endpoints | Own assigned tickets |
| ROLE_CUSTOMER | Customer endpoints | Own data only |

## Communication Pattern

- Frontend → Backend: **REST/JSON** via Axios
- Backend → Database: **JPA/Hibernate** with connection pooling (HikariCP)
- Notifications: **HTTP polling** (every 15 s) via `/api/notifications`

## Deployment Topology (Docker Compose — Development)

```
docker network: smart_support_network
┌────────────────┐   ┌─────────────────┐   ┌─────────────────┐
│  frontend:80   │──▶│  backend:8080   │──▶│   db:3306       │
│  nginx/React   │   │  Spring Boot    │   │   MySQL 8       │
└────────────────┘   └─────────────────┘   └─────────────────┘
```
