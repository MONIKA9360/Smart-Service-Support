package com.example.support.repository;

import com.example.support.entity.*;
import com.example.support.entity.enums.TicketPriority;
import com.example.support.entity.enums.TicketStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class RepositoryMappingTest {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private TicketCommentRepository ticketCommentRepository;

    @Autowired
    private TicketAttachmentRepository ticketAttachmentRepository;

    @Autowired
    private TicketStatusHistoryRepository ticketStatusHistoryRepository;

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private ActivityLogRepository activityLogRepository;

    private Role adminRole;
    private Role agentRole;
    private Role customerRole;
    private User customerUser;
    private User agentUser;
    private Customer customer;
    private Employee employee;
    private Service service;

    @BeforeEach
    void setUp() {
        // Create Roles
        adminRole = roleRepository.save(Role.builder()
                .name("ROLE_ADMIN")
                .description("System Administrator")
                .build());

        agentRole = roleRepository.save(Role.builder()
                .name("ROLE_AGENT")
                .description("Support Agent")
                .build());

        customerRole = roleRepository.save(Role.builder()
                .name("ROLE_CUSTOMER")
                .description("End User Customer")
                .build());

        // Create Users
        customerUser = userRepository.save(User.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .passwordHash("$2a$10$7EqJtq98hPqEX7fNZaFWoO7pjFbRk8vVGZ6kJf4lHmMH1TZjS8JHi")
                .phone("+1-555-1001")
                .role(customerRole)
                .isActive(true)
                .build());

        agentUser = userRepository.save(User.builder()
                .firstName("Alice")
                .lastName("Turner")
                .email("alice.turner@smartsupport.com")
                .passwordHash("$2a$10$7EqJtq98hPqEX7fNZaFWoO7pjFbRk8vVGZ6kJf4lHmMH1TZjS8JHi")
                .phone("+1-555-0101")
                .role(agentRole)
                .isActive(true)
                .build());

        // Create Customer Profile
        customer = customerRepository.save(Customer.builder()
                .user(customerUser)
                .customerCode("CUST-00001")
                .address("123 Elm St")
                .city("New York")
                .state("NY")
                .postalCode("10001")
                .build());

        // Create Employee Profile
        employee = employeeRepository.save(Employee.builder()
                .user(agentUser)
                .employeeCode("EMP-00001")
                .department("Technical Support")
                .designation("Senior Specialist")
                .build());

        // Create Service
        service = serviceRepository.save(Service.builder()
                .serviceName("Technical Support")
                .description("Hardware and OS diagnostics")
                .category("IT")
                .isActive(true)
                .build());
    }

    @Test
    @DisplayName("RoleRepository: findByName and existsByName")
    void testRoleRepository() {
        Optional<Role> found = roleRepository.findByName("ROLE_ADMIN");
        assertThat(found).isPresent();
        assertThat(found.get().getDescription()).isEqualTo("System Administrator");
        assertThat(roleRepository.existsByName("ROLE_CUSTOMER")).isTrue();
        assertThat(roleRepository.existsByName("ROLE_NONEXISTENT")).isFalse();
    }

    @Test
    @DisplayName("UserRepository: findByEmail, findByRole_Id, and findByIsActiveTrue")
    void testUserRepository() {
        Optional<User> found = userRepository.findByEmail("john.doe@example.com");
        assertThat(found).isPresent();
        assertThat(found.get().getFirstName()).isEqualTo("John");

        List<User> agents = userRepository.findByRole_Id(agentRole.getId());
        assertThat(agents).hasSize(1);
        assertThat(agents.get(0).getEmail()).isEqualTo("alice.turner@smartsupport.com");

        List<User> activeUsers = userRepository.findByIsActiveTrue();
        assertThat(activeUsers).hasSize(2);
    }

    @Test
    @DisplayName("CustomerRepository and EmployeeRepository query methods")
    void testCustomerAndEmployeeRepository() {
        Optional<Customer> foundCust = customerRepository.findByCustomerCode("CUST-00001");
        assertThat(foundCust).isPresent();
        assertThat(foundCust.get().getCity()).isEqualTo("New York");

        Optional<Customer> custByUser = customerRepository.findByUser_Id(customerUser.getId());
        assertThat(custByUser).isPresent();

        Optional<Employee> foundEmp = employeeRepository.findByEmployeeCode("EMP-00001");
        assertThat(foundEmp).isPresent();
        assertThat(foundEmp.get().getDepartment()).isEqualTo("Technical Support");

        List<Employee> techEmps = employeeRepository.findByDepartment("Technical Support");
        assertThat(techEmps).hasSize(1);
    }

    @Test
    @DisplayName("ServiceRepository: findByServiceName and findByCategory")
    void testServiceRepository() {
        Optional<Service> found = serviceRepository.findByServiceName("Technical Support");
        assertThat(found).isPresent();
        assertThat(found.get().getCategory()).isEqualTo("IT");

        List<Service> itServices = serviceRepository.findByCategory("IT");
        assertThat(itServices).hasSize(1);
    }

    @Test
    @DisplayName("TicketRepository: Full ticket lifecycle with comments, status history, and feedback")
    void testTicketWorkflowAndRelations() {
        // 1. Create Ticket
        Ticket ticket = ticketRepository.save(Ticket.builder()
                .ticketNumber("SR-2026-00001")
                .customer(customer)
                .assignedEmployee(employee)
                .service(service)
                .title("Cannot boot laptop")
                .description("Laptop freezes on startup screen")
                .priority(TicketPriority.HIGH)
                .status(TicketStatus.OPEN)
                .build());

        assertThat(ticket.getId()).isNotNull();

        // 2. Query Ticket
        Optional<Ticket> foundTicket = ticketRepository.findByTicketNumber("SR-2026-00001");
        assertThat(foundTicket).isPresent();
        assertThat(foundTicket.get().getTitle()).isEqualTo("Cannot boot laptop");

        List<Ticket> custTickets = ticketRepository.findByCustomer_Id(customer.getId());
        assertThat(custTickets).hasSize(1);

        List<Ticket> empTickets = ticketRepository.findByAssignedEmployee_Id(employee.getId());
        assertThat(empTickets).hasSize(1);

        // 3. Add Ticket Comment
        TicketComment comment = ticketCommentRepository.save(TicketComment.builder()
                .ticket(ticket)
                .user(agentUser)
                .commentText("Please provide system model and error log")
                .build());

        List<TicketComment> comments = ticketCommentRepository.findByTicket_IdOrderByCreatedAtAsc(ticket.getId());
        assertThat(comments).hasSize(1);
        assertThat(comments.get(0).getCommentText()).contains("system model");

        // 4. Add Ticket Attachment
        TicketAttachment attachment = ticketAttachmentRepository.save(TicketAttachment.builder()
                .ticket(ticket)
                .uploadedBy(customerUser)
                .originalFileName("error_screen.png")
                .storedFileName("uuid-123-error_screen.png")
                .filePath("/uploads/uuid-123-error_screen.png")
                .fileType("image/png")
                .fileSize(204800L)
                .build());

        List<TicketAttachment> attachments = ticketAttachmentRepository.findByTicket_Id(ticket.getId());
        assertThat(attachments).hasSize(1);
        assertThat(attachments.get(0).getOriginalFileName()).isEqualTo("error_screen.png");

        // 5. Add Ticket Status History
        TicketStatusHistory history = ticketStatusHistoryRepository.save(TicketStatusHistory.builder()
                .ticket(ticket)
                .oldStatus(TicketStatus.OPEN)
                .newStatus(TicketStatus.ASSIGNED)
                .changedBy(agentUser)
                .comment("Assigned to Alice Turner")
                .build());

        List<TicketStatusHistory> historyList = ticketStatusHistoryRepository.findByTicket_IdOrderByCreatedAtAsc(ticket.getId());
        assertThat(historyList).hasSize(1);
        assertThat(historyList.get(0).getNewStatus()).isEqualTo(TicketStatus.ASSIGNED);

        // 6. Add Feedback
        Feedback feedback = feedbackRepository.save(Feedback.builder()
                .ticket(ticket)
                .customer(customer)
                .rating(5)
                .comment("Excellent support!")
                .build());

        Optional<Feedback> foundFeedback = feedbackRepository.findByTicket_Id(ticket.getId());
        assertThat(foundFeedback).isPresent();
        assertThat(foundFeedback.get().getRating()).isEqualTo(5);
    }

    @Test
    @DisplayName("NotificationRepository and ActivityLogRepository")
    void testNotificationsAndActivityLogs() {
        // Notification
        notificationRepository.save(Notification.builder()
                .user(customerUser)
                .title("Ticket Created")
                .message("Your ticket SR-2026-00001 was submitted.")
                .type("TICKET")
                .isRead(false)
                .build());

        List<Notification> unread = notificationRepository.findByUser_IdAndIsReadFalseOrderByCreatedAtDesc(customerUser.getId());
        assertThat(unread).hasSize(1);
        assertThat(notificationRepository.countByUser_IdAndIsReadFalse(customerUser.getId())).isEqualTo(1);

        // Activity Log
        activityLogRepository.save(ActivityLog.builder()
                .user(customerUser)
                .action("CREATE_TICKET")
                .entityType("Ticket")
                .entityId(100L)
                .description("Customer created new ticket")
                .build());

        List<ActivityLog> logs = activityLogRepository.findByUser_IdOrderByCreatedAtDesc(customerUser.getId());
        assertThat(logs).hasSize(1);
        assertThat(logs.get(0).getAction()).isEqualTo("CREATE_TICKET");
    }
}
