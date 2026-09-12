# API Documentation — Smart Service & Support Management System

> **Phase 2 Status:** Authentication and Role-Based Access Control (RBAC) are fully implemented and verified with Spring Security 6, JWT (HMAC-SHA256), and BCrypt.

---

## 1. Base Configuration

- **Base URL:** `http://localhost:8080/api`
- **Swagger / OpenAPI UI:** `http://localhost:8080/swagger-ui/index.html`
- **OpenAPI JSON Spec:** `http://localhost:8080/v3/api-docs`

### Security Scheme (Swagger)
- **Type:** HTTP Bearer (JWT)
- **Format:** `Authorization: Bearer <token>`

---

## 2. Standard Response Format

### Success Response Envelope
```json
{
  "success": true,
  "message": "Operation successful",
  "data": { ... }
}
```

### Error Response Envelope
```json
{
  "success": false,
  "message": "Detailed error message",
  "errorCode": "VALIDATION_FAILED",
  "details": {
    "email": "Email must be valid",
    "password": "Password is required"
  }
}
```

---

## 3. Authentication Endpoints (Phase 2 Implemented)

### 3.1 Public Customer Registration
- **Method / Path:** `POST /api/auth/register`
- **Access:** Public (no authentication required)
- **Role Assigned:** Automatically assigns `ROLE_CUSTOMER`. Ignores any client role override for security.
- **Transactional:** Creates `User` and `Customer` profile (`CUST-xxxxx`) atomically.

**Request Body:**
```json
{
  "firstName": "Alice",
  "lastName": "Johnson",
  "email": "alice@example.com",
  "phone": "555-0199",
  "password": "Password@123"
}
```

**Response (201 Created):**
```json
{
  "success": true,
  "message": "Customer registered successfully",
  "data": {
    "id": 6,
    "firstName": "Alice",
    "lastName": "Johnson",
    "email": "alice@example.com",
    "phone": "555-0199",
    "roleId": 3,
    "roleName": "ROLE_CUSTOMER",
    "isActive": true,
    "createdAt": "2026-09-12T16:11:04",
    "updatedAt": "2026-09-12T16:11:04"
  }
}
```

### 3.2 User Login
- **Method / Path:** `POST /api/auth/login`
- **Access:** Public
- **Validation:** BCrypt password verification and active status check.
- **Error:** Returns `401 Unauthorized` for invalid credentials or inactive accounts.

**Request Body:**
```json
{
  "email": "admin@example.com",
  "password": "Admin@123"
}
```

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
    "tokenType": "Bearer",
    "expiresIn": 900,
    "user": {
      "id": 1,
      "firstName": "Admin",
      "lastName": "User",
      "email": "admin@example.com",
      "phone": "1234567890",
      "roleId": 1,
      "roleName": "ROLE_ADMIN",
      "isActive": true
    }
  }
}
```

### 3.3 Get Current User Profile
- **Method / Path:** `GET /api/auth/me`
- **Access:** Authenticated (Requires Bearer JWT)
- **Protection:** Strictly excludes `passwordHash` and internal security credentials.

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Current user fetched successfully",
  "data": {
    "id": 1,
    "firstName": "Admin",
    "lastName": "User",
    "email": "admin@example.com",
    "phone": "1234567890",
    "roleId": 1,
    "roleName": "ROLE_ADMIN",
    "isActive": true
  }
}
```

---

## 4. Role-Based Access Control (RBAC) Mapping

| Endpoint Pattern | Allowed Roles | Authentication |
| :--- | :--- | :--- |
| `/api/auth/**` | Public (Login & Register) | None |
| `/api/auth/me` | `ROLE_ADMIN`, `ROLE_AGENT`, `ROLE_CUSTOMER` | Bearer JWT |
| `/api/admin/**` | `ROLE_ADMIN` | Bearer JWT |
| `/api/agent/**` | `ROLE_AGENT` | Bearer JWT |
| `/api/customer/**` | `ROLE_CUSTOMER` | Bearer JWT |
| `/swagger-ui/**`, `/v3/api-docs/**` | Public | None |
| `/api/actuator/health` | Public | None |
