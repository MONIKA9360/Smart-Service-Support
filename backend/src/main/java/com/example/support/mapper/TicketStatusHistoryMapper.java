package com.example.support.mapper;

import com.example.support.dto.response.TicketStatusHistoryResponse;
import com.example.support.entity.TicketStatusHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TicketStatusHistoryMapper {

    @Mapping(target = "ticketId", source = "ticket.id")
    @Mapping(target = "changedById", source = "changedBy.id")
    @Mapping(target = "changedByName", source = "changedBy", qualifiedByName = "changedByToName")
    TicketStatusHistoryResponse toResponse(TicketStatusHistory history);

    List<TicketStatusHistoryResponse> toResponseList(List<TicketStatusHistory> historyList);

    @Named("changedByToName")
    default String changedByToName(com.example.support.entity.User user) {
        if (user == null) return null;
        return user.getFirstName() + " " + user.getLastName();
    }
}
