# Architecture — Smart Service & Support Management System

## 1. Overview

The system follows a multi-tier, clean enterprise architecture:

```
┌─────────────────────────────────────────────────────────┐
│                    Presentation Tier                    │
│          React 18 + TypeScript + Vite + Tailwind        │
└────────────────────────────┬────────────────────────────┘
                             │ HTTPS REST / JSON
┌────────────────────────────▼────────────────────────────┐
│                        API Tier                         │
│           Spring Boot 3 REST Controllers & DTOs         │
└────────────────────────────┬────────────────────────────┘
                             │
┌────────────────────────────▼────────────────────────────┐
│                      Service Tier                       │
│    Spring Service Classes (Business Logic & Validation) │
└────────────────────────────┬────────────────────────────┘
                             │
┌────────────────────────────▼────────────────────────────┐
│                    Persistence Tier                     │
│      Spring Data JPA + Hibernate 6 ORM + MapStruct      │
└────────────────────────────┬────────────────────────────┘
                             │ JDBC (HikariCP)
┌────────────────────────────▼────────────────────────────┐
│                      Database Tier                      │
│                    MySQL 8+ Database                    │
└─────────────────────────────────────────────────────────┘
```

---

## 2. Layer Responsibilities

| Layer | Technologies | Responsibilities |
|---|---|---|
| **Presentation** | React 18, TypeScript, Vite, Tailwind CSS, React Router, React Hook Form, Yup, Recharts, Lucide Icons | Responsive UI, client-side routing, form validation, state management, dashboard analytics charts. |
| **API Layer** | Spring Web (Spring Boot 3.3), OpenAPI / Swagger, GlobalExceptionHandler | RESTful endpoint routing, DTO request mapping, Bean Validation, standardized HTTP response envelopes. |
| **Service Layer** | Spring Service components, MapStruct mappers | Core business logic, ticket assignment and state transition rules, entity-to-DTO transformations. |
| **Persistence Layer** | Spring Data JPA, Hibernate ORM, HikariCP | Repository abstractions, derived query methods, lazy loading, relational entity modeling. |
| **Database** | MySQL 8.0+ (InnoDB, UTF-8 utf8mb4) | ACID relational data storage, constraints, foreign keys, query indexes. |

---

## 3. Communication & Data Flow

```
Client Request (JSON)
       │
       ▼
[Controller] ───────────▶ [Bean Validation (@Valid)]
       │
       ▼
   [Service] ───────────▶ [MapStruct (RequestDTO ➔ Entity)]
       │
       ▼
  [Repository] ─────────▶ [MySQL 8 Database via JPA]
       │
       ▼
  [MapStruct] ──────────▶ (Entity ➔ ResponseDTO)
       │
       ▼
Client Response (JSON)
```

---

## 4. Current Status: Phase 1 Complete

- ✅ Normalized 12-table relational database schema (`db/schema.sql`).
- ✅ Seed dataset with BCrypt password hashes (`db/seed.sql`).
- ✅ 12 Spring Data JPA entities with lifecycle enums (`TicketPriority`, `TicketStatus`).
- ✅ 12 Spring Data JPA repositories with verified derived-query property paths.
- ✅ Request & Response DTOs with Jakarta Bean Validation.
- ✅ MapStruct 1.5 mappers for entity-DTO transformations.
- ✅ Java 17 compatibility verified across Maven compilation and test suite.
- ⏳ Authentication, JWT filter, and Controller endpoints will be implemented in Phase 2.
