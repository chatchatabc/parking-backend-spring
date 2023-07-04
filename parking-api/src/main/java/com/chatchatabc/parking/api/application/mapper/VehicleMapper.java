package com.chatchatabc.parking.api.application.mapper;

import com.chatchatabc.parking.domain.model.Vehicle;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface VehicleMapper {

    record VehicleMapDTO(
            String name,
            String plateNumber,
            String modelUuid,
            String color,
            String year
    ) {
    }

    /**
     * Vehicle Mapper
     */
    @Mappings({
            @Mapping(target = "name", source = "request.name"),
            @Mapping(target = "plateNumber", source = "request.plateNumber"),
            @Mapping(target = "modelUuid", source = "request.modelUuid"),
            @Mapping(target = "color", source = "request.color"),
            @Mapping(target = "year", source = "request.year")
    })
    void mapRequestToVehicle(
            VehicleMapDTO request,
            @MappingTarget Vehicle vehicle
    );

}
