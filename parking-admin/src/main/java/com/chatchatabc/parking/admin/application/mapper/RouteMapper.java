package com.chatchatabc.parking.admin.application.mapper;

import com.chatchatabc.parking.domain.model.Route;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface RouteMapper {

    record RouteMapDTO(
            String slug,
            String name,
            String description,
            String color,
            int status
    ) {
    }

    /**
     * Route Mapper
     */
    @Mappings({
            @Mapping(target = "slug", source = "request.slug"),
            @Mapping(target = "name", source = "request.name"),
            @Mapping(target = "description", source = "request.description"),
            @Mapping(target = "color", source = "request.color"),
            @Mapping(target = "status", source = "request.status"),
    })
    void mapRequestToRoute(
            RouteMapDTO request,
            @MappingTarget Route route
    );
}
