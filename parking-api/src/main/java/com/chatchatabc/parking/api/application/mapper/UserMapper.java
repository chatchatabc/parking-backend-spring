package com.chatchatabc.parking.api.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface UserMapper {

    record UserMapDTO(
            String email,
            String firstName,
            String lastName
    ) {
    }

    /**
     * User Mapper
     */
    @Mappings({
            @Mapping(target = "email", source = "request.email"),
            @Mapping(target = "firstName", source = "request.firstName"),
            @Mapping(target = "lastName", source = "request.lastName")
    })
    void mapRequestToUser(
            UserMapDTO request,
            @MappingTarget com.chatchatabc.parking.domain.model.User user
    );
}
