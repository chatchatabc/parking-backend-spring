package com.chatchatabc.parking.admin.application.mapper;

import com.chatchatabc.parking.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    record UserCreateDTO(
            String email,
            String phone,
            String username,
            List<String> roles,
            boolean enabled
    ) {
    }

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
            UserCreateDTO request,
            @MappingTarget User user
    );

    record UserUpdateDTO(
            String email,
            String phone,
            String username,
            String firstName,
            String lastName,
            List<String> roles
    ) {
    }

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
            UserUpdateDTO request,
            @MappingTarget User user
    );
}
