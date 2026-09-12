-- =============================================================================
-- Smart Service & Support Management System — Database Schema
-- MySQL 8.0+
-- Phase 0: Core table definitions (skeleton — constraints & indexes will be
--          finalized in Phase 2 after entity relationships are confirmed).
-- =============================================================================

CREATE DATABASE IF NOT EXISTS `smart_support`
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;

USE `smart_support`;

-- ─────────────────────────────────────────────────────────────────────────────
-- ROLES
-- ─────────────────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS `roles` (
  `id`         BIGINT       NOT NULL AUTO_INCREMENT,
  `name`       VARCHAR(50)  NOT NULL,
  `created_at` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ─────────────────────────────────────────────────────────────────────────────
-- USERS
-- ─────────────────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS `users` (
  `id`            BIGINT        NOT NULL AUTO_INCREMENT,
  `username`      VARCHAR(50)   NOT NULL,
  `email`         VARCHAR(100)  NOT NULL,
  `password_hash` VARCHAR(255)  NOT NULL,
  `enabled`       TINYINT(1)    NOT NULL DEFAULT 1,
  `created_at`    DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at`    DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_username` (`username`),
  UNIQUE KEY `uk_user_email`    (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ─────────────────────────────────────────────────────────────────────────────
-- USER_ROLES  (many-to-many)
-- ─────────────────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS `user_roles` (
  `user_id` BIGINT NOT NULL,
  `role_id` BIGINT NOT NULL,
  PRIMARY KEY (`user_id`, `role_id`),
  CONSTRAINT `fk_ur_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_ur_role` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ─────────────────────────────────────────────────────────────────────────────
-- CUSTOMERS  (extends users — one-to-one)
-- ─────────────────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS `customers` (
  `id`         BIGINT       NOT NULL AUTO_INCREMENT,
  `user_id`    BIGINT       NOT NULL,
  `first_name` VARCHAR(50)  NOT NULL,
  `last_name`  VARCHAR(50)  NOT NULL,
  `phone`      VARCHAR(20)  NULL,
  `address`    VARCHAR(255) NULL,
  `created_at` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_customer_user_id` (`user_id`),
  CONSTRAINT `fk_customer_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ─────────────────────────────────────────────────────────────────────────────
-- EMPLOYEES  (extends users — one-to-one)
-- ─────────────────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS `employees` (
  `id`         BIGINT      NOT NULL AUTO_INCREMENT,
  `user_id`    BIGINT      NOT NULL,
  `first_name` VARCHAR(50) NOT NULL,
  `last_name`  VARCHAR(50) NOT NULL,
  `department` VARCHAR(100) NULL,
  `phone`      VARCHAR(20)  NULL,
  `created_at` DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_employee_user_id` (`user_id`),
  CONSTRAINT `fk_employee_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ─────────────────────────────────────────────────────────────────────────────
-- SERVICES
-- ─────────────────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS `services` (
  `id`          BIGINT       NOT NULL AUTO_INCREMENT,
  `name`        VARCHAR(100) NOT NULL,
  `description` TEXT         NULL,
  `category`    VARCHAR(100) NULL,
  `status`      ENUM('ACTIVE','INACTIVE') NOT NULL DEFAULT 'ACTIVE',
  `created_at`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_service_name` (`name`),
  INDEX `idx_service_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ─────────────────────────────────────────────────────────────────────────────
-- TICKETS
-- ─────────────────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS `tickets` (
  `id`                BIGINT        NOT NULL AUTO_INCREMENT,
  `ticket_number`     VARCHAR(20)   NOT NULL,
  `customer_id`       BIGINT        NOT NULL,
  `service_id`        BIGINT        NOT NULL,
  `agent_id`          BIGINT        NULL,
  `title`             VARCHAR(200)  NOT NULL,
  `description`       TEXT          NOT NULL,
  `category`          VARCHAR(100)  NULL,
  `priority`          ENUM('LOW','MEDIUM','HIGH','CRITICAL') NOT NULL DEFAULT 'MEDIUM',
  `status`            ENUM('OPEN','ASSIGNED','IN_PROGRESS','RESOLVED','CLOSED','REOPENED') NOT NULL DEFAULT 'OPEN',
  `resolution`        TEXT          NULL,
  `created_at`        DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at`        DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `assigned_at`       DATETIME      NULL,
  `resolved_at`       DATETIME      NULL,
  `closed_at`         DATETIME      NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_ticket_number` (`ticket_number`),
  INDEX `idx_ticket_status`     (`status`),
  INDEX `idx_ticket_priority`   (`priority`),
  INDEX `idx_ticket_customer`   (`customer_id`),
  INDEX `idx_ticket_agent`      (`agent_id`),
  INDEX `idx_ticket_service`    (`service_id`),
  CONSTRAINT `fk_ticket_customer` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`id`),
  CONSTRAINT `fk_ticket_service`  FOREIGN KEY (`service_id`)  REFERENCES `services`  (`id`),
  CONSTRAINT `fk_ticket_agent`    FOREIGN KEY (`agent_id`)    REFERENCES `employees`  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ─────────────────────────────────────────────────────────────────────────────
-- TICKET COMMENTS
-- ─────────────────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS `ticket_comments` (
  `id`         BIGINT    NOT NULL AUTO_INCREMENT,
  `ticket_id`  BIGINT    NOT NULL,
  `user_id`    BIGINT    NOT NULL,
  `content`    TEXT      NOT NULL,
  `created_at` DATETIME  NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  INDEX `idx_comment_ticket` (`ticket_id`),
  CONSTRAINT `fk_comment_ticket` FOREIGN KEY (`ticket_id`) REFERENCES `tickets` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_comment_user`   FOREIGN KEY (`user_id`)   REFERENCES `users`   (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ─────────────────────────────────────────────────────────────────────────────
-- TICKET ATTACHMENTS
-- ─────────────────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS `ticket_attachments` (
  `id`           BIGINT       NOT NULL AUTO_INCREMENT,
  `ticket_id`    BIGINT       NOT NULL,
  `filename`     VARCHAR(255) NOT NULL,
  `stored_path`  VARCHAR(500) NOT NULL,
  `content_type` VARCHAR(100) NULL,
  `size_bytes`   BIGINT       NULL,
  `uploaded_at`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  INDEX `idx_attachment_ticket` (`ticket_id`),
  CONSTRAINT `fk_attachment_ticket` FOREIGN KEY (`ticket_id`) REFERENCES `tickets` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ─────────────────────────────────────────────────────────────────────────────
-- TICKET STATUS HISTORY
-- ─────────────────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS `ticket_status_history` (
  `id`              BIGINT   NOT NULL AUTO_INCREMENT,
  `ticket_id`       BIGINT   NOT NULL,
  `previous_status` VARCHAR(20) NULL,
  `new_status`      VARCHAR(20) NOT NULL,
  `changed_by`      BIGINT   NOT NULL,
  `note`            TEXT     NULL,
  `changed_at`      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  INDEX `idx_history_ticket` (`ticket_id`),
  CONSTRAINT `fk_history_ticket` FOREIGN KEY (`ticket_id`)  REFERENCES `tickets` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_history_user`   FOREIGN KEY (`changed_by`) REFERENCES `users`   (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ─────────────────────────────────────────────────────────────────────────────
-- FEEDBACK
-- ─────────────────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS `feedback` (
  `id`          BIGINT     NOT NULL AUTO_INCREMENT,
  `ticket_id`   BIGINT     NOT NULL,
  `customer_id` BIGINT     NOT NULL,
  `rating`      TINYINT    NOT NULL CHECK (`rating` BETWEEN 1 AND 5),
  `comment`     TEXT       NULL,
  `created_at`  DATETIME   NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_feedback_ticket` (`ticket_id`),
  CONSTRAINT `fk_feedback_ticket`   FOREIGN KEY (`ticket_id`)   REFERENCES `tickets`   (`id`),
  CONSTRAINT `fk_feedback_customer` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ─────────────────────────────────────────────────────────────────────────────
-- NOTIFICATIONS
-- ─────────────────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS `notifications` (
  `id`         BIGINT       NOT NULL AUTO_INCREMENT,
  `user_id`    BIGINT       NOT NULL,
  `message`    TEXT         NOT NULL,
  `type`       ENUM('TICKET','GENERAL','ALERT') NOT NULL DEFAULT 'GENERAL',
  `is_read`    TINYINT(1)   NOT NULL DEFAULT 0,
  `created_at` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  INDEX `idx_notification_user_read` (`user_id`, `is_read`),
  CONSTRAINT `fk_notification_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ─────────────────────────────────────────────────────────────────────────────
-- ACTIVITY LOGS
-- ─────────────────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS `activity_logs` (
  `id`          BIGINT       NOT NULL AUTO_INCREMENT,
  `user_id`     BIGINT       NULL,
  `action_type` VARCHAR(100) NOT NULL,
  `entity_name` VARCHAR(100) NULL,
  `entity_id`   BIGINT       NULL,
  `description` TEXT         NULL,
  `ip_address`  VARCHAR(45)  NULL,
  `created_at`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  INDEX `idx_log_user`       (`user_id`),
  INDEX `idx_log_created_at` (`created_at`),
  CONSTRAINT `fk_log_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
