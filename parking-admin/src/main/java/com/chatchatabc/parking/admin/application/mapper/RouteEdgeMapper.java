package com.chatchatabc.parking.admin.application.mapper;

import com.chatchatabc.parking.domain.model.RouteEdge;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface RouteEdgeMapper {
    record RouteEdgeMapDTO(
            Long id,
            Long routeId,
            Long nodeFrom,
            Long nodeTo
    ) {
    }

    /**
     * Route Edge Mapper
     */
    @Mappings({
            @Mapping(target = "id", source = "request.id"),
            @Mapping(target = "routeId", source = "request.routeId"),
            @Mapping(target = "nodeFrom", source = "request.nodeFrom"),
            @Mapping(target = "nodeTo", source = "request.nodeTo"),
    })
    void mapRequestToRouteEdge(
            RouteEdgeMapDTO request,
            @MappingTarget RouteEdge routeEdge
    );
}
