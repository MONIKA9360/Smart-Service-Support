-- =============================================================================
-- Smart Service & Support Management System — Seed Data
-- Phase 0: Demo users, roles, services, and sample tickets
-- Passwords below are BCrypt hashes for the value:  Admin@123
-- =============================================================================

USE `smart_support`;

-- ─────────────────────────────────────────────────────────────────────────────
-- ROLES
-- ─────────────────────────────────────────────────────────────────────────────
INSERT IGNORE INTO `roles` (`name`) VALUES
  ('ROLE_ADMIN'),
  ('ROLE_AGENT'),
  ('ROLE_CUSTOMER');

-- ─────────────────────────────────────────────────────────────────────────────
-- USERS
-- BCrypt hash of "Admin@123"  (strength 10)
-- ─────────────────────────────────────────────────────────────────────────────
INSERT IGNORE INTO `users`
  (`id`, `username`, `email`, `password_hash`, `enabled`)
VALUES
  -- Admin
  (1, 'admin',        'admin@smartsupport.com',      '$2a$10$7EqJtq98hPqEX7fNZaFWoO7pjFbRk8vVGZ6kJf4lHmMH1TZjS8JHi', 1),
  -- Support Agents
  (2, 'agent.alice',  'alice@smartsupport.com',      '$2a$10$7EqJtq98hPqEX7fNZaFWoO7pjFbRk8vVGZ6kJf4lHmMH1TZjS8JHi', 1),
  (3, 'agent.bob',    'bob@smartsupport.com',        '$2a$10$7EqJtq98hPqEX7fNZaFWoO7pjFbRk8vVGZ6kJf4lHmMH1TZjS8JHi', 1),
  -- Customers
  (4, 'john.doe',     'john.doe@example.com',        '$2a$10$7EqJtq98hPqEX7fNZaFWoO7pjFbRk8vVGZ6kJf4lHmMH1TZjS8JHi', 1),
  (5, 'jane.smith',   'jane.smith@example.com',      '$2a$10$7EqJtq98hPqEX7fNZaFWoO7pjFbRk8vVGZ6kJf4lHmMH1TZjS8JHi', 1),
  (6, 'mike.johnson', 'mike.johnson@example.com',    '$2a$10$7EqJtq98hPqEX7fNZaFWoO7pjFbRk8vVGZ6kJf4lHmMH1TZjS8JHi', 1);

-- ─────────────────────────────────────────────────────────────────────────────
-- USER ROLES
-- ─────────────────────────────────────────────────────────────────────────────
INSERT IGNORE INTO `user_roles` (`user_id`, `role_id`) VALUES
  (1, 1),  -- admin     → ROLE_ADMIN
  (2, 2),  -- alice     → ROLE_AGENT
  (3, 2),  -- bob       → ROLE_AGENT
  (4, 3),  -- john.doe  → ROLE_CUSTOMER
  (5, 3),  -- jane      → ROLE_CUSTOMER
  (6, 3);  -- mike      → ROLE_CUSTOMER

-- ─────────────────────────────────────────────────────────────────────────────
-- EMPLOYEES  (agents)
-- ─────────────────────────────────────────────────────────────────────────────
INSERT IGNORE INTO `employees`
  (`id`, `user_id`, `first_name`, `last_name`, `department`, `phone`)
VALUES
  (1, 2, 'Alice',  'Turner',  'Technical Support',  '+1-555-0101'),
  (2, 3, 'Bob',    'Harris',  'Software Support',   '+1-555-0102');

-- ─────────────────────────────────────────────────────────────────────────────
-- CUSTOMERS
-- ─────────────────────────────────────────────────────────────────────────────
INSERT IGNORE INTO `customers`
  (`id`, `user_id`, `first_name`, `last_name`, `phone`, `address`)
VALUES
  (1, 4, 'John',  'Doe',     '+1-555-1001', '123 Elm Street, New York'),
  (2, 5, 'Jane',  'Smith',   '+1-555-1002', '456 Oak Avenue, Los Angeles'),
  (3, 6, 'Mike',  'Johnson', '+1-555-1003', '789 Pine Road, Chicago');

-- ─────────────────────────────────────────────────────────────────────────────
-- SERVICES
-- ─────────────────────────────────────────────────────────────────────────────
INSERT IGNORE INTO `services`
  (`id`, `name`, `description`, `category`, `status`)
VALUES
  (1, 'Technical Support',  'Hardware, OS, and connectivity issues',          'IT',        'ACTIVE'),
  (2, 'Software Support',   'Application bugs, installation, and licensing',  'IT',        'ACTIVE'),
  (3, 'Network Support',    'LAN, WAN, VPN, and internet connectivity',       'IT',        'ACTIVE'),
  (4, 'Account Support',    'Login, password resets, account settings',       'Account',   'ACTIVE'),
  (5, 'Hardware Support',   'Device repairs, peripherals, and replacement',   'Hardware',  'ACTIVE'),
  (6, 'General Inquiry',    'General questions and service information',       'General',   'ACTIVE');

-- ─────────────────────────────────────────────────────────────────────────────
-- SAMPLE TICKETS
-- ─────────────────────────────────────────────────────────────────────────────
INSERT IGNORE INTO `tickets`
  (`id`, `ticket_number`, `customer_id`, `service_id`, `agent_id`,
   `title`, `description`, `priority`, `status`, `created_at`)
VALUES
  (1, 'SR-2026-00001', 1, 1, NULL,
   'Laptop not booting',
   'My laptop does not boot past the BIOS screen since this morning.',
   'HIGH', 'OPEN', NOW()),

  (2, 'SR-2026-00002', 2, 2, 1,
   'Software crashes on startup',
   'The CRM application crashes immediately on launch. Error code: 0x8007001F.',
   'CRITICAL', 'ASSIGNED', NOW()),

  (3, 'SR-2026-00003', 3, 4, NULL,
   'Unable to reset password',
   'I am not receiving the password reset email.',
   'MEDIUM', 'OPEN', NOW()),

  (4, 'SR-2026-00004', 1, 3, 2,
   'VPN connection drops frequently',
   'VPN disconnects every 15 minutes while working from home.',
   'HIGH', 'IN_PROGRESS', NOW());

-- ─────────────────────────────────────────────────────────────────────────────
-- STATUS HISTORY  (initial entries)
-- ─────────────────────────────────────────────────────────────────────────────
INSERT IGNORE INTO `ticket_status_history`
  (`ticket_id`, `previous_status`, `new_status`, `changed_by`, `note`)
VALUES
  (1, NULL,   'OPEN',        4, 'Ticket created by customer'),
  (2, NULL,   'OPEN',        5, 'Ticket created by customer'),
  (2, 'OPEN', 'ASSIGNED',    1, 'Assigned to Alice Turner'),
  (3, NULL,   'OPEN',        6, 'Ticket created by customer'),
  (4, NULL,   'OPEN',        4, 'Ticket created by customer'),
  (4, 'OPEN', 'ASSIGNED',    1, 'Assigned to Bob Harris'),
  (4, 'ASSIGNED', 'IN_PROGRESS', 3, 'Agent started working');

-- ─────────────────────────────────────────────────────────────────────────────
-- SAMPLE NOTIFICATIONS
-- ─────────────────────────────────────────────────────────────────────────────
INSERT IGNORE INTO `notifications` (`user_id`, `message`, `type`, `is_read`) VALUES
  (4, 'Your ticket SR-2026-00001 has been created successfully.', 'TICKET',   0),
  (5, 'Your ticket SR-2026-00002 has been assigned to Alice Turner.', 'TICKET', 0),
  (2, 'You have been assigned ticket SR-2026-00002.', 'TICKET', 0),
  (3, 'You have been assigned ticket SR-2026-00004.', 'TICKET', 0);
