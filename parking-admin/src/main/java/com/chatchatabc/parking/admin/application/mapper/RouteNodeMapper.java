package com.chatchatabc.parking.admin.application.mapper;

import com.chatchatabc.parking.domain.model.RouteNode;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface RouteNodeMapper {
    record RouteNodeMapDTO(
            Long id,
            double latitude,
            double longitude,
            String poi
    ) {
    }

    /**
     * Route Node Mapper
     */
    @Mappings({
            @Mapping(target = "id", source = "request.id"),
            @Mapping(target = "latitude", source = "request.latitude"),
            @Mapping(target = "longitude", source = "request.longitude"),
            @Mapping(target = "poi", source = "request.poi"),
    })
    void mapRequestToRouteNode(
            RouteNodeMapDTO request,
            @MappingTarget RouteNode routeNode
    );
}
