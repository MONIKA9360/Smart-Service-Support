# Database Design — Smart Service & Support Management System

> **Status:** Phase 1 (Completed) — Normalized 12-Table Relational Schema with Spring Data JPA entities, repositories, and constraints matching `db/schema.sql` and `db/seed.sql`.

---

## 1. Relational Schema Overview

```
                      ┌───────────────┐
                      │     ROLES     │
                      └───────┬───────┘
                              │ 1
                              │
                              │ N
                      ┌───────▼───────┐
                      │     USERS     │
                      └───┬───────┬───┘
                          │ 1     │ 1
             ┌────────────┘       └────────────┐
             │ 1                               │ 1
     ┌───────▼───────┐                 ┌───────▼───────┐
     │   CUSTOMERS   │                 │   EMPLOYEES   │
     └───┬───────────┘                 └───┬───────────┘
         │ 1                               │ 0..1
         │                                 │
         │           ┌─────────────┐       │
         │           │  SERVICES   │       │
         │           └──────┬──────┘       │
         │                  │ 1            │
         │                  │              │
         │ N                │ N            │ N
     ┌───▼──────────────────▼──────────────▼───┐
     │                 TICKETS                 │
     └───┬─────────────┬─────────────┬─────┬───┘
         │ 1           │ 1           │ 1   │ 1
         │             │             │     │
         │ N           │ N           │ N   │ 0..1
┌────────▼───────┐ ┌───▼───────────┐ ┌─▼───▼───────────┐ ┌─────────▼────────┐
│TICKET_COMMENTS │ │TICKET_ATTACH- │ │  TICKET_STATUS_ │ │     FEEDBACK     │
│                │ │    MENTS      │ │     HISTORY     │ │                  │
└────────────────┘ └───────────────┘ └─────────────────┘ └──────────────────┘

Additional User Associations:
USERS (1) ───▶ (N) NOTIFICATIONS
USERS (1) ───▶ (N) ACTIVITY_LOGS
```

---

## 2. Table Specifications

### 2.1 `roles`
Security authority definitions.
- **`id`** `BIGINT` `AUTO_INCREMENT` `PRIMARY KEY`
- **`name`** `VARCHAR(50)` `NOT NULL` `UNIQUE` (`ROLE_ADMIN`, `ROLE_AGENT`, `ROLE_CUSTOMER`)
- **`description`** `VARCHAR(255)` `NULL`
- **`created_at`** `DATETIME` `NOT NULL` `DEFAULT CURRENT_TIMESTAMP`
- **`updated_at`** `DATETIME` `NOT NULL` `DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP`

### 2.2 `users`
Core user identity for system access.
- **`id`** `BIGINT` `AUTO_INCREMENT` `PRIMARY KEY`
- **`first_name`** `VARCHAR(50)` `NOT NULL`
- **`last_name`** `VARCHAR(50)` `NOT NULL`
- **`email`** `VARCHAR(100)` `NOT NULL` `UNIQUE` (`INDEX: idx_user_email`)
- **`password_hash`** `VARCHAR(255)` `NOT NULL` (BCrypt hash)
- **`phone`** `VARCHAR(20)` `NULL`
- **`role_id`** `BIGINT` `NOT NULL` (`FK -> roles.id`, `INDEX: idx_user_role_id`)
- **`is_active`** `TINYINT(1)` `NOT NULL` `DEFAULT 1`
- **`created_at`** `DATETIME` `NOT NULL` `DEFAULT CURRENT_TIMESTAMP`
- **`updated_at`** `DATETIME` `NOT NULL` `DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP`

### 2.3 `customers`
Customer profile linked to user account.
- **`id`** `BIGINT` `AUTO_INCREMENT` `PRIMARY KEY`
- **`user_id`** `BIGINT` `NOT NULL` `UNIQUE` (`FK -> users.id ON DELETE CASCADE`)
- **`customer_code`** `VARCHAR(50)` `NOT NULL` `UNIQUE` (`INDEX: idx_customer_code`)
- **`address`** `VARCHAR(255)` `NULL`
- **`city`** `VARCHAR(100)` `NULL`
- **`state`** `VARCHAR(100)` `NULL`
- **`postal_code`** `VARCHAR(20)` `NULL`
- **`created_at`** `DATETIME` `NOT NULL` `DEFAULT CURRENT_TIMESTAMP`
- **`updated_at`** `DATETIME` `NOT NULL` `DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP`

### 2.4 `employees`
Support agent and employee profile.
- **`id`** `BIGINT` `AUTO_INCREMENT` `PRIMARY KEY`
- **`user_id`** `BIGINT` `NOT NULL` `UNIQUE` (`FK -> users.id ON DELETE CASCADE`)
- **`employee_code`** `VARCHAR(50)` `NOT NULL` `UNIQUE` (`INDEX: idx_employee_code`)
- **`department`** `VARCHAR(100)` `NULL`
- **`designation`** `VARCHAR(100)` `NULL`
- **`created_at`** `DATETIME` `NOT NULL` `DEFAULT CURRENT_TIMESTAMP`
- **`updated_at`** `DATETIME` `NOT NULL` `DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP`

### 2.5 `services`
Catalog of support service offerings.
- **`id`** `BIGINT` `AUTO_INCREMENT` `PRIMARY KEY`
- **`service_name`** `VARCHAR(100)` `NOT NULL` `UNIQUE`
- **`description`** `TEXT` `NULL`
- **`category`** `VARCHAR(100)` `NULL` (`INDEX: idx_service_category`)
- **`is_active`** `TINYINT(1)` `NOT NULL` `DEFAULT 1` (`INDEX: idx_service_is_active`)
- **`created_at`** `DATETIME` `NOT NULL` `DEFAULT CURRENT_TIMESTAMP`
- **`updated_at`** `DATETIME` `NOT NULL` `DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP`

### 2.6 `tickets`
Central entity for service requests and support tickets.
- **`id`** `BIGINT` `AUTO_INCREMENT` `PRIMARY KEY`
- **`ticket_number`** `VARCHAR(30)` `NOT NULL` `UNIQUE` (`INDEX: idx_ticket_number`)
- **`customer_id`** `BIGINT` `NOT NULL` (`FK -> customers.id`, `INDEX: idx_ticket_customer_id`)
- **`assigned_employee_id`** `BIGINT` `NULL` (`FK -> employees.id`, `INDEX: idx_ticket_assigned_employee_id`)
- **`service_id`** `BIGINT` `NOT NULL` (`FK -> services.id`, `INDEX: idx_ticket_service_id`)
- **`title`** `VARCHAR(200)` `NOT NULL`
- **`description`** `TEXT` `NOT NULL`
- **`priority`** `VARCHAR(20)` `NOT NULL` `DEFAULT 'MEDIUM'` (`LOW`, `MEDIUM`, `HIGH`, `CRITICAL`) (`INDEX: idx_ticket_priority`)
- **`status`** `VARCHAR(20)` `NOT NULL` `DEFAULT 'OPEN'` (`OPEN`, `ASSIGNED`, `IN_PROGRESS`, `RESOLVED`, `CLOSED`, `REOPENED`) (`INDEX: idx_ticket_status`)
- **`resolution`** `TEXT` `NULL`
- **`created_at`** `DATETIME` `NOT NULL` `DEFAULT CURRENT_TIMESTAMP` (`INDEX: idx_ticket_created_at`)
- **`updated_at`** `DATETIME` `NOT NULL` `DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP`
- **`assigned_at`** `DATETIME` `NULL`
- **`resolved_at`** `DATETIME` `NULL`
- **`closed_at`** `DATETIME` `NULL`

### 2.7 `ticket_comments`
Discussion messages and updates per ticket.
- **`id`** `BIGINT` `AUTO_INCREMENT` `PRIMARY KEY`
- **`ticket_id`** `BIGINT` `NOT NULL` (`FK -> tickets.id ON DELETE CASCADE`, `INDEX: idx_comment_ticket_id`)
- **`user_id`** `BIGINT` `NOT NULL` (`FK -> users.id`, `INDEX: idx_comment_user_id`)
- **`comment_text`** `TEXT` `NOT NULL`
- **`created_at`** `DATETIME` `NOT NULL` `DEFAULT CURRENT_TIMESTAMP`
- **`updated_at`** `DATETIME` `NOT NULL` `DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP`

### 2.8 `ticket_attachments`
Uploaded file metadata records.
- **`id`** `BIGINT` `AUTO_INCREMENT` `PRIMARY KEY`
- **`ticket_id`** `BIGINT` `NOT NULL` (`FK -> tickets.id ON DELETE CASCADE`, `INDEX: idx_attachment_ticket_id`)
- **`uploaded_by`** `BIGINT` `NOT NULL` (`FK -> users.id`, `INDEX: idx_attachment_uploaded_by`)
- **`original_file_name`** `VARCHAR(255)` `NOT NULL`
- **`stored_file_name`** `VARCHAR(255)` `NOT NULL`
- **`file_path`** `VARCHAR(500)` `NOT NULL`
- **`file_type`** `VARCHAR(100)` `NULL`
- **`file_size`** `BIGINT` `NULL`
- **`created_at`** `DATETIME` `NOT NULL` `DEFAULT CURRENT_TIMESTAMP`

### 2.9 `ticket_status_history`
Lifecycle state transition audit trail.
- **`id`** `BIGINT` `AUTO_INCREMENT` `PRIMARY KEY`
- **`ticket_id`** `BIGINT` `NOT NULL` (`FK -> tickets.id ON DELETE CASCADE`, `INDEX: idx_history_ticket_id`)
- **`old_status`** `VARCHAR(20)` `NULL`
- **`new_status`** `VARCHAR(20)` `NOT NULL`
- **`changed_by`** `BIGINT` `NOT NULL` (`FK -> users.id`, `INDEX: idx_history_changed_by`)
- **`comment`** `TEXT` `NULL`
- **`created_at`** `DATETIME` `NOT NULL` `DEFAULT CURRENT_TIMESTAMP`

### 2.10 `feedback`
Customer ratings and reviews on resolved tickets.
- **`id`** `BIGINT` `AUTO_INCREMENT` `PRIMARY KEY`
- **`ticket_id`** `BIGINT` `NOT NULL` `UNIQUE` (`FK -> tickets.id ON DELETE CASCADE`)
- **`customer_id`** `BIGINT` `NOT NULL` (`FK -> customers.id`, `INDEX: idx_feedback_customer_id`)
- **`rating`** `TINYINT` `NOT NULL` `CHECK (rating BETWEEN 1 AND 5)`
- **`comment`** `TEXT` `NULL`
- **`created_at`** `DATETIME` `NOT NULL` `DEFAULT CURRENT_TIMESTAMP`
- **`updated_at`** `DATETIME` `NOT NULL` `DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP`

### 2.11 `notifications`
In-app alerts and notifications.
- **`id`** `BIGINT` `AUTO_INCREMENT` `PRIMARY KEY`
- **`user_id`** `BIGINT` `NOT NULL` (`FK -> users.id ON DELETE CASCADE`, `INDEX: idx_notification_user_id`)
- **`title`** `VARCHAR(200)` `NOT NULL`
- **`message`** `TEXT` `NOT NULL`
- **`type`** `VARCHAR(50)` `NOT NULL` `DEFAULT 'GENERAL'`
- **`is_read`** `TINYINT(1)` `NOT NULL` `DEFAULT 0` (`INDEX: idx_notification_user_read` on `(user_id, is_read)`)
- **`created_at`** `DATETIME` `NOT NULL` `DEFAULT CURRENT_TIMESTAMP`

### 2.12 `activity_logs`
System-wide audit event records.
- **`id`** `BIGINT` `AUTO_INCREMENT` `PRIMARY KEY`
- **`user_id`** `BIGINT` `NULL` (`FK -> users.id ON DELETE SET NULL`, `INDEX: idx_activity_log_user_id`)
- **`action`** `VARCHAR(100)` `NOT NULL`
- **`entity_type`** `VARCHAR(100)` `NULL` (`INDEX: idx_activity_log_entity` on `(entity_type, entity_id)`)
- **`entity_id`** `BIGINT` `NULL`
- **`description`** `TEXT` `NULL`
- **`created_at`** `DATETIME` `NOT NULL` `DEFAULT CURRENT_TIMESTAMP` (`INDEX: idx_activity_log_created`)

---

## 3. Enums

### 3.1 `TicketPriority`
- `LOW`
- `MEDIUM`
- `HIGH`
- `CRITICAL`

### 3.2 `TicketStatus`
- `OPEN`
- `ASSIGNED`
- `IN_PROGRESS`
- `RESOLVED`
- `CLOSED`
- `REOPENED`

---

## 4. Ticket Lifecycle Workflow

```
[OPEN] ──(Assign to Agent)──▶ [ASSIGNED] ──(Start Investigation)──▶ [IN_PROGRESS]
                                                                          │
                                                                 (Resolve Ticket)
                                                                          │
                                                                          ▼
[CLOSED] ◀──(Customer Feedback / Auto-Close)── [RESOLVED]
    │                                              │
    └──────────────(Customer Reopens)──────────────┘
                           │
                           ▼
                      [REOPENED] ──▶ [IN_PROGRESS]
```
