package com.chatchatabc.parking.admin.application.mapper;

import com.chatchatabc.parking.domain.model.Jeepney;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface JeepneyMapper {

    record JeepneyMapDTO(
            String jeepneyUuid,
            String name,
            String plateNumber,
            String routeUuid,
            String drivers,
            int capacity,
            double latitude,
            double longitude,
            String status,
            String flag
    ) {
    }

    /**
     * Jeepney Mapper
     */
    @Mappings({
            @Mapping(target = "jeepneyUuid", source = "request.jeepneyUuid"),
            @Mapping(target = "name", source = "request.name"),
            @Mapping(target = "plateNumber", source = "request.plateNumber"),
            @Mapping(target = "routeUuid", source = "request.routeUuid"),
            @Mapping(target = "drivers", source = "request.drivers"),
            @Mapping(target = "capacity", source = "request.capacity"),
            @Mapping(target = "latitude", source = "request.latitude"),
            @Mapping(target = "longitude", source = "request.longitude"),
            @Mapping(target = "status", source = "request.status"),
            @Mapping(target = "flag", source = "request.flag"),
    })
    void mapRequestToJeepney(
            JeepneyMapDTO request,
            @MappingTarget Jeepney jeepney
    );
}
