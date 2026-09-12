# Architecture — Smart Service & Support Management System

## 1. Overview

The system follows a multi-tier, clean enterprise architecture with Spring Security 6 stateless JWT authentication and role-based access control (RBAC):

```
┌─────────────────────────────────────────────────────────┐
│                    Presentation Tier                    │
│     React 18 + TypeScript + Vite + Tailwind + AuthCtx   │
└────────────────────────────┬────────────────────────────┘
                             │ HTTPS REST / JSON (Bearer JWT)
┌────────────────────────────▼────────────────────────────┐
│                   Security & API Tier                   │
│    Spring Security 6 + JwtAuthFilter + REST Endpoints   │
└────────────────────────────┬────────────────────────────┘
                             │
┌────────────────────────────▼────────────────────────────┐
│                      Service Tier                       │
│  AuthService (BCrypt) + Business Logic & MapStruct DTOs │
└────────────────────────────┬────────────────────────────┘
                             │
┌────────────────────────────▼────────────────────────────┐
│                    Persistence Tier                     │
│      Spring Data JPA + Hibernate 6 ORM + HikariCP       │
└────────────────────────────┬────────────────────────────┘
                             │ JDBC
┌────────────────────────────▼────────────────────────────┐
│                      Database Tier                      │
│                    MySQL 8+ Database                    │
└─────────────────────────────────────────────────────────┘
```

---

## 2. Layer Responsibilities

| Layer | Technologies | Responsibilities |
|---|---|---|
| **Presentation** | React 18, TypeScript, Vite, Tailwind CSS, React Router, AuthContext, Axios Interceptor | Responsive UI, client-side routing, protected route guards, JWT session restore, login/register forms, role-tailored navigation. |
| **Security Layer** | Spring Security 6, JJWT (HMAC-SHA256), BCrypt | Stateless session management, token validation, user active-status verification, role-based endpoint authorization. |
| **API Layer** | Spring Web (Spring Boot 3.3), OpenAPI / Swagger, GlobalExceptionHandler | RESTful endpoint routing, DTO request mapping, Bean Validation, standardized HTTP response envelopes (400, 401, 403, 404, 409). |
| **Service Layer** | Spring Service components, MapStruct mappers | Core authentication and business logic, transactional customer registration with unique code generation (`CUST-xxxxx`), entity-to-DTO transformations. |
| **Persistence Layer** | Spring Data JPA, Hibernate ORM, HikariCP | Repository abstractions, derived query methods, relational entity modeling. |
| **Database** | MySQL 8.0+ (InnoDB, UTF-8 utf8mb4) | ACID relational data storage, constraints, foreign keys, query indexes. |

---

## 3. Communication & Data Flow

```
Client Request (Bearer JWT)
       │
       ▼
[JwtAuthenticationFilter] ──▶ [Validate Signature & Expiration & Active Status]
       │
       ▼
[SecurityContextHolder] ────▶ [Authorize Role (ROLE_ADMIN, ROLE_AGENT, ROLE_CUSTOMER)]
       │
       ▼
[Controller] ───────────────▶ [Bean Validation (@Valid)]
       │
       ▼
    [Service] ──────────────▶ [Transactional Business Logic & BCrypt]
       │
       ▼
   [Repository] ────────────▶ [MySQL 8 Database via JPA]
       │
       ▼
   [MapStruct] ─────────────▶ (Entity ➔ ResponseDTO [Excludes passwordHash])
       │
       ▼
Client Response (JSON)
```

---

## 4. Current Status: Phase 2 Complete

- ✅ Strict Java 17 compliance across Maven, compiler, and build configuration.
- ✅ Normalized 12-table relational database schema with verified BCrypt demo password hashes (`Admin@123`).
- ✅ Spring Security 6 stateless filter chain with `JwtAuthenticationFilter` verifying signature, expiration (15m), and account active status.
- ✅ Customer public registration strictly assigning `ROLE_CUSTOMER` and generating unique customer code (`CUST-xxxxx`) in a single database transaction.
- ✅ User login with BCrypt password verification and safe `UserResponse` (strictly excluding `passwordHash`).
- ✅ Role-Based Access Control (RBAC) enforced on backend URL matchers (`/api/admin/**`, `/api/agent/**`, `/api/customer/**`).
- ✅ Frontend `AuthContext`, Axios request/response interceptors, `ProtectedRoute` with 403 access denial handling, and dynamic role navigation.
- ✅ 33 backend tests passing with H2 test profile.
- ✅ Frontend TypeScript compilation and production build passing with 0 errors.
