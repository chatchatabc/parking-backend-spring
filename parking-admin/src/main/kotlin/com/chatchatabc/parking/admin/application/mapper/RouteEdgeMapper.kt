package com.chatchatabc.parking.admin.application.mapper

import com.chatchatabc.parking.admin.application.rest.RouteEdgeController
import com.chatchatabc.parking.domain.model.RouteEdge
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import org.mapstruct.Mappings

@Mapper(componentModel = "spring")
interface RouteEdgeMapper {
    /**
     * Route Edge Create Mapper
     */
    @Mappings(
        Mapping(target = "routeId", source = "request.routeId"),
        Mapping(target = "nodeFrom", source = "request.nodeFrom"),
        Mapping(target = "nodeTo", source = "request.nodeTo"),
    )
    fun createRouteEdgeFromCreateRequest(
        request: RouteEdgeController.RouteEdgeCreateRequest,
        @MappingTarget routeEdge: RouteEdge
    )

    /**
     * Route Edge Update Mapper
     */
    @Mappings(
        Mapping(target = "routeId", source = "request.routeId"),
        Mapping(target = "nodeFrom", source = "request.nodeFrom"),
        Mapping(target = "nodeTo", source = "request.nodeTo"),
    )
    fun updateRouteEdgeFromUpdateRequest(
        request: RouteEdgeController.RouteEdgeUpdateRequest,
        @MappingTarget routeEdge: RouteEdge
    )

    /**
     * Route Edges Update Mapper
     */
    @Mappings(
        Mapping(target = "id", source = "request.id"),
        Mapping(target = "routeId", source = "request.routeId"),
        Mapping(target = "nodeFrom", source = "request.nodeFrom"),
        Mapping(target = "nodeTo", source = "request.nodeTo"),
    )
    fun updateRouteEdgesFromUpdateRequest(
        request: RouteEdgeController.RouteEdgesItem,
        @MappingTarget routeEdge: RouteEdge
    )
}