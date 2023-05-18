package com.chatchatabc.parking.admin.application.mapper

import com.chatchatabc.parking.admin.application.rest.MemberController
import com.chatchatabc.parking.domain.model.Member
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import org.mapstruct.Mappings

@Mapper(componentModel = "spring")
interface MemberMapper {

    /**
     * Update member from request
     */
    @Mappings(
        Mapping(target = "email", source = "request.email"),
        Mapping(target = "username", source = "request.username"),
        Mapping(target = "firstName", source = "request.firstName"),
        Mapping(target = "lastName", source = "request.lastName")
    )
    fun updateMemberFromUpdateRequest(
        request: MemberController.MemberUpdateRequest,
        @MappingTarget member: Member
    )
}