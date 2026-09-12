package com.example.support.mapper;

import com.example.support.dto.request.TicketCommentRequest;
import com.example.support.dto.response.TicketCommentResponse;
import com.example.support.entity.TicketComment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TicketCommentMapper {

    @Mapping(target = "ticketId", source = "ticket.id")
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userName", source = "user", qualifiedByName = "userToFullName")
    @Mapping(target = "userRole", source = "user.role.name")
    TicketCommentResponse toResponse(TicketComment comment);

    List<TicketCommentResponse> toResponseList(List<TicketComment> comments);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ticket", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    TicketComment toEntity(TicketCommentRequest request);

    @Named("userToFullName")
    default String userToFullName(com.example.support.entity.User user) {
        if (user == null) return null;
        return user.getFirstName() + " " + user.getLastName();
    }
}
