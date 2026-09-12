package com.example.support.service;

import com.example.support.dto.request.LoginRequest;
import com.example.support.dto.request.RegisterRequest;
import com.example.support.dto.response.AuthResponse;
import com.example.support.dto.response.UserResponse;
import com.example.support.security.UserPrincipal;

/**
 * Authentication and customer registration service interface.
 */
public interface AuthService {

    UserResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    UserResponse getCurrentUser(UserPrincipal principal);
}
