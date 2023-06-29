package com.chatchatabc.parking.admin.application.mapper;

import com.chatchatabc.parking.admin.application.rest.UserController;
import com.chatchatabc.parking.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface UserMapper {
    /**
     * Map User From Request
     */
    @Mappings({
            @Mapping(target = "email", source = "request.email"),
            @Mapping(target = "phone", source = "request.phone"),
            @Mapping(target = "username", source = "request.username"),
            @Mapping(target = "roles", ignore = true),
            @Mapping(target = "enabled", ignore = true),
    })
    void mapRequestToUser(
            UserController.UserCreateRequest request,
            @MappingTarget User user
    );

    /**
     * Update User Map Request
     */
    @Mappings({
            @Mapping(target = "email", source = "request.email"),
            @Mapping(target = "phone", source = "request.phone"),
            @Mapping(target = "username", source = "request.username"),
            @Mapping(target = "firstName", source = "request.firstName"),
            @Mapping(target = "lastName", source = "request.lastName"),
            @Mapping(target = "roles", ignore = true)
    })
    void updateUserFromUpdateRequest(
            UserController.UserUpdateRequest request,
            @MappingTarget User user
    );
}
