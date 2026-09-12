package com.example.support.mapper;

import com.example.support.dto.request.CustomerRequest;
import com.example.support.dto.request.ServiceRequest;
import com.example.support.dto.request.TicketCreateRequest;
import com.example.support.dto.request.UserCreateRequest;
import com.example.support.dto.response.*;
import com.example.support.entity.*;
import com.example.support.entity.enums.TicketPriority;
import com.example.support.entity.enums.TicketStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class MapperMappingTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private ServiceMapper serviceMapper;

    @Autowired
    private TicketMapper ticketMapper;

    @Autowired
    private TicketCommentMapper ticketCommentMapper;

    @Autowired
    private FeedbackMapper feedbackMapper;

    @Autowired
    private NotificationMapper notificationMapper;

    @Autowired
    private ActivityLogMapper activityLogMapper;

    @Test
    @DisplayName("UserMapper: Entity to UserResponse (excludes password)")
    void testUserMapper() {
        Role role = Role.builder().id(1L).name("ROLE_ADMIN").build();
        User user = User.builder()
                .id(10L)
                .firstName("John")
                .lastName("Doe")
                .email("john@example.com")
                .passwordHash("secret-bcrypt-hash")
                .phone("+1-555-0100")
                .role(role)
                .isActive(true)
                .build();

        UserResponse response = userMapper.toResponse(user);
        assertThat(response.getId()).isEqualTo(10L);
        assertThat(response.getFirstName()).isEqualTo("John");
        assertThat(response.getLastName()).isEqualTo("Doe");
        assertThat(response.getEmail()).isEqualTo("john@example.com");
        assertThat(response.getRoleName()).isEqualTo("ROLE_ADMIN");
        assertThat(response.getRoleId()).isEqualTo(1L);

        // Verify request to entity mapping
        UserCreateRequest request = UserCreateRequest.builder()
                .firstName("Jane")
                .lastName("Smith")
                .email("jane@example.com")
                .password("Password123")
                .phone("+1-555-0200")
                .roleId(2L)
                .build();

        User entity = userMapper.toEntity(request);
        assertThat(entity.getFirstName()).isEqualTo("Jane");
        assertThat(entity.getEmail()).isEqualTo("jane@example.com");
        assertThat(entity.getIsActive()).isTrue();
    }

    @Test
    @DisplayName("TicketMapper: Entity to TicketResponse and Request to Entity")
    void testTicketMapper() {
        Role role = Role.builder().name("ROLE_CUSTOMER").build();
        User user = User.builder().firstName("John").lastName("Doe").role(role).build();
        Customer customer = Customer.builder().id(1L).user(user).customerCode("CUST-001").build();
        Service service = Service.builder().id(2L).serviceName("Network Support").build();

        Ticket ticket = Ticket.builder()
                .id(100L)
                .ticketNumber("SR-2026-00001")
                .customer(customer)
                .service(service)
                .title("Cannot connect to VPN")
                .description("VPN connection timeout error")
                .priority(TicketPriority.HIGH)
                .status(TicketStatus.OPEN)
                .build();

        TicketResponse response = ticketMapper.toResponse(ticket);
        assertThat(response.getId()).isEqualTo(100L);
        assertThat(response.getTicketNumber()).isEqualTo("SR-2026-00001");
        assertThat(response.getCustomerName()).isEqualTo("John Doe");
        assertThat(response.getServiceName()).isEqualTo("Network Support");
        assertThat(response.getPriority()).isEqualTo(TicketPriority.HIGH);
        assertThat(response.getStatus()).isEqualTo(TicketStatus.OPEN);

        TicketCreateRequest createReq = TicketCreateRequest.builder()
                .customerId(1L)
                .serviceId(2L)
                .title("New Issue")
                .description("Issue details")
                .priority(TicketPriority.LOW)
                .build();

        Ticket newTicket = ticketMapper.toEntity(createReq);
        assertThat(newTicket.getTitle()).isEqualTo("New Issue");
        assertThat(newTicket.getPriority()).isEqualTo(TicketPriority.LOW);
        assertThat(newTicket.getStatus()).isEqualTo(TicketStatus.OPEN);
    }

    @Test
    @DisplayName("FeedbackMapper: Entity to Response mapping")
    void testFeedbackMapper() {
        User user = User.builder().firstName("Alice").lastName("Customer").build();
        Customer customer = Customer.builder().id(5L).user(user).build();
        Ticket ticket = Ticket.builder().id(12L).ticketNumber("SR-2026-00012").build();

        Feedback feedback = Feedback.builder()
                .id(1L)
                .ticket(ticket)
                .customer(customer)
                .rating(5)
                .comment("Great service!")
                .build();

        FeedbackResponse res = feedbackMapper.toResponse(feedback);
        assertThat(res.getTicketNumber()).isEqualTo("SR-2026-00012");
        assertThat(res.getCustomerName()).isEqualTo("Alice Customer");
        assertThat(res.getRating()).isEqualTo(5);
    }
}
