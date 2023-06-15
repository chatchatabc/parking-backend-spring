package com.chatchatabc.parking.admin.application.mapper

import com.chatchatabc.parking.admin.application.rest.RouteController
import com.chatchatabc.parking.domain.model.Route
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import org.mapstruct.Mappings

@Mapper(componentModel = "spring")
interface RouteMapper {
    /**
     * Route Create Mapper
     */
    @Mappings(
        Mapping(target = "name", source = "request.name"),
        Mapping(target = "description", source = "request.description"),
        Mapping(target = "status", source = "request.status"),
    )
    fun createRouteFromCreateRequest(
        request: RouteController.RouteCreateRequest,
        @MappingTarget route: Route
    )

    /**
     * Route Update Mapper
     */
    @Mappings(
        Mapping(target = "name", source = "request.name"),
        Mapping(target = "description", source = "request.description"),
        Mapping(target = "status", source = "request.status"),
    )
    fun updateRouteFromUpdateRequest(
        request: RouteController.RouteUpdateRequest,
        @MappingTarget route: Route
    )
}