package com.chatchatabc.parking.api.application.mapper

import com.chatchatabc.parking.api.application.rest.UserController
import com.chatchatabc.parking.domain.model.User
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import org.mapstruct.Mappings

@Mapper(componentModel = "spring")
interface UserMapper {

    /**
     * Update user from request
     */
    @Mappings(
        Mapping(target = "email", source = "request.email"),
        Mapping(target = "username", source = "request.username"),
        Mapping(target = "firstName", source = "request.firstName"),
        Mapping(target = "lastName", source = "request.lastName")
    )
    fun updateUserFromUpdateProfileRequest(
        request: UserController.UserProfileUpdateRequest,
        @MappingTarget user: User
    )
}