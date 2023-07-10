package com.chatchatabc.parking.admin.application.mapper;

import com.chatchatabc.parking.domain.model.VehicleType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface VehicleTypeMapper {
    record VehicleTypeResponse(
            String name,
            int plateNumberType,
            int status
    ) {
    }

    /**
     * Map Vehicle Type from Request
     */
    @Mappings({
            @Mapping(target = "name", source = "request.name"),
            @Mapping(target = "status", source = "request.status"),
            @Mapping(target = "plateNumberType", source = "request.plateNumberType")
    })
    void mapRequestToVehicleType(
            VehicleTypeResponse request,
            @MappingTarget VehicleType vehicleType
    );
}
