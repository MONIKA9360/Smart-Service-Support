# API Documentation — Smart Service & Support Management System

> **Phase 1 Status:** Data model, JPA entities, DTOs with Bean Validation, MapStruct mappers, and repositories are established. REST endpoints and controllers will be connected in subsequent phases (Phase 2: Authentication; Phase 3: Ticket Operations).

---

## 1. Base Configuration

- **Base URL:** `http://localhost:8080/api`
- **Swagger / OpenAPI UI:** `http://localhost:8080/api/swagger-ui.html`
- **OpenAPI JSON Spec:** `http://localhost:8080/api/api-docs`

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

## 3. Data Transfer Objects (Phase 1 Implemented)

### Request DTOs
- `UserCreateRequest` (`firstName`, `lastName`, `email`, `password`, `phone`, `roleId`)
- `UserUpdateRequest` (`firstName`, `lastName`, `email`, `phone`, `isActive`, `roleId`)
- `CustomerRequest` (`userId`, `customerCode`, `address`, `city`, `state`, `postalCode`)
- `EmployeeRequest` (`userId`, `employeeCode`, `department`, `designation`)
- `ServiceRequest` (`serviceName`, `description`, `category`, `isActive`)
- `TicketCreateRequest` (`customerId`, `serviceId`, `title`, `description`, `priority`)
- `TicketUpdateRequest` (`assignedEmployeeId`, `serviceId`, `title`, `description`, `priority`, `status`, `resolution`)
- `TicketCommentRequest` (`ticketId`, `userId`, `commentText`)
- `FeedbackRequest` (`ticketId`, `customerId`, `rating`, `comment`)

### Response DTOs
- `UserResponse` (`id`, `firstName`, `lastName`, `email`, `phone`, `roleId`, `roleName`, `isActive`, `createdAt`, `updatedAt` — **strictly excludes `passwordHash`**)
- `RoleResponse` (`id`, `name`, `description`, `createdAt`, `updatedAt`)
- `CustomerResponse` (`id`, `userId`, `firstName`, `lastName`, `email`, `customerCode`, `address`, `city`, `state`, `postalCode`, `createdAt`, `updatedAt`)
- `EmployeeResponse` (`id`, `userId`, `firstName`, `lastName`, `email`, `employeeCode`, `department`, `designation`, `createdAt`, `updatedAt`)
- `ServiceResponse` (`id`, `serviceName`, `description`, `category`, `isActive`, `createdAt`, `updatedAt`)
- `TicketResponse` (`id`, `ticketNumber`, `customerId`, `customerName`, `customerCode`, `assignedEmployeeId`, `assignedEmployeeName`, `serviceId`, `serviceName`, `title`, `description`, `priority`, `status`, `resolution`, `createdAt`, `updatedAt`, `assignedAt`, `resolvedAt`, `closedAt`)
- `TicketCommentResponse` (`id`, `ticketId`, `userId`, `userName`, `userRole`, `commentText`, `createdAt`, `updatedAt`)
- `TicketAttachmentResponse` (`id`, `ticketId`, `uploadedById`, `uploadedByName`, `originalFileName`, `storedFileName`, `filePath`, `fileType`, `fileSize`, `createdAt`)
- `TicketStatusHistoryResponse` (`id`, `ticketId`, `oldStatus`, `newStatus`, `changedById`, `changedByName`, `comment`, `createdAt`)
- `FeedbackResponse` (`id`, `ticketId`, `ticketNumber`, `customerId`, `customerName`, `rating`, `comment`, `createdAt`, `updatedAt`)
- `NotificationResponse` (`id`, `userId`, `title`, `message`, `type`, `isRead`, `createdAt`)
- `ActivityLogResponse` (`id`, `userId`, `userName`, `action`, `entityType`, `entityId`, `description`, `createdAt`)
