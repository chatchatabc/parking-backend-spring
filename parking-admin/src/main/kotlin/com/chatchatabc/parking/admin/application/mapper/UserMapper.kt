package com.chatchatabc.parking.admin.application.mapper

import com.chatchatabc.parking.admin.application.rest.UserController
import com.chatchatabc.parking.domain.model.User
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import org.mapstruct.Mappings

@Mapper(componentModel = "spring")
interface UserMapper {
    /**
     * Create user from request
     */
    @Mappings(
        Mapping(target = "email", source = "request.email"),
        Mapping(target = "phone", source = "request.phone"),
        Mapping(target = "username", source = "request.username"),
        Mapping(target = "roles", ignore = true),
    )
    fun createUserFromCreateRequest(
        request: UserController.UserCreateRequest,
        @MappingTarget user: User
    ): User


    /**
     * Update user from request
     */
    @Mappings(
        Mapping(target = "email", source = "request.email"),
        Mapping(target = "phone", source = "request.phone"),
        Mapping(target = "username", source = "request.username"),
        Mapping(target = "firstName", source = "request.firstName"),
        Mapping(target = "lastName", source = "request.lastName"),
        Mapping(target = "roles", ignore = true)
    )
    fun updateUserFromUpdateRequest(
        request: UserController.UserUpdateRequest,
        @MappingTarget user: User
    )
}