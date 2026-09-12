package com.example.support.mapper;

import com.example.support.dto.request.FeedbackRequest;
import com.example.support.dto.response.FeedbackResponse;
import com.example.support.entity.Feedback;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FeedbackMapper {

    @Mapping(target = "ticketId", source = "ticket.id")
    @Mapping(target = "ticketNumber", source = "ticket.ticketNumber")
    @Mapping(target = "customerId", source = "customer.id")
    @Mapping(target = "customerName", source = "customer", qualifiedByName = "feedbackCustomerToName")
    FeedbackResponse toResponse(Feedback feedback);

    List<FeedbackResponse> toResponseList(List<Feedback> feedbackList);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ticket", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Feedback toEntity(FeedbackRequest request);

    @Named("feedbackCustomerToName")
    default String feedbackCustomerToName(com.example.support.entity.Customer customer) {
        if (customer == null || customer.getUser() == null) return null;
        return customer.getUser().getFirstName() + " " + customer.getUser().getLastName();
    }
}
