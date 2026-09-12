package com.example.support.mapper;

import com.example.support.dto.request.ServiceRequest;
import com.example.support.dto.response.ServiceResponse;
import com.example.support.entity.Service;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ServiceMapper {

    ServiceResponse toResponse(Service service);

    List<ServiceResponse> toResponseList(List<Service> services);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Service toEntity(ServiceRequest request);
}
