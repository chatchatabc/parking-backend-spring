package com.chatchatabc.parking.admin.application.mapper;

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
            String brandUuid,
            String modelUuid,
            String typeUuid,
            String color,
            int year
    ) {
    }

    /**
     * Map Vehicle from Request
     */
    @Mappings({
            @Mapping(target = "name", source = "request.name"),
            @Mapping(target = "plateNumber", source = "request.plateNumber"),
            @Mapping(target = "brandUuid", source = "request.brandUuid"),
            @Mapping(target = "modelUuid", source = "request.modelUuid"),
            @Mapping(target = "typeUuid", source = "request.typeUuid"),
            @Mapping(target = "color", source = "request.color"),
            @Mapping(target = "year", source = "request.year"),
    })
    void mapRequestToVehicle(
            VehicleMapDTO request,
            @MappingTarget Vehicle vehicle
    );
}
