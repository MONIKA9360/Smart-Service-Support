package com.example.support.service.impl;

import com.example.support.dto.request.LoginRequest;
import com.example.support.dto.request.RegisterRequest;
import com.example.support.dto.response.AuthResponse;
import com.example.support.dto.response.UserResponse;
import com.example.support.entity.Customer;
import com.example.support.entity.Role;
import com.example.support.entity.User;
import com.example.support.exception.AccountInactiveException;
import com.example.support.exception.DuplicateResourceException;
import com.example.support.exception.ResourceNotFoundException;
import com.example.support.exception.UnauthorizedException;
import com.example.support.mapper.UserMapper;
import com.example.support.repository.CustomerRepository;
import com.example.support.repository.RoleRepository;
import com.example.support.repository.UserRepository;
import com.example.support.security.JwtTokenProvider;
import com.example.support.security.UserPrincipal;
import com.example.support.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Implementation of authentication, registration, and user identity operations.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserResponse register(RegisterRequest request) {
        // 1. Check for email uniqueness
        if (userRepository.existsByEmail(request.getEmail().trim().toLowerCase())) {
            throw new DuplicateResourceException("An account with email " + request.getEmail() + " already exists");
        }

        // 2. Fetch or create ROLE_CUSTOMER (public registration ALWAYS assigns ROLE_CUSTOMER)
        Role customerRole = roleRepository.findByName("ROLE_CUSTOMER")
                .orElseGet(() -> roleRepository.save(
                        Role.builder()
                                .name("ROLE_CUSTOMER")
                                .description("Customer user")
                                .build()
                ));

        // 3. Create User entity with BCrypt password hash
        User user = User.builder()
                .firstName(request.getFirstName().trim())
                .lastName(request.getLastName().trim())
                .email(request.getEmail().trim().toLowerCase())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone() != null ? request.getPhone().trim() : null)
                .role(customerRole)
                .isActive(true)
                .build();

        User savedUser = userRepository.save(user);

        // 4. Generate unique customer code (e.g., CUST-00001 or collision-resistant code)
        String customerCode = generateUniqueCustomerCode(savedUser.getId());

        // 5. Create Customer profile record
        Customer customer = Customer.builder()
                .user(savedUser)
                .customerCode(customerCode)
                .build();

        customerRepository.save(customer);

        log.info("Customer registered successfully with ID {} and code {}", savedUser.getId(), customerCode);
        return userMapper.toResponse(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        String email = request.getEmail().trim().toLowerCase();

        // 1. Authenticate credentials with Spring Security
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, request.getPassword())
            );
        } catch (BadCredentialsException ex) {
            throw new UnauthorizedException("Invalid email or password");
        } catch (DisabledException ex) {
            throw new UnauthorizedException("Account has been deactivated. Please contact support.");
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

        // 2. Verify account is active
        if (!Boolean.TRUE.equals(principal.getIsActive())) {
            throw new UnauthorizedException("Account has been deactivated. Please contact support.");
        }

        // 3. Generate JWT access token
        String jwt = tokenProvider.generateToken(authentication);
        long expiresInSeconds = tokenProvider.getExpirationMs() / 1000;

        // 4. Retrieve clean User entity to map into UserResponse
        User user = userRepository.findById(principal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return AuthResponse.builder()
                .accessToken(jwt)
                .tokenType("Bearer")
                .expiresIn(expiresInSeconds)
                .user(userMapper.toResponse(user))
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getCurrentUser(UserPrincipal principal) {
        if (principal == null) {
            throw new UnauthorizedException("User is not authenticated");
        }

        User user = userRepository.findById(principal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + principal.getId()));

        if (!Boolean.TRUE.equals(user.getIsActive())) {
            throw new UnauthorizedException("Account is deactivated");
        }

        return userMapper.toResponse(user);
    }

    private String generateUniqueCustomerCode(Long userId) {
        String candidate = String.format("CUST-%05d", userId);
        if (!customerRepository.existsByCustomerCode(candidate)) {
            return candidate;
        }
        // Fallback in case of code collision
        String randomSuffix = UUID.randomUUID().toString().substring(0, 5).toUpperCase();
        return "CUST-" + randomSuffix;
    }
}
