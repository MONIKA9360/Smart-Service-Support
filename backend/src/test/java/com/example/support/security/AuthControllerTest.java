package com.example.support.security;

import com.example.support.dto.request.LoginRequest;
import com.example.support.dto.request.RegisterRequest;
import com.example.support.entity.Customer;
import com.example.support.entity.Role;
import com.example.support.entity.User;
import com.example.support.repository.CustomerRepository;
import com.example.support.repository.RoleRepository;
import com.example.support.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider tokenProvider;

    private Role customerRole;
    private Role adminRole;
    private Role agentRole;

    @BeforeEach
    void setUp() {
        customerRole = roleRepository.findByName("ROLE_CUSTOMER")
                .orElseGet(() -> roleRepository.save(Role.builder().name("ROLE_CUSTOMER").description("Customer").build()));

        adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseGet(() -> roleRepository.save(Role.builder().name("ROLE_ADMIN").description("Admin").build()));

        agentRole = roleRepository.findByName("ROLE_AGENT")
                .orElseGet(() -> roleRepository.save(Role.builder().name("ROLE_AGENT").description("Agent").build()));
    }

    @Test
    @DisplayName("Verify BCrypt hash verification for seed password Admin@123")
    void testBCryptPasswordVerification() {
        String hash = "$2a$10$YukCzCnR/elzZB0SyLd4ZulPOwxVdV0u/drrVhs.H4uhWIq/I/F9O";
        assertThat(passwordEncoder.matches("Admin@123", hash)).isTrue();
    }

    @Test
    @DisplayName("1. Successful Customer registration creates User and Customer profile")
    void testRegisterSuccess() throws Exception {
        RegisterRequest request = RegisterRequest.builder()
                .firstName("Alice")
                .lastName("Wonderland")
                .email("alice.wonder@example.com")
                .phone("+1-555-8888")
                .password("SecurePassword123!")
                .build();

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.email").value("alice.wonder@example.com"))
                .andExpect(jsonPath("$.data.roleName").value("ROLE_CUSTOMER"))
                .andExpect(jsonPath("$.data.passwordHash").doesNotExist())
                .andExpect(jsonPath("$.data.password").doesNotExist());

        // Verify database state
        User savedUser = userRepository.findByEmail("alice.wonder@example.com").orElseThrow();
        assertThat(savedUser.getRole().getName()).isEqualTo("ROLE_CUSTOMER");
        assertThat(passwordEncoder.matches("SecurePassword123!", savedUser.getPasswordHash())).isTrue();

        Customer customerProfile = customerRepository.findByUser_Id(savedUser.getId()).orElseThrow();
        assertThat(customerProfile.getCustomerCode()).startsWith("CUST-");
    }

    @Test
    @DisplayName("2. Duplicate email registration is rejected with 409 Conflict")
    void testRegisterDuplicateEmail() throws Exception {
        userRepository.save(User.builder()
                .firstName("Existing")
                .lastName("User")
                .email("duplicate@example.com")
                .passwordHash(passwordEncoder.encode("Password123!"))
                .role(customerRole)
                .isActive(true)
                .build());

        RegisterRequest request = RegisterRequest.builder()
                .firstName("Another")
                .lastName("User")
                .email("duplicate@example.com")
                .password("Password123!")
                .build();

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value("DUPLICATE_RESOURCE"));
    }

    @Test
    @DisplayName("3. Invalid registration request returns 400 Bad Request with field errors")
    void testRegisterValidationFailure() throws Exception {
        RegisterRequest request = RegisterRequest.builder()
                .firstName("")
                .lastName("")
                .email("invalid-email")
                .password("short")
                .build();

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.details.email").exists())
                .andExpect(jsonPath("$.details.password").exists());
    }

    @Test
    @DisplayName("4. Login success returns valid JWT token and UserResponse")
    void testLoginSuccess() throws Exception {
        userRepository.save(User.builder()
                .firstName("John")
                .lastName("Customer")
                .email("john.auth@example.com")
                .passwordHash(passwordEncoder.encode("Admin@123"))
                .role(customerRole)
                .isActive(true)
                .build());

        LoginRequest request = LoginRequest.builder()
                .email("john.auth@example.com")
                .password("Admin@123")
                .build();

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.accessToken").isString())
                .andExpect(jsonPath("$.data.tokenType").value("Bearer"))
                .andExpect(jsonPath("$.data.expiresIn").value(900))
                .andExpect(jsonPath("$.data.user.email").value("john.auth@example.com"))
                .andExpect(jsonPath("$.data.user.roleName").value("ROLE_CUSTOMER"))
                .andExpect(jsonPath("$.data.user.passwordHash").doesNotExist());
    }

    @Test
    @DisplayName("5. Login with wrong password returns 401 Unauthorized")
    void testLoginWrongPassword() throws Exception {
        userRepository.save(User.builder()
                .firstName("Jane")
                .lastName("Customer")
                .email("jane.auth@example.com")
                .passwordHash(passwordEncoder.encode("CorrectPassword123!"))
                .role(customerRole)
                .isActive(true)
                .build());

        LoginRequest request = LoginRequest.builder()
                .email("jane.auth@example.com")
                .password("WrongPassword123!")
                .build();

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value("UNAUTHORIZED"));
    }

    @Test
    @DisplayName("6. Login with unknown email returns 401 Unauthorized")
    void testLoginUnknownEmail() throws Exception {
        LoginRequest request = LoginRequest.builder()
                .email("nonexistent@example.com")
                .password("SomePassword123!")
                .build();

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    @DisplayName("7. Deactivated user login returns 401 Unauthorized")
    void testLoginDeactivatedUser() throws Exception {
        userRepository.save(User.builder()
                .firstName("Inactive")
                .lastName("User")
                .email("inactive@example.com")
                .passwordHash(passwordEncoder.encode("Admin@123"))
                .role(customerRole)
                .isActive(false)
                .build());

        LoginRequest request = LoginRequest.builder()
                .email("inactive@example.com")
                .password("Admin@123")
                .build();

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    @DisplayName("8. GET /api/auth/me with valid JWT returns authenticated user profile")
    void testGetCurrentUserSuccess() throws Exception {
        User user = userRepository.save(User.builder()
                .firstName("Current")
                .lastName("User")
                .email("current@example.com")
                .passwordHash(passwordEncoder.encode("Admin@123"))
                .role(customerRole)
                .isActive(true)
                .build());

        String token = tokenProvider.generateTokenFromUserId(user.getId(), user.getEmail(), user.getRole().getName());

        mockMvc.perform(get("/auth/me")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.email").value("current@example.com"))
                .andExpect(jsonPath("$.data.firstName").value("Current"))
                .andExpect(jsonPath("$.data.roleName").value("ROLE_CUSTOMER"))
                .andExpect(jsonPath("$.data.passwordHash").doesNotExist());
    }

    @Test
    @DisplayName("9. GET /api/auth/me without JWT returns 401 Unauthorized")
    void testGetCurrentUserMissingToken() throws Exception {
        mockMvc.perform(get("/auth/me"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value("UNAUTHORIZED"));
    }

    @Test
    @DisplayName("10. GET /api/auth/me with invalid JWT returns 401 Unauthorized")
    void testGetCurrentUserInvalidToken() throws Exception {
        mockMvc.perform(get("/auth/me")
                        .header("Authorization", "Bearer invalid.jwt.token"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false));
    }
}
