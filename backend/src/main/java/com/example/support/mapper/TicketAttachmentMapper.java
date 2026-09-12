package com.example.support.mapper;

import com.example.support.dto.response.TicketAttachmentResponse;
import com.example.support.entity.TicketAttachment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TicketAttachmentMapper {

    @Mapping(target = "ticketId", source = "ticket.id")
    @Mapping(target = "uploadedById", source = "uploadedBy.id")
    @Mapping(target = "uploadedByName", source = "uploadedBy", qualifiedByName = "uploadedByToName")
    TicketAttachmentResponse toResponse(TicketAttachment attachment);

    List<TicketAttachmentResponse> toResponseList(List<TicketAttachment> attachments);

    @Named("uploadedByToName")
    default String uploadedByToName(com.example.support.entity.User user) {
        if (user == null) return null;
        return user.getFirstName() + " " + user.getLastName();
    }
}
