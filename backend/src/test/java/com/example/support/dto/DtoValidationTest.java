package com.example.support.dto;

import com.example.support.dto.request.FeedbackRequest;
import com.example.support.dto.request.TicketCreateRequest;
import com.example.support.dto.request.UserCreateRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class DtoValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("UserCreateRequest: Validation succeeds for valid input")
    void testUserCreateRequestValid() {
        UserCreateRequest request = UserCreateRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("Password123!")
                .roleId(1L)
                .build();

        Set<ConstraintViolation<UserCreateRequest>> violations = validator.validate(request);
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("UserCreateRequest: Validation fails for blank fields and invalid email")
    void testUserCreateRequestInvalid() {
        UserCreateRequest request = UserCreateRequest.builder()
                .firstName("")
                .lastName("")
                .email("not-an-email")
                .password("short")
                .roleId(null)
                .build();

        Set<ConstraintViolation<UserCreateRequest>> violations = validator.validate(request);
        assertThat(violations).hasSizeGreaterThanOrEqualTo(4);
    }

    @Test
    @DisplayName("TicketCreateRequest: Validation enforces required fields")
    void testTicketCreateRequestValidation() {
        TicketCreateRequest invalidReq = TicketCreateRequest.builder()
                .title("")
                .description("")
                .customerId(null)
                .serviceId(null)
                .build();

        Set<ConstraintViolation<TicketCreateRequest>> violations = validator.validate(invalidReq);
        assertThat(violations).hasSize(4);
    }

    @Test
    @DisplayName("FeedbackRequest: Rating bounds check (1 to 5)")
    void testFeedbackRequestValidation() {
        FeedbackRequest valid = FeedbackRequest.builder()
                .ticketId(1L)
                .customerId(1L)
                .rating(5)
                .build();

        assertThat(validator.validate(valid)).isEmpty();

        FeedbackRequest invalidLow = FeedbackRequest.builder()
                .ticketId(1L)
                .customerId(1L)
                .rating(0)
                .build();
        assertThat(validator.validate(invalidLow)).isNotEmpty();

        FeedbackRequest invalidHigh = FeedbackRequest.builder()
                .ticketId(1L)
                .customerId(1L)
                .rating(6)
                .build();
        assertThat(validator.validate(invalidHigh)).isNotEmpty();
    }
}
