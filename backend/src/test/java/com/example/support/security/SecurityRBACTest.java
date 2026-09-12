package com.example.support.security;

import com.example.support.entity.Role;
import com.example.support.entity.User;
import com.example.support.repository.RoleRepository;
import com.example.support.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class SecurityRBACTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider tokenProvider;

    private User adminUser;
    private User agentUser;
    private User customerUser;
    private User inactiveUser;

    private String adminToken;
    private String agentToken;
    private String customerToken;
    private String inactiveToken;

    @BeforeEach
    void setUp() {
        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseGet(() -> roleRepository.save(Role.builder().name("ROLE_ADMIN").build()));

        Role agentRole = roleRepository.findByName("ROLE_AGENT")
                .orElseGet(() -> roleRepository.save(Role.builder().name("ROLE_AGENT").build()));

        Role customerRole = roleRepository.findByName("ROLE_CUSTOMER")
                .orElseGet(() -> roleRepository.save(Role.builder().name("ROLE_CUSTOMER").build()));

        adminUser = userRepository.save(User.builder()
                .firstName("Admin")
                .lastName("User")
                .email("admin.rbac@example.com")
                .passwordHash(passwordEncoder.encode("Admin@123"))
                .role(adminRole)
                .isActive(true)
                .build());

        agentUser = userRepository.save(User.builder()
                .firstName("Agent")
                .lastName("User")
                .email("agent.rbac@example.com")
                .passwordHash(passwordEncoder.encode("Admin@123"))
                .role(agentRole)
                .isActive(true)
                .build());

        customerUser = userRepository.save(User.builder()
                .firstName("Customer")
                .lastName("User")
                .email("customer.rbac@example.com")
                .passwordHash(passwordEncoder.encode("Admin@123"))
                .role(customerRole)
                .isActive(true)
                .build());

        inactiveUser = userRepository.save(User.builder()
                .firstName("Deactivated")
                .lastName("User")
                .email("deactivated.rbac@example.com")
                .passwordHash(passwordEncoder.encode("Admin@123"))
                .role(adminRole)
                .isActive(false)
                .build());

        adminToken = tokenProvider.generateTokenFromUserId(adminUser.getId(), adminUser.getEmail(), "ROLE_ADMIN");
        agentToken = tokenProvider.generateTokenFromUserId(agentUser.getId(), agentUser.getEmail(), "ROLE_AGENT");
        customerToken = tokenProvider.generateTokenFromUserId(customerUser.getId(), customerUser.getEmail(), "ROLE_CUSTOMER");
        inactiveToken = tokenProvider.generateTokenFromUserId(inactiveUser.getId(), inactiveUser.getEmail(), "ROLE_ADMIN");
    }

    @Test
    @DisplayName("Admin user can access /admin/check")
    void testAdminCanAccessAdminEndpoint() throws Exception {
        mockMvc.perform(get("/admin/check")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @DisplayName("Customer user accessing /admin/check is forbidden (403)")
    void testCustomerCannotAccessAdminEndpoint() throws Exception {
        mockMvc.perform(get("/admin/check")
                        .header("Authorization", "Bearer " + customerToken))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value("FORBIDDEN"));
    }

    @Test
    @DisplayName("Agent user accessing /admin/check is forbidden (403)")
    void testAgentCannotAccessAdminEndpoint() throws Exception {
        mockMvc.perform(get("/admin/check")
                        .header("Authorization", "Bearer " + agentToken))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value("FORBIDDEN"));
    }

    @Test
    @DisplayName("Agent user can access /agent/check")
    void testAgentCanAccessAgentEndpoint() throws Exception {
        mockMvc.perform(get("/agent/check")
                        .header("Authorization", "Bearer " + agentToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @DisplayName("Customer user accessing /agent/check is forbidden (403)")
    void testCustomerCannotAccessAgentEndpoint() throws Exception {
        mockMvc.perform(get("/agent/check")
                        .header("Authorization", "Bearer " + customerToken))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    @DisplayName("Customer user can access /customer/check")
    void testCustomerCanAccessCustomerEndpoint() throws Exception {
        mockMvc.perform(get("/customer/check")
                        .header("Authorization", "Bearer " + customerToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @DisplayName("Unauthenticated request to protected endpoint returns 401 Unauthorized")
    void testUnauthenticatedAccessReturns401() throws Exception {
        mockMvc.perform(get("/admin/check"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value("UNAUTHORIZED"));
    }

    @Test
    @DisplayName("Deactivated user token is rejected with 401 Unauthorized")
    void testDeactivatedUserTokenRejected() throws Exception {
        mockMvc.perform(get("/admin/check")
                        .header("Authorization", "Bearer " + inactiveToken))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false));
    }
}
