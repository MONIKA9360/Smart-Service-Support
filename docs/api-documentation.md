# API Documentation — Smart Service & Support Management System

> **Status:** Phase 0 — API plan documented below. Full Swagger UI will be available at
> `http://localhost:8080/swagger-ui/index.html` once Phase 2 (Authentication) is implemented.

## Base URL

```
http://localhost:8080/api
```

## Authentication

All protected endpoints require:
```
Authorization: Bearer <JWT_TOKEN>
```

Obtain token via `POST /api/auth/login`.

## Response Format

**Success:**
```json
{
  "success": true,
  "message": "Operation successful",
  "data": { }
}
```

**Error:**
```json
{
  "success": false,
  "message": "Human-readable error",
  "errorCode": "ERROR_CODE_CONSTANT",
  "details": { }
}
```

## Planned Endpoints

### Authentication
| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| POST | `/auth/register` | Customer registration | Public |
| POST | `/auth/login` | Login, returns JWT | Public |
| GET | `/auth/me` | Get current user profile | Any |

### Customers
| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| GET | `/customers` | List all customers (paginated) | ADMIN |
| GET | `/customers/{id}` | Get customer by ID | ADMIN |
| PUT | `/customers/{id}` | Update customer | ADMIN |
| DELETE | `/customers/{id}` | Deactivate customer | ADMIN |

### Employees
| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| GET | `/employees` | List all agents | ADMIN |
| POST | `/employees` | Create employee account | ADMIN |
| PUT | `/employees/{id}` | Update employee | ADMIN |

### Services
| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| GET | `/services` | List active services | Any |
| POST | `/services` | Create service | ADMIN |
| PUT | `/services/{id}` | Update service | ADMIN |
| DELETE | `/services/{id}` | Deactivate service | ADMIN |

### Tickets
| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| GET | `/tickets` | List tickets (filtered, paginated) | Any (role-scoped) |
| POST | `/tickets` | Create new ticket | CUSTOMER |
| GET | `/tickets/{id}` | Get ticket detail | Any (row-level) |
| PUT | `/tickets/{id}` | Update ticket | ADMIN / AGENT |
| PATCH | `/tickets/{id}/status` | Change ticket status | ADMIN / AGENT |
| PATCH | `/tickets/{id}/assign` | Assign ticket to agent | ADMIN |

### Comments
| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| GET | `/tickets/{id}/comments` | Get all comments | Any (row-level) |
| POST | `/tickets/{id}/comments` | Add comment | Any (row-level) |

### Attachments
| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| POST | `/tickets/{id}/attachments` | Upload file | Any (row-level) |

### Feedback
| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| POST | `/tickets/{id}/feedback` | Submit rating | CUSTOMER |
| GET | `/feedback` | List all feedback | ADMIN |

### Notifications
| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| GET | `/notifications` | Get user notifications | Any |
| PATCH | `/notifications/{id}/read` | Mark as read | Any |

### Reports
| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| GET | `/reports/dashboard` | Dashboard metrics | ADMIN |
| GET | `/reports/tickets` | Ticket report | ADMIN |
| GET | `/reports/employees` | Employee performance | ADMIN |

### Activity Logs
| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| GET | `/logs` | Activity log (paginated) | ADMIN |
