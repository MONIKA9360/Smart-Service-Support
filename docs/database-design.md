# Database Design — Smart Service & Support Management System

> **Status:** Phase 0 — Schema defined in `db/schema.sql`. ER diagram to be added after Phase 2 entity implementation.

## Core Tables

| Table | Description | Key Relationships |
|-------|-------------|-------------------|
| `users` | Shared user account for all roles | Parent of `customers`, `employees` |
| `roles` | ROLE_ADMIN, ROLE_AGENT, ROLE_CUSTOMER | M:N with `users` via `user_roles` |
| `user_roles` | Join table for user-role assignment | FK → users, FK → roles |
| `customers` | Extended customer profile | 1:1 FK → users |
| `employees` | Extended agent/employee profile | 1:1 FK → users |
| `services` | Available support services | Referenced by tickets |
| `tickets` | Core ticket entity | FK → customers, services, employees |
| `ticket_comments` | Conversation thread per ticket | FK → tickets, users |
| `ticket_attachments` | File metadata per ticket | FK → tickets |
| `ticket_status_history` | Audit trail of status changes | FK → tickets, users |
| `feedback` | Customer satisfaction rating | FK → tickets, customers |
| `notifications` | In-app notifications | FK → users |
| `activity_logs` | Admin-visible audit log | FK → users |

## Ticket Status Workflow

```
OPEN → ASSIGNED → IN_PROGRESS → RESOLVED → CLOSED
                                     ↓
                                  REOPENED → IN_PROGRESS
```

## Ticket Number Format

`SR-{YEAR}-{SEQUENCE}`  
Example: `SR-2026-00001`

Sequence is zero-padded to 5 digits, scoped per year.

## ER Diagram

> 📌 Preliminary — formal ER diagram (MySQL Workbench .mwb file) to be added in Phase 2.

```
users (1) ──────────────── (N) user_roles (N) ──── (1) roles
users (1) ──────────── (0..1) customers
users (1) ──────────── (0..1) employees
customers (1) ──────── (N) tickets
employees (0..1) ────── (N) tickets
services  (1) ──────── (N) tickets
tickets   (1) ──────── (N) ticket_comments
tickets   (1) ──────── (N) ticket_attachments
tickets   (1) ──────── (N) ticket_status_history
tickets   (1) ──────── (0..1) feedback
users     (1) ──────── (N) notifications
users     (1) ──────── (N) activity_logs
```

## Indexes

| Table | Index | Purpose |
|-------|-------|---------|
| `users` | `uk_user_email`, `uk_user_username` | Unique login lookup |
| `tickets` | `idx_ticket_status`, `idx_ticket_priority`, `idx_ticket_customer` | Frequent filter queries |
| `notifications` | `idx_notification_user_read` | Efficient unread count |
| `activity_logs` | `idx_log_user`, `idx_log_created_at` | Admin log browsing |
