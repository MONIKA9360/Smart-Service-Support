-- =============================================================================
-- Smart Service & Support Management System — Seed Data
-- MySQL 8.0+
-- Phase 1: Development/Demo Data with BCrypt-hashed passwords
-- Passwords below are BCrypt hashes (strength 10) for the value: Admin@123
-- (Development & testing only — do not use in production)
-- =============================================================================

USE `smart_support`;

-- ─────────────────────────────────────────────────────────────────────────────
-- 1. ROLES
-- ─────────────────────────────────────────────────────────────────────────────
INSERT IGNORE INTO `roles` (`id`, `name`, `description`) VALUES
  (1, 'ROLE_ADMIN',    'System administrator with full access to configuration, users, and reports'),
  (2, 'ROLE_AGENT',    'Support employee/agent responsible for handling and resolving assigned tickets'),
  (3, 'ROLE_CUSTOMER', 'Customer user who can create service tickets, add comments, and provide feedback');

-- ─────────────────────────────────────────────────────────────────────────────
-- 2. USERS
-- Verified BCrypt hash for "Admin@123" is: $2a$10$YukCzCnR/elzZB0SyLd4ZulPOwxVdV0u/drrVhs.H4uhWIq/I/F9O
-- ─────────────────────────────────────────────────────────────────────────────
INSERT IGNORE INTO `users`
  (`id`, `first_name`, `last_name`, `email`, `password_hash`, `phone`, `role_id`, `is_active`)
VALUES
  -- 1 Admin
  (1, 'System', 'Admin',    'admin@smartsupport.com',      '$2a$10$YukCzCnR/elzZB0SyLd4ZulPOwxVdV0u/drrVhs.H4uhWIq/I/F9O', '+1-555-0001', 1, 1),
  -- 2 Support Agents
  (2, 'Alice',  'Turner',   'alice@smartsupport.com',      '$2a$10$YukCzCnR/elzZB0SyLd4ZulPOwxVdV0u/drrVhs.H4uhWIq/I/F9O', '+1-555-0101', 2, 1),
  (3, 'Bob',    'Harris',   'bob@smartsupport.com',        '$2a$10$YukCzCnR/elzZB0SyLd4ZulPOwxVdV0u/drrVhs.H4uhWIq/I/F9O', '+1-555-0102', 2, 1),
  -- 3 Customers
  (4, 'John',   'Doe',      'john.doe@example.com',        '$2a$10$YukCzCnR/elzZB0SyLd4ZulPOwxVdV0u/drrVhs.H4uhWIq/I/F9O', '+1-555-1001', 3, 1),
  (5, 'Jane',   'Smith',    'jane.smith@example.com',      '$2a$10$YukCzCnR/elzZB0SyLd4ZulPOwxVdV0u/drrVhs.H4uhWIq/I/F9O', '+1-555-1002', 3, 1),
  (6, 'Mike',   'Johnson',  'mike.johnson@example.com',    '$2a$10$YukCzCnR/elzZB0SyLd4ZulPOwxVdV0u/drrVhs.H4uhWIq/I/F9O', '+1-555-1003', 3, 1);

-- ─────────────────────────────────────────────────────────────────────────────
-- 3. CUSTOMERS
-- ─────────────────────────────────────────────────────────────────────────────
INSERT IGNORE INTO `customers`
  (`id`, `user_id`, `customer_code`, `address`, `city`, `state`, `postal_code`)
VALUES
  (1, 4, 'CUST-00001', '123 Elm Street',     'New York',    'NY', '10001'),
  (2, 5, 'CUST-00002', '456 Oak Avenue',     'Los Angeles', 'CA', '90001'),
  (3, 6, 'CUST-00003', '789 Pine Road',      'Chicago',     'IL', '60601');

-- ─────────────────────────────────────────────────────────────────────────────
-- 4. EMPLOYEES
-- ─────────────────────────────────────────────────────────────────────────────
INSERT IGNORE INTO `employees`
  (`id`, `user_id`, `employee_code`, `department`, `designation`)
VALUES
  (1, 2, 'EMP-00001', 'Technical Support', 'Senior Support Specialist'),
  (2, 3, 'EMP-00002', 'Software Support',  'Application Engineer');

-- ─────────────────────────────────────────────────────────────────────────────
-- 5. SERVICES
-- ─────────────────────────────────────────────────────────────────────────────
INSERT IGNORE INTO `services`
  (`id`, `service_name`, `description`, `category`, `is_active`)
VALUES
  (1, 'Technical Support',  'Hardware, operating system diagnostics, and device troubleshooting', 'IT',        1),
  (2, 'Software Support',   'Application bugs, installation, licensing, and updates',             'IT',        1),
  (3, 'Network Support',    'LAN, WAN, VPN configuration, and connectivity issues',               'IT',        1),
  (4, 'Account Support',    'Account access, profile updates, and authentication assistance',      'Account',   1),
  (5, 'Hardware Support',   'Device repairs, component replacements, and warranty services',      'Hardware',  1),
  (6, 'General Inquiry',    'General service questions, billing inquiries, and guidance',         'General',   1);

-- ─────────────────────────────────────────────────────────────────────────────
-- 6. SAMPLE TICKETS
-- ─────────────────────────────────────────────────────────────────────────────
INSERT IGNORE INTO `tickets`
  (`id`, `ticket_number`, `customer_id`, `assigned_employee_id`, `service_id`,
   `title`, `description`, `priority`, `status`, `resolution`, `created_at`, `assigned_at`, `resolved_at`, `closed_at`)
VALUES
  (1, 'SR-2026-00001', 1, NULL, 1,
   'Laptop not booting after system update',
   'My workstation laptop freezes at the manufacturer splash screen after the recent security update.',
   'HIGH', 'OPEN', NULL, NOW(), NULL, NULL, NULL),

  (2, 'SR-2026-00002', 2, 1, 2,
   'CRM application crashes immediately on startup',
   'When launching the CRM desktop client, error code 0x8007001F is displayed and the application closes.',
   'CRITICAL', 'ASSIGNED', NULL, NOW(), NOW(), NULL, NULL),

  (3, 'SR-2026-00003', 3, NULL, 4,
   'Unable to update registered billing email',
   'The profile settings page returns a validation error when attempting to change the primary email address.',
   'MEDIUM', 'OPEN', NULL, NOW(), NULL, NULL, NULL),

  (4, 'SR-2026-00004', 1, 2, 3,
   'VPN gateway connection drops every 15 minutes',
   'The corporate VPN tunnel disconnects intermittently during remote work sessions.',
   'HIGH', 'IN_PROGRESS', NULL, NOW(), NOW(), NULL, NULL);

-- ─────────────────────────────────────────────────────────────────────────────
-- 7. TICKET COMMENTS
-- ─────────────────────────────────────────────────────────────────────────────
INSERT IGNORE INTO `ticket_comments`
  (`id`, `ticket_id`, `user_id`, `comment_text`, `created_at`)
VALUES
  (1, 2, 2, 'Initial triage complete. Requesting application crash logs from the user.', NOW()),
  (2, 4, 3, 'Investigating gateway timeout parameters and MTU configuration.', NOW());

-- ─────────────────────────────────────────────────────────────────────────────
-- 8. TICKET STATUS HISTORY
-- ─────────────────────────────────────────────────────────────────────────────
INSERT IGNORE INTO `ticket_status_history`
  (`id`, `ticket_id`, `old_status`, `new_status`, `changed_by`, `comment`, `created_at`)
VALUES
  (1, 1, NULL,       'OPEN',        4, 'Ticket created by customer', NOW()),
  (2, 2, NULL,       'OPEN',        5, 'Ticket created by customer', NOW()),
  (3, 2, 'OPEN',     'ASSIGNED',    1, 'Assigned to Alice Turner',   NOW()),
  (4, 3, NULL,       'OPEN',        6, 'Ticket created by customer', NOW()),
  (5, 4, NULL,       'OPEN',        4, 'Ticket created by customer', NOW()),
  (6, 4, 'OPEN',     'ASSIGNED',    1, 'Assigned to Bob Harris',     NOW()),
  (7, 4, 'ASSIGNED', 'IN_PROGRESS', 3, 'Agent started diagnostic investigation', NOW());

-- ─────────────────────────────────────────────────────────────────────────────
-- 9. NOTIFICATIONS
-- ─────────────────────────────────────────────────────────────────────────────
INSERT IGNORE INTO `notifications`
  (`id`, `user_id`, `title`, `message`, `type`, `is_read`, `created_at`)
VALUES
  (1, 4, 'Ticket Created',  'Your ticket SR-2026-00001 has been submitted successfully.',     'TICKET',  0, NOW()),
  (2, 5, 'Ticket Assigned', 'Your ticket SR-2026-00002 has been assigned to Alice Turner.',    'TICKET',  0, NOW()),
  (3, 2, 'New Assignment',  'You have been assigned ticket SR-2026-00002.',                  'TICKET',  0, NOW()),
  (4, 3, 'New Assignment',  'You have been assigned ticket SR-2026-00004.',                  'TICKET',  0, NOW());

-- ─────────────────────────────────────────────────────────────────────────────
-- 10. ACTIVITY LOGS
-- ─────────────────────────────────────────────────────────────────────────────
INSERT IGNORE INTO `activity_logs`
  (`id`, `user_id`, `action`, `entity_type`, `entity_id`, `description`, `created_at`)
VALUES
  (1, 4, 'CREATE_TICKET', 'Ticket', 1, 'Customer John Doe created ticket SR-2026-00001', NOW()),
  (2, 5, 'CREATE_TICKET', 'Ticket', 2, 'Customer Jane Smith created ticket SR-2026-00002', NOW()),
  (3, 1, 'ASSIGN_TICKET', 'Ticket', 2, 'Admin assigned ticket SR-2026-00002 to Alice Turner', NOW()),
  (4, 6, 'CREATE_TICKET', 'Ticket', 3, 'Customer Mike Johnson created ticket SR-2026-00003', NOW());
