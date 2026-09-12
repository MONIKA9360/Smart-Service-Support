package com.example.support.mapper;

import com.example.support.dto.request.TicketCreateRequest;
import com.example.support.dto.response.TicketResponse;
import com.example.support.entity.Ticket;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TicketMapper {

    @Mapping(target = "customerId", source = "customer.id")
    @Mapping(target = "customerName", source = "customer", qualifiedByName = "customerToName")
    @Mapping(target = "customerCode", source = "customer.customerCode")
    @Mapping(target = "assignedEmployeeId", source = "assignedEmployee.id")
    @Mapping(target = "assignedEmployeeName", source = "assignedEmployee", qualifiedByName = "employeeToName")
    @Mapping(target = "serviceId", source = "service.id")
    @Mapping(target = "serviceName", source = "service.serviceName")
    TicketResponse toResponse(Ticket ticket);

    List<TicketResponse> toResponseList(List<Ticket> tickets);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ticketNumber", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "assignedEmployee", ignore = true)
    @Mapping(target = "service", ignore = true)
    @Mapping(target = "status", constant = "OPEN")
    @Mapping(target = "resolution", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "assignedAt", ignore = true)
    @Mapping(target = "resolvedAt", ignore = true)
    @Mapping(target = "closedAt", ignore = true)
    Ticket toEntity(TicketCreateRequest request);

    @Named("customerToName")
    default String customerToName(com.example.support.entity.Customer customer) {
        if (customer == null || customer.getUser() == null) return null;
        return customer.getUser().getFirstName() + " " + customer.getUser().getLastName();
    }

    @Named("employeeToName")
    default String employeeToName(com.example.support.entity.Employee employee) {
        if (employee == null || employee.getUser() == null) return null;
        return employee.getUser().getFirstName() + " " + employee.getUser().getLastName();
    }
}
