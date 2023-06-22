package com.chatchatabc.parking.admin.application.mapper

import com.chatchatabc.parking.admin.application.rest.RouteNodeController
import com.chatchatabc.parking.domain.model.RouteNode
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import org.mapstruct.Mappings

@Mapper(componentModel = "spring")
interface RouteNodeMapper {
    /**
     * Route Node Create Mapper
     */
    @Mappings(
        Mapping(target = "latitude", source = "request.latitude"),
        Mapping(target = "longitude", source = "request.longitude"),
        Mapping(target = "poi", source = "request.poi"),
    )
    fun createRouteNodeFromCreateRequest(
        request: RouteNodeController.RouteNodeCreateRequest,
        @MappingTarget routeNode: RouteNode
    )

    /**
     * Route Node Update Mapper
     */
    @Mappings(
        Mapping(target = "latitude", source = "request.latitude"),
        Mapping(target = "longitude", source = "request.longitude"),
        Mapping(target = "poi", source = "request.poi"),
    )
    fun updateRouteNodeFromUpdateRequest(
        request: RouteNodeController.RouteNodeUpdateRequest,
        @MappingTarget routeNode: RouteNode
    )
}