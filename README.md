# Smart Service & Support Management System

> **Final-Year Computer Science Engineering Project**  
> A production-grade, centralized customer service and support management platform.

---

## Project Description

The Smart Service & Support Management System replaces scattered phone calls, emails, and spreadsheets with one professional platform where:

- **Customers** raise and track support tickets
- **Support Agents** manage assigned tickets and update statuses
- **Administrators** oversee the entire system, manage users, and view analytics

---

## Technology Stack

| Layer | Technology |
|-------|-----------|
| Frontend | React 18, TypeScript, Vite, Tailwind CSS |
| Routing | React Router v6 |
| HTTP Client | Axios |
| Charts | Recharts |
| Forms | React Hook Form + Yup |
| Backend | Java 17, Spring Boot 3.2 |
| Security | Spring Security, JWT (JJWT), BCrypt |
| ORM | Spring Data JPA, Hibernate |
| Database | MySQL 8 |
| Build | Maven 3.9, npm 10 |
| Containers | Docker, Docker Compose |
| API Docs | Springdoc OpenAPI (Swagger UI) |

---

## Architecture

```
[React SPA — Port 5173]
        ↕ REST/JSON
[Spring Boot API — Port 8080]
        ↕ JWT Auth
[Service Layer]
        ↕ JPA/Hibernate
[MySQL 8 — Port 3306]
```

See [`docs/architecture.md`](docs/architecture.md) for full details.

---

## Project Structure

```
smart-service-support-system/
│
├── frontend/          React + TypeScript + Vite application
├── backend/           Spring Boot Maven application
├── db/                MySQL schema and seed data
│   ├── schema.sql     Table definitions, indexes, FK constraints
│   └── seed.sql       Demo data (users, services, tickets)
├── docs/              Architecture, DB design, API documentation
├── uploads/           Local file storage (gitignored contents)
├── docker-compose.yml Full stack Docker orchestration
├── .env.example       Environment variable template
└── README.md          This file
```

---

## Prerequisites

| Tool | Minimum Version |
|------|----------------|
| Docker Desktop | 24+ |
| Docker Compose | v2 (included with Docker Desktop) |
| Node.js | 18+ |
| JDK | 17 |
| Maven | 3.9+ |
| MySQL | 8.0+ (if running locally without Docker) |

---

## Quick Start (Docker Compose — Recommended)

### 1. Clone the repository

```bash
git clone <your-repo-url>
cd smart-service-support-system
```

### 2. Create environment file

```bash
cp .env.example .env
```

Edit `.env` and set at minimum:
```
DB_PASSWORD=yourpassword
MYSQL_ROOT_PASSWORD=yourrootpassword
MYSQL_PASSWORD=yourpassword
JWT_SECRET=your-64-char-secret-here
```

> Generate a JWT secret: `openssl rand -base64 64`

### 3. Start all containers

```bash
docker compose up -d --build
```

### 4. Verify containers are healthy

```bash
docker compose ps
```

All three services (`db`, `backend`, `frontend`) should show `healthy` or `running`.

---

## Access URLs

| Service | URL |
|---------|-----|
| **Frontend** | http://localhost:5173 |
| **Backend API** | http://localhost:8080/api |
| **Health Check** | http://localhost:8080/api/actuator/health |
| **Swagger UI** | http://localhost:8080/swagger-ui/index.html |
| **Health Endpoint** | http://localhost:8080/api/health-check |

---

## Local Development (Without Docker)

### Frontend

```bash
cd frontend
cp .env.example .env          # Fill in VITE_API_BASE_URL
npm install
npm run dev                   # Starts at http://localhost:5173
```

### Backend

```bash
cd backend
# Ensure MySQL is running and db is created
mvn spring-boot:run           # Starts at http://localhost:8080/api
```

### Database Setup (Local MySQL)

```sql
CREATE DATABASE smart_support;
CREATE USER 'support_user'@'localhost' IDENTIFIED BY 'yourpassword';
GRANT ALL PRIVILEGES ON smart_support.* TO 'support_user'@'localhost';
FLUSH PRIVILEGES;
```

Then run the SQL files:
```bash
mysql -u support_user -p smart_support < db/schema.sql
mysql -u support_user -p smart_support < db/seed.sql
```

---

## Environment Variables Reference

| Variable | Description | Example |
|----------|-------------|---------|
| `DB_HOST` | MySQL host | `localhost` |
| `DB_PORT` | MySQL port | `3306` |
| `DB_NAME` | Database name | `smart_support` |
| `DB_USERNAME` | DB user | `support_user` |
| `DB_PASSWORD` | DB password | `secret` |
| `MYSQL_ROOT_PASSWORD` | MySQL root password | `rootsecret` |
| `MYSQL_DATABASE` | MySQL init DB name | `smart_support` |
| `MYSQL_USER` | MySQL user to create | `support_user` |
| `MYSQL_PASSWORD` | MySQL user password | `secret` |
| `JWT_SECRET` | JWT signing secret (≥ 64 chars) | `openssl rand -base64 64` |
| `JWT_EXPIRATION_MS` | JWT expiry in ms | `86400000` (24h) |
| `VITE_API_BASE_URL` | Backend API URL for frontend | `http://localhost:8080/api` |
| `FILE_UPLOAD_DIR` | Upload directory | `./uploads` |

---

## Demo Credentials

> ⚠️ Available after Phase 2 (Authentication) is implemented.

All seed users have the password: **`Admin@123`**

| Role | Email | Username |
|------|-------|----------|
| Admin | admin@smartsupport.com | admin |
| Support Agent | alice@smartsupport.com | agent.alice |
| Support Agent | bob@smartsupport.com | agent.bob |
| Customer | john.doe@example.com | john.doe |
| Customer | jane.smith@example.com | jane.smith |

---

## Implementation Status

### ✅ Implemented (Phase 0)
- Project scaffold and folder structure
- Frontend: React + TypeScript + Vite + Tailwind CSS
- Backend: Spring Boot 3.2 + Java 17
- Database schema (`db/schema.sql`) with all tables, indexes, FK constraints
- Seed data (`db/seed.sql`) with demo users, services, and tickets
- Docker Compose (MySQL + Backend + Frontend)
- Environment variable configuration
- Landing page (professional SaaS design)
- Login / Register page shells
- MainLayout with responsive sidebar and top bar
- Placeholder pages for all future routes
- API client (Axios) with interceptor stubs
- Swagger / OpenAPI configuration
- Spring Actuator health endpoint
- Global exception handler
- Documentation (`docs/`)

### 🚧 Planned (Phases 2–15)
- Phase 2: JWT authentication, user registration, role-based login
- Phase 3: Frontend auth context, protected routes, role-aware navigation
- Phase 4: Role-aware UI shell, sidebar role filtering
- Phase 5: Service management (CRUD), customer dashboard
- Phase 6: Ticket CRUD, status workflow, agent dashboard, ticket assignment
- Phase 7: Comments, attachments, file upload
- Phase 8: Notification system (polling)
- Phase 9: Admin dashboard, analytics charts (Recharts)
- Phase 10: Feedback & rating system
- Phase 11: Reports and CSV export
- Phase 12: Audit/activity logs
- Phase 13: Testing (JUnit, MockMvc, Cypress)
- Phase 14: CI/CD (GitHub Actions), Docker hardening
- Phase 15: Final documentation, UML diagrams, screenshots

---

## API Overview

See [`docs/api-documentation.md`](docs/api-documentation.md) for the full endpoint plan.

After Phase 2, Swagger UI is available at: `http://localhost:8080/swagger-ui/index.html`

---

## Future Enhancements

- WebSocket real-time notifications (Phase 2 enhancement)
- Email notifications
- PDF report export
- AI-assisted ticket categorisation / priority recommendation
- Cloud deployment (Azure App Service / AWS Elastic Beansteak)
- Multi-language support (i18n)

---

## License

MIT — See [LICENSE](LICENSE) for details.

---

*Smart Service & Support Management System — Final-Year CS Engineering Project © 2026*
