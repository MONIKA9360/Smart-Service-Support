-- =============================================================================
-- Smart Service & Support Management System — Database Schema
-- MySQL 8.0+
-- Phase 1: Normalized 12-Table Relational Schema with Constraints & Indexes
-- =============================================================================

CREATE DATABASE IF NOT EXISTS `smart_support`
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;

USE `smart_support`;

-- ─────────────────────────────────────────────────────────────────────────────
-- 1. ROLES
-- ─────────────────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS `roles` (
  `id`          BIGINT        NOT NULL AUTO_INCREMENT,
  `name`        VARCHAR(50)   NOT NULL,
  `description` VARCHAR(255)  NULL,
  `created_at`  DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at`  DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ─────────────────────────────────────────────────────────────────────────────
-- 2. USERS
-- ─────────────────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS `users` (
  `id`            BIGINT        NOT NULL AUTO_INCREMENT,
  `first_name`    VARCHAR(50)   NOT NULL,
  `last_name`     VARCHAR(50)   NOT NULL,
  `email`         VARCHAR(100)  NOT NULL,
  `password_hash` VARCHAR(255)  NOT NULL,
  `phone`         VARCHAR(20)   NULL,
  `role_id`       BIGINT        NOT NULL,
  `is_active`     TINYINT(1)    NOT NULL DEFAULT 1,
  `created_at`    DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at`    DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_email` (`email`),
  INDEX `idx_user_email` (`email`),
  INDEX `idx_user_role_id` (`role_id`),
  CONSTRAINT `fk_user_role` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ─────────────────────────────────────────────────────────────────────────────
-- 3. CUSTOMERS
-- ─────────────────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS `customers` (
  `id`            BIGINT        NOT NULL AUTO_INCREMENT,
  `user_id`       BIGINT        NOT NULL,
  `customer_code` VARCHAR(50)   NOT NULL,
  `address`       VARCHAR(255)  NULL,
  `city`          VARCHAR(100)  NULL,
  `state`         VARCHAR(100)  NULL,
  `postal_code`   VARCHAR(20)   NULL,
  `created_at`    DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at`    DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_customer_user_id` (`user_id`),
  UNIQUE KEY `uk_customer_code` (`customer_code`),
  CONSTRAINT `fk_customer_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ─────────────────────────────────────────────────────────────────────────────
-- 4. EMPLOYEES
-- ─────────────────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS `employees` (
  `id`            BIGINT        NOT NULL AUTO_INCREMENT,
  `user_id`       BIGINT        NOT NULL,
  `employee_code` VARCHAR(50)   NOT NULL,
  `department`    VARCHAR(100)  NULL,
  `designation`   VARCHAR(100)  NULL,
  `created_at`    DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at`    DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_employee_user_id` (`user_id`),
  UNIQUE KEY `uk_employee_code` (`employee_code`),
  CONSTRAINT `fk_employee_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ─────────────────────────────────────────────────────────────────────────────
-- 5. SERVICES
-- ─────────────────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS `services` (
  `id`           BIGINT        NOT NULL AUTO_INCREMENT,
  `service_name` VARCHAR(100)  NOT NULL,
  `description`  TEXT          NULL,
  `category`     VARCHAR(100)  NULL,
  `is_active`    TINYINT(1)    NOT NULL DEFAULT 1,
  `created_at`   DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at`   DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_service_name` (`service_name`),
  INDEX `idx_service_category` (`category`),
  INDEX `idx_service_is_active` (`is_active`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ─────────────────────────────────────────────────────────────────────────────
-- 6. TICKETS
-- ─────────────────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS `tickets` (
  `id`                   BIGINT        NOT NULL AUTO_INCREMENT,
  `ticket_number`        VARCHAR(30)   NOT NULL,
  `customer_id`          BIGINT        NOT NULL,
  `assigned_employee_id` BIGINT        NULL,
  `service_id`           BIGINT        NOT NULL,
  `title`                VARCHAR(200)  NOT NULL,
  `description`          TEXT          NOT NULL,
  `priority`             VARCHAR(20)   NOT NULL DEFAULT 'MEDIUM',
  `status`               VARCHAR(20)   NOT NULL DEFAULT 'OPEN',
  `resolution`           TEXT          NULL,
  `created_at`           DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at`           DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `assigned_at`          DATETIME      NULL,
  `resolved_at`          DATETIME      NULL,
  `closed_at`            DATETIME      NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_ticket_number` (`ticket_number`),
  INDEX `idx_ticket_number` (`ticket_number`),
  INDEX `idx_ticket_customer_id` (`customer_id`),
  INDEX `idx_ticket_assigned_employee_id` (`assigned_employee_id`),
  INDEX `idx_ticket_service_id` (`service_id`),
  INDEX `idx_ticket_status` (`status`),
  INDEX `idx_ticket_priority` (`priority`),
  INDEX `idx_ticket_created_at` (`created_at`),
  CONSTRAINT `fk_ticket_customer` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `fk_ticket_employee` FOREIGN KEY (`assigned_employee_id`) REFERENCES `employees` (`id`) ON UPDATE CASCADE ON DELETE SET NULL,
  CONSTRAINT `fk_ticket_service`  FOREIGN KEY (`service_id`) REFERENCES `services` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ─────────────────────────────────────────────────────────────────────────────
-- 7. TICKET COMMENTS
-- ─────────────────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS `ticket_comments` (
  `id`           BIGINT    NOT NULL AUTO_INCREMENT,
  `ticket_id`    BIGINT    NOT NULL,
  `user_id`      BIGINT    NOT NULL,
  `comment_text` TEXT      NOT NULL,
  `created_at`   DATETIME  NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at`   DATETIME  NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  INDEX `idx_comment_ticket_id` (`ticket_id`),
  INDEX `idx_comment_user_id` (`user_id`),
  CONSTRAINT `fk_comment_ticket` FOREIGN KEY (`ticket_id`) REFERENCES `tickets` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_comment_user`   FOREIGN KEY (`user_id`)   REFERENCES `users`   (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ─────────────────────────────────────────────────────────────────────────────
-- 8. TICKET ATTACHMENTS
-- ─────────────────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS `ticket_attachments` (
  `id`                 BIGINT       NOT NULL AUTO_INCREMENT,
  `ticket_id`          BIGINT       NOT NULL,
  `uploaded_by`        BIGINT       NOT NULL,
  `original_file_name` VARCHAR(255) NOT NULL,
  `stored_file_name`   VARCHAR(255) NOT NULL,
  `file_path`          VARCHAR(500) NOT NULL,
  `file_type`          VARCHAR(100) NULL,
  `file_size`          BIGINT       NULL,
  `created_at`         DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  INDEX `idx_attachment_ticket_id` (`ticket_id`),
  INDEX `idx_attachment_uploaded_by` (`uploaded_by`),
  CONSTRAINT `fk_attachment_ticket` FOREIGN KEY (`ticket_id`)   REFERENCES `tickets` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_attachment_user`   FOREIGN KEY (`uploaded_by`) REFERENCES `users`   (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ─────────────────────────────────────────────────────────────────────────────
-- 9. TICKET STATUS HISTORY
-- ─────────────────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS `ticket_status_history` (
  `id`         BIGINT       NOT NULL AUTO_INCREMENT,
  `ticket_id`  BIGINT       NOT NULL,
  `old_status` VARCHAR(20)  NULL,
  `new_status` VARCHAR(20)  NOT NULL,
  `changed_by` BIGINT       NOT NULL,
  `comment`    TEXT         NULL,
  `created_at` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  INDEX `idx_history_ticket_id` (`ticket_id`),
  INDEX `idx_history_changed_by` (`changed_by`),
  CONSTRAINT `fk_history_ticket` FOREIGN KEY (`ticket_id`)  REFERENCES `tickets` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_history_user`   FOREIGN KEY (`changed_by`) REFERENCES `users`   (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ─────────────────────────────────────────────────────────────────────────────
-- 10. FEEDBACK
-- ─────────────────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS `feedback` (
  `id`          BIGINT     NOT NULL AUTO_INCREMENT,
  `ticket_id`   BIGINT     NOT NULL,
  `customer_id` BIGINT     NOT NULL,
  `rating`      TINYINT    NOT NULL CHECK (`rating` BETWEEN 1 AND 5),
  `comment`     TEXT       NULL,
  `created_at`  DATETIME   NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at`  DATETIME   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_feedback_ticket_id` (`ticket_id`),
  INDEX `idx_feedback_customer_id` (`customer_id`),
  CONSTRAINT `fk_feedback_ticket`   FOREIGN KEY (`ticket_id`)   REFERENCES `tickets`   (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_feedback_customer` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ─────────────────────────────────────────────────────────────────────────────
-- 11. NOTIFICATIONS
-- ─────────────────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS `notifications` (
  `id`         BIGINT        NOT NULL AUTO_INCREMENT,
  `user_id`    BIGINT        NOT NULL,
  `title`      VARCHAR(200)  NOT NULL,
  `message`    TEXT          NOT NULL,
  `type`       VARCHAR(50)   NOT NULL DEFAULT 'GENERAL',
  `is_read`    TINYINT(1)    NOT NULL DEFAULT 0,
  `created_at` DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  INDEX `idx_notification_user_id` (`user_id`),
  INDEX `idx_notification_user_read` (`user_id`, `is_read`),
  CONSTRAINT `fk_notification_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ─────────────────────────────────────────────────────────────────────────────
-- 12. ACTIVITY LOGS
-- ─────────────────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS `activity_logs` (
  `id`          BIGINT        NOT NULL AUTO_INCREMENT,
  `user_id`     BIGINT        NULL,
  `action`      VARCHAR(100)  NOT NULL,
  `entity_type` VARCHAR(100)  NULL,
  `entity_id`   BIGINT        NULL,
  `description` TEXT          NULL,
  `created_at`  DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  INDEX `idx_activity_log_user_id` (`user_id`),
  INDEX `idx_activity_log_entity`  (`entity_type`, `entity_id`),
  INDEX `idx_activity_log_created` (`created_at`),
  CONSTRAINT `fk_log_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
