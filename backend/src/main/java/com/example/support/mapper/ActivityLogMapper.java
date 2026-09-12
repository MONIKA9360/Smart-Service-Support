package com.example.support.mapper;

import com.example.support.dto.response.ActivityLogResponse;
import com.example.support.entity.ActivityLog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ActivityLogMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userName", source = "user", qualifiedByName = "userToFullName")
    ActivityLogResponse toResponse(ActivityLog log);

    List<ActivityLogResponse> toResponseList(List<ActivityLog> logs);

    @Named("userToFullName")
    default String userToFullName(com.example.support.entity.User user) {
        if (user == null) return null;
        return user.getFirstName() + " " + user.getLastName();
    }
}
